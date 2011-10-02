package ikube.cluster.cache;

import ikube.IConstants;
import ikube.listener.ListenerManager;
import ikube.model.Server;
import ikube.toolkit.ApplicationContextManager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import org.apache.log4j.Logger;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.blocks.locking.LockService;

/**
 * @see ICache
 * @author Michael Couck
 * @since 01.10.11
 * @version 01.00
 */
public class CacheJGroups implements ICache {

	private Logger		logger;
	private JChannel	channel;
	private LockService	lockService;

	// private Map<String, ReplicatedHashMap<Long, Object>> maps;

	class ShutdownReceiverAdapter extends ReceiverAdapter {

		@Override
		public void viewAccepted(View view) {
			// logger.info("View : " + view);
			super.viewAccepted(view);
		}

		public void receive(Message message) {
			Address address = message.getSrc();
			Object other = message.getObject();
			logger.info("Message : " + message.getObject() + ", this : " + address + ", other : " + other);
			if (other == null || !Server.class.isAssignableFrom(other.getClass())) {
				return;
			}
			logger.warn("Got shutdown message : " + other);
			long delay = 1000;
			ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
			executorService.schedule(new Runnable() {
				public void run() {
					logger.warn("Shutting down Ikube server : " + this);
					ListenerManager.removeListeners();
					ApplicationContextManager.closeApplicationContext();
					channel.clearChannelListeners();
					channel.close();
					System.exit(0);
				}
			}, delay, TimeUnit.MILLISECONDS);
			executorService.shutdown();
		}
	}

	/**
	 * This method adds a shutdown hook that can be executed remotely causing the the cluster to close down, but not
	 * ourselves. This is useful when a unit test needs to run without the cluster running as the synchronisation will
	 * affect the tests.
	 */
	public void initialise() throws Exception {
		logger = Logger.getLogger(this.getClass());
		// maps = new HashMap<String, ReplicatedHashMap<Long, Object>>();
		String configurationFile = IConstants.META_INF + IConstants.SEP + IConstants.UDP_XML;
		InputStream inputStream = getClass().getResourceAsStream(configurationFile);
		channel = new JChannel(inputStream);
		// channel.setDiscardOwnMessages(Boolean.TRUE);
		channel.connect(IConstants.IKUBE);
		channel.setReceiver(new ShutdownReceiverAdapter());
		lockService = new LockService(channel);
		// channel.send(null, "Ikube running : " + channel.getAddressAsString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size(final String name) {
		return getMap(name).size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T extends Object> T get(final String name, final Long id) {
		return (T) getMap(name).get(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Object> void set(final String name, final Long id, final T object) {
		getMap(name).put(id, object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(final String name, final Long id) {
		getMap(name).remove(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T extends Object> List<T> get(final String name, final ICriteria<T> criteria, final IAction<T> action, final int size) {
		List<T> result = new ArrayList<T>();
		Map<Long, Object> map = getMap(name);
		for (Map.Entry<Long, Object> mapEntry : map.entrySet()) {
			if (result.size() >= size) {
				break;
			}
			T t = (T) mapEntry.getValue();
			if (criteria == null) {
				result.add(t);
			} else {
				if (criteria.evaluate(t)) {
					result.add(t);
				}
			}
			if (action != null) {
				action.execute(t);
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear(final String name) {
		getMap(name).clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Object> T get(final String name, final String sql) {
		throw new RuntimeException("This operation is not really required : ");
	}

	public boolean lock(final String name) {
		Lock lock = lockService.getLock(name);
		if (lock != null) {
			try {
				boolean gotLock = lock.tryLock(3000, TimeUnit.MILLISECONDS);
				return gotLock;
			} catch (InterruptedException e) {
				logger.error("Exception acquiring the cluster lock : " + name, e);
			}
		}
		return Boolean.FALSE;
	}

	public boolean unlock(String name) {
		Lock lock = lockService.getLock(name);
		if (lock != null) {
			lock.unlock();
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	private Map<Long, Object> getMap(String name) {
		// ReplicatedHashMap<Long, Object> map = maps.get(name);
		// if (map == null) {
		// try {
		// // JChannel channel = new JChannel();
		// // channel.connect(IConstants.IKUBE);
		// map = new ReplicatedHashMap<Long, Object>(channel);
		// map.setBlockingUpdates(Boolean.TRUE);
		// int timeout = 3000;
		// map.setTimeout(timeout);
		// map.start(timeout);
		// maps.put(name, map);
		// } catch (Exception e) {
		// logger.error("Exception opening the channel on the map : ", e);
		// }
		// }
		// return map;
		return null;
	}

}