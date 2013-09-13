package ikube.scheduling.schedule;

import ikube.IConstants;
import ikube.action.index.IndexManager;
import ikube.cluster.IClusterManager;
import ikube.cluster.IMonitorService;
import ikube.database.IDataBase;
import ikube.model.Action;
import ikube.model.IndexContext;
import ikube.model.Search;
import ikube.model.Server;
import ikube.model.Snapshot;
import ikube.scheduling.Schedule;
import ikube.toolkit.Logging;
import ikube.toolkit.ThreadUtilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This schedule will take a snapshot of various system states periodically, including the cpu, how many searches there have been on all the indexes etc.
 * Snapshots are then persisted to the database, and cleaned from time to time in the {@link Purge} action so the database doesn't fill up. Typically we only
 * need a few snapshots and not two weeks worth. This schedule should run every minute.
 * 
 * @author Michael Couck
 * @since 22.07.12
 * @version 01.00
 */
public class SnapshotSchedule extends Schedule {

	private static final Logger LOGGER = LoggerFactory.getLogger(SnapshotSchedule.class);

	static final int MAX_SNAPSHOTS_CONTEXT = 90;
	static final double ONE_MINUTE_MILLIS = 60000;

	@Autowired
	private IDataBase dataBase;
	@Autowired
	private IMonitorService monitorService;
	@Autowired
	private IClusterManager clusterManager;

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void run() {
		Server server = clusterManager.getServer();
		setServerStatistics(server);
		Map<String, IndexContext> indexContexts = monitorService.getIndexContexts();
		for (final Map.Entry<String, IndexContext> mapEntry : indexContexts.entrySet()) {
			try {
				IndexContext indexContext = mapEntry.getValue();
				indexContext.setNumDocsForSearchers(IndexManager.getNumDocsForIndexSearchers(indexContext));
				indexContext.setIndexing(indexContext.getIndexWriters() != null && indexContext.getIndexWriters().length > 0);

				Snapshot snapshot = new Snapshot();

				snapshot.setAvailableProcessors(server.getProcessors());
				snapshot.setSystemLoad(server.getAverageCpuLoad());

				snapshot.setIndexContext(indexContext.getName());
				snapshot.setTimestamp(new Timestamp(System.currentTimeMillis()));

				snapshot.setNumDocsForIndexWriters(IndexManager.getNumDocsForIndexWriters(indexContext));
				snapshot.setIndexSize(IndexManager.getIndexSize(indexContext));
				snapshot.setLatestIndexTimestamp(IndexManager.getLatestIndexDirectoryDate(indexContext));

				snapshot.setDocsPerMinute(getDocsPerMinute(indexContext, snapshot));
				snapshot.setSearchesPerMinute(getSearchesPerMinute(indexContext, snapshot));
				snapshot.setTotalSearches(getTotalSearchesForIndex(indexContext).longValue());

				dataBase.persist(snapshot);
				String[] names = new String[] { IConstants.INDEX_CONTEXT };
				Object[] values = new Object[] { indexContext.getName() };
				List<Snapshot> snapshots = dataBase.find(Snapshot.class, Snapshot.SELECT_SNAPSHOTS_ORDER_BY_TIMESTAMP_DESC, names, values, 0,
						MAX_SNAPSHOTS_CONTEXT);
				List<Snapshot> sortedSnapshots = sortSnapshots(snapshots);
				indexContext.setSnapshots(sortedSnapshots);

				// Find the last snapshot and put it in the action if there is one
				// executing on the index context
				for (final Action action : server.getActions()) {
					if (action.getIndexName().equals(indexContext.getIndexName())) {
						action.setSnapshot(snapshot);
						break;
					}
				}
			} catch (Exception e) {
				LOGGER.error("Exception persisting snapshot : ", e);
			}
		}
	}

	protected List<Snapshot> sortSnapshots(final List<Snapshot> snapshots) {
		List<Snapshot> sortedSnapshots = new ArrayList<Snapshot>(snapshots);
		// We have the last snapshots, now reverse the order for the gui
		Comparator<Snapshot> comparator = new Comparator<Snapshot>() {
			@Override
			public int compare(Snapshot o1, Snapshot o2) {
				return o1.getTimestamp().compareTo(o2.getTimestamp());
			}
		};
		Collections.sort(sortedSnapshots, comparator);
		return sortedSnapshots;
	}

