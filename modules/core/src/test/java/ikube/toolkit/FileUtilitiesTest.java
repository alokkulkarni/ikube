package ikube.toolkit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import ikube.IConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael Couck
 * @since 21.11.10
 * @version 01.00
 */
public class FileUtilitiesTest {

	private File folder = new File(".");
	private File file = new File(folder, IConstants.READER_FILE_SUFFIX);
	private String[] stringPatterns = new String[] { IConstants.READER_FILE_SUFFIX };

	@Before
	public void before() {
		if (file.exists()) {
			file.delete();
		}
	}

	@After
	public void after() {
		if (file.exists()) {
			file.delete();
		}
	}

	@Test
	public void findFiles() throws Exception {
		assertFalse(file.exists());
		file.createNewFile();
		assertTrue(file.exists());

		File[] files = FileUtilities.findFiles(folder, stringPatterns);
		int initialLength = files.length;
		assertTrue(initialLength >= 1);
		file.delete();

		files = FileUtilities.findFiles(folder, stringPatterns);
		assertEquals(initialLength - 1, files.length);
	}

	@Test
	public void findFilesRecursively() throws Exception {
		assertFalse(file.exists());
		file.createNewFile();
		assertTrue(file.exists());

		List<File> files = FileUtilities.findFilesRecursively(folder, stringPatterns, new ArrayList<File>());
		int initialLength = files.size();
		assertTrue(initialLength >= 1);
		files.clear();

		files = FileUtilities.findFilesRecursively(folder, new String[] { ".xml" }, files);
		initialLength = files.size();
		assertTrue(initialLength >= 1);
	}

	@Test
	public void deleteFile() throws Exception {
		assertFalse(file.exists());
		file.createNewFile();
		assertTrue(file.exists());

		FileUtilities.deleteFile(file, 1);
		assertFalse(file.exists());
	}

	@Test
	public void deleteFiles() throws Exception {
		assertFalse(file.exists());
		file.createNewFile();
		assertTrue(file.exists());

		FileUtilities.deleteFiles(folder, stringPatterns);
		assertFalse(file.exists());
	}

	@Test
	public void findFile() {
		File file = FileUtilities.findFile(new File("."), new String[] { "mime-types.xml" });
		assertNotNull(file);
	}

	@Test
	public void setContents() throws Exception {
		String data = "Michael Couck";
		File tempFile = File.createTempFile("temp", ".file");
		FileUtilities.setContents(tempFile.getAbsolutePath(), data.getBytes());
		assertTrue(tempFile.exists());
		assertTrue(tempFile.length() > 5);
	}

}
