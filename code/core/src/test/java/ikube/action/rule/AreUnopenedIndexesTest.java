package ikube.action.rule;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import ikube.AbstractTest;
import ikube.toolkit.FileUtilities;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael Couck
 * @since 29.03.2011
 * @version 01.00
 */
public class AreUnopenedIndexesTest extends AbstractTest {

	private AreUnopenedIndexes areUnopenedIndexes;

	public AreUnopenedIndexesTest() {
		super(AreUnopenedIndexesTest.class);
	}

	@Before
	public void before() {
		areUnopenedIndexes = new AreUnopenedIndexes();
		FileUtilities.deleteFile(new File(indexContext.getIndexDirectoryPath()), 1);
	}

	@After
	public void after() {
		FileUtilities.deleteFile(new File(indexContext.getIndexDirectoryPath()), 1);
	}

	@Test
	@SuppressWarnings("deprecation")
	public void evaluate() {
		boolean result = areUnopenedIndexes.evaluate(indexContext);
		assertFalse(result);

		File latestIndexDirectory = createIndexFileSystem(indexContext, "some words to index");
		result = areUnopenedIndexes.evaluate(indexContext);
		assertTrue(result);

		when(fsDirectory.getFile()).thenReturn(latestIndexDirectory);
		result = areUnopenedIndexes.evaluate(indexContext);
		assertFalse(result);
	}

}