	protected void setServerStatistics(final Server server) {
		OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
		server.setArchitecture(operatingSystemMXBean.getArch());
		server.setProcessors(operatingSystemMXBean.getAvailableProcessors());
		server.setAverageCpuLoad(operatingSystemMXBean.getSystemLoadAverage());
		server.setFreeMemory(Runtime.getRuntime().freeMemory() / IConstants.MILLION);
		server.setMaxMemory(Runtime.getRuntime().maxMemory() / IConstants.MILLION);
		server.setTotalMemory(Runtime.getRuntime().totalMemory() / IConstants.MILLION);
		server.setThreadsRunning(ThreadUtilities.isInitialized());
		server.setAge(System.currentTimeMillis());
		server.setTimestamp(new Timestamp(System.currentTimeMillis()));
		try {
			// long availableDiskSpace = FileSystemUtils.freeSpaceKb("/") / IConstants.MILLION;
			// server.setFreeDiskSpace(availableDiskSpace);
		} catch (Exception e) {
			LOGGER.error("Exception accessing the disk space : ", e);
		}
		setLogTail(server);
	}

	void setLogTail(final Server server) {
		File logFile = Logging.getLogFile();
		if (logFile == null || !logFile.exists() || !logFile.isFile() || !logFile.canRead()) {
			String message = "Can't find log file : " + logFile;
			LOGGER.warn(message);
			System.err.println(message);
			return;
		}
		RandomAccessFile inputStream = null;
		try {
			inputStream = new RandomAccessFile(logFile, "r");
			// 1000000
			int fileLength = (int) logFile.length();
			// 900000
			int offset = Math.max(fileLength - (IConstants.MILLION / 100), 0);
			// 100000
			int lengthToRead = Math.max(0, fileLength - offset);
			// 100000
			byte[] bytes = new byte[lengthToRead];
			inputStream.seek(offset);
			inputStream.read(bytes, 0, lengthToRead);
			server.setLogTail(new String(bytes));
		} catch (FileNotFoundException e) {
			LOGGER.error("Log file not found : " + e.getMessage());
		} catch (Exception e) {
			LOGGER.error("Error reading log file : ", e);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
				LOGGER.error("Exception closing the file reader on the log file : ", e);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	protected Number getTotalSearchesForIndex(final IndexContext indexContext) {
		return dataBase.execute(Search.SELECT_FROM_SEARCH_COUNT_SEARCHES, new String[] { "indexName" }, new Object[] { indexContext.getIndexName() });
	}

	protected long getSearchesPerMinute(final IndexContext<?> indexContext, final Snapshot snapshot) {
		List<Snapshot> snapshots = indexContext.getSnapshots();
		if (snapshots == null || snapshots.size() < 1) {
			return 0;
		}
		Snapshot previous = snapshots.get(snapshots.size() - 1);
		double ratio = getRatio(previous, snapshot);
		long searchesPerMinute = (long) ((snapshot.getTotalSearches() - previous.getTotalSearches()) / ratio);
		return Math.max(0, searchesPerMinute);
	}

	protected long getDocsPerMinute(final IndexContext<?> indexContext, final Snapshot snapshot) {
		List<Snapshot> snapshots = indexContext.getSnapshots();
		if (snapshots == null || snapshots.size() == 0) {
			return 0;
		}
		Snapshot previous = snapshots.get(snapshots.size() - 1);
		double ratio = getRatio(previous, snapshot);
		long docsPerMinute = (long) ((snapshot.getNumDocsForIndexWriters() - previous.getNumDocsForIndexWriters()) / ratio);
		// We can never do a million documents per minute with the hardware
		// that we have at the current time, perhaps with nano cpus and 3d memory
		docsPerMinute = docsPerMinute < 0 ? 0 : Math.min(docsPerMinute, IConstants.MILLION);
		// If the previous docs per minute was 1000000 and this one is 0 then the index was just
		// opened and it looks like there are a million documents per minute, but we'll correct that
		return docsPerMinute;
	}

	protected double getRatio(final Snapshot previous, final Snapshot snapshot) {
		double interval = snapshot.getTimestamp().getTime() - previous.getTimestamp().getTime();
		return interval / ONE_MINUTE_MILLIS;
	}

}