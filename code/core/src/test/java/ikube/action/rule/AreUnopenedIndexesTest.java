package ikube.action.rule;

import ikube.AbstractTest;
import ikube.toolkit.FileUtilities;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Michael Couck
 * @version 01.00
 * @since 29-03-2011
 */
public class AreUnopenedIndexesTest extends AbstractTest {

    /**
     * Class under test
     */
    private AreUnopenedIndexes areUnopenedIndexes;

    @Before
    public void before() {
        areUnopenedIndexes = new AreUnopenedIndexes();
    }

    @After
    public void after() {
        FileUtilities.deleteFile(new File(indexContext.getIndexDirectoryPath()));
    }

    @Test
    public void evaluate() throws Exception {
        File latestIndexDirectory = createIndexFileSystem(indexContext, "some words to index");

        Directory directory = NIOFSDirectory.open(latestIndexDirectory);
        logger.info("Directory : " + directory);
        IndexReader indexReader = DirectoryReader.open(directory);
        MultiReader multiReader = new MultiReader(indexReader);
        IndexSearcher searcher = new IndexSearcher(multiReader);

        Mockito.when(indexContext.getMultiSearcher()).thenReturn(searcher);

        Boolean result = areUnopenedIndexes.evaluate(indexContext);
        assertFalse(result);

        FileUtils.copyDirectory(
                latestIndexDirectory,
                new File(latestIndexDirectory.getParent(), "127.0.0.1.1234567890"));
        result = areUnopenedIndexes.evaluate(indexContext);
        assertTrue(result);
    }

}