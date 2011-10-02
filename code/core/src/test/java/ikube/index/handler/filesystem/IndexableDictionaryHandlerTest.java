package ikube.index.handler.filesystem;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import ikube.ATest;
import ikube.model.IndexableDictionary;
import ikube.toolkit.FileUtilities;
import ikube.toolkit.ThreadUtilities;

import java.io.File;
import java.util.List;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael Couck
 * @since 01.10.11
 * @version 01.00
 */
public class IndexableDictionaryHandlerTest extends ATest {

	public IndexableDictionaryHandlerTest() {
		super(IndexableDictionaryHandlerTest.class);
	}

	@Before
	public void before() {
		File dictionaryIndexDirectory = FileUtilities.getFile(INDEX_CONTEXT.getIndexDirectoryPath(), Boolean.TRUE);
		FileUtilities.deleteFile(dictionaryIndexDirectory, 1);
	}

	@After
	public void after() {
		File dictionaryIndexDirectory = FileUtilities.getFile(INDEX_CONTEXT.getIndexDirectoryPath(), Boolean.TRUE);
		FileUtilities.deleteFile(dictionaryIndexDirectory, 1);
	}

	@Test
	public void handle() throws Exception {
		IndexableDictionary indexableDictionary = mock(IndexableDictionary.class);
		File dictionariesDirectory = FileUtilities.findFileRecursively(new File("."), "dictionaries");
		when(indexableDictionary.getPath()).thenReturn(dictionariesDirectory.getAbsolutePath());
		IndexableDictionaryHandler dictionaryHandler = new IndexableDictionaryHandler();
		dictionaryHandler.setThreads(1);
		List<Thread> threads = dictionaryHandler.handle(INDEX_CONTEXT, indexableDictionary);
		ThreadUtilities.waitForThreads(threads);
		File dictionaryIndexDirectory = FileUtilities.getFile(INDEX_CONTEXT.getIndexDirectoryPath(), Boolean.TRUE);
		boolean indexExists = IndexReader.indexExists(FSDirectory.open(dictionaryIndexDirectory));
		assertTrue("The dictionary index should be created : ", indexExists);
	}

}
