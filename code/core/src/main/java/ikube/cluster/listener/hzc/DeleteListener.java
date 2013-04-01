package ikube.cluster.listener.hzc;

import ikube.action.index.IndexManager;
import ikube.cluster.IMonitorService;
import ikube.cluster.listener.IListener;
import ikube.model.IndexContext;
import ikube.scheduling.schedule.Event;
import ikube.toolkit.FileUtilities;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;

/**
 * This class will delete the index directory completely and the backup directory too. This guarantees that the index will be re-created.
 * 
 * @author Michael Couck
 * @version 01.00
 * @since 15.12.12
 */
public class DeleteListener implements IListener<Message<Object>>, MessageListener<Object> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private IMonitorService monitorService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMessage(final Message<Object> message) {
		Object object = message.getMessageObject();
		if (object != null && Event.class.isAssignableFrom(object.getClass())) {
			Event event = (Event) object;
			if (Event.DELETE_INDEX.equals(event.getType())) {
				IndexContext<?> indexContext = monitorService.getIndexContext(event.getObject().toString());
				String indexDirectoryPath = IndexManager.getIndexDirectoryPath(indexContext);
				String indexDirectoryBackupPath = IndexManager.getIndexDirectoryPathBackup(indexContext);
				logger.warn("Deleting index directory : " + indexDirectoryPath);
				logger.warn("Deleting backup index directory : " + indexDirectoryBackupPath);
				FileUtilities.deleteFile(new File(indexDirectoryPath), 3);
				FileUtilities.deleteFile(new File(indexDirectoryBackupPath), 3);
			}
		}
	}

}
