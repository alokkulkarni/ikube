package ikube.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import ikube.BaseTest;
import ikube.IConstants;
import ikube.listener.Event;
import ikube.model.IndexContext;
import ikube.model.SynchronizationMessage;
import ikube.toolkit.ApplicationContextManager;
import ikube.toolkit.FileUtilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.MessageListener;

/**
 * @author Michael Couck
 * @since 12.10.2010
 * @version 01.00
 */
@Ignore
public class SynchronizeTest extends BaseTest {

	private transient final Synchronize synchronize = ApplicationContextManager.getBean(Synchronize.class);

	@BeforeClass
	public static void beforeClass() {
		Map<String, IndexContext> contexts = ApplicationContextManager.getBeans(IndexContext.class);
		for (IndexContext indexContext : contexts.values()) {
			File baseIndexDirectory = FileUtilities.getFile(indexContext.getIndexDirectoryPath(), Boolean.TRUE);
			FileUtilities.deleteFile(baseIndexDirectory, 1);
		}
	}

	@Test
	public void getIndexFiles() {
		List<File> indexFiles = synchronize.getIndexFiles();
		logger.info("Files : " + indexFiles);
		assertEquals("There should be no indexes : ", 0, indexFiles.size());

		String serverIndexDirectoryPath = getServerIndexDirectoryPath(indexContext);
		File indexDirectory = new File(serverIndexDirectoryPath);
		createIndex(indexDirectory);

		indexFiles = synchronize.getIndexFiles();
		logger.info("Files : " + indexFiles);
		assertEquals("There should be an index now : ", 3, indexFiles.size());

		FileUtilities.deleteFile(indexDirectory, 1);
	}

	@Test
	public void execute() throws InterruptedException {
		// Event
		final List<SynchronizationMessage> synchronizationMessages = new ArrayList<SynchronizationMessage>();
		ITopic<SynchronizationMessage> topic = Hazelcast.getTopic(IConstants.SYNCHRONIZATION_TOPIC);
		MessageListener<SynchronizationMessage> messageListener = new MessageListener<SynchronizationMessage>() {
			@Override
			public void onMessage(final SynchronizationMessage synchronizationMessage) {
				logger.info("SynchronizationMessage : " + synchronizationMessage);
				synchronizationMessages.add(synchronizationMessage);
			}
		};
		topic.addMessageListener(messageListener);

		Event event = new Event();
		event.setType(Event.SYNCHRONISE);

		synchronize.execute(indexContext);

		long sleep = 1000;
		Thread.sleep(sleep);
		assertEquals("There should be no messages yet : ", 0, synchronizationMessages.size());

		String serverIndexDirectoryPath = getServerIndexDirectoryPath(indexContext);
		File indexDirectory = new File(serverIndexDirectoryPath);
		createIndex(indexDirectory);

		synchronize.execute(indexContext);

		Thread.sleep(sleep);
		assertEquals("There should be one message : ", 1, synchronizationMessages.size());

		FileUtilities.deleteFile(indexDirectory, 1);
		topic.removeMessageListener(messageListener);
	}

	@Test
	public void onMessage() throws UnknownHostException {
		// SynchronizationMessage
		String serverIndexDirectoryPath = getServerIndexDirectoryPath(indexContext);
		File indexDirectory = new File(serverIndexDirectoryPath);
		createIndex(indexDirectory);

		File segmentsFile = FileUtilities.findFile(indexDirectory, "segments.gen");

		SynchronizationMessage synchronizationMessage = new SynchronizationMessage();
		synchronizationMessage.setFilePath(segmentsFile.getAbsolutePath());
		synchronizationMessage.setIp(InetAddress.getLocalHost().getHostAddress());
		synchronize.onMessage(synchronizationMessage);

		FileUtilities.deleteFile(indexDirectory, 1);
	}

	@Test
	public void writeFile() throws IOException {
		// TODO - How can we test this without deploying to different Jvm(s)?
		String serverIndexDirectoryPath = getServerIndexDirectoryPath(indexContext);
		File indexDirectory = new File(serverIndexDirectoryPath);
		createIndex(indexDirectory);

		Socket socket = mock(Socket.class);
		OutputStream outputStream = new ByteArrayOutputStream();
		when(socket.getOutputStream()).thenReturn(outputStream);
		synchronize.writeFile(socket);

		FileUtilities.deleteFile(indexDirectory, 1);
		assertTrue(true);
	}

}
