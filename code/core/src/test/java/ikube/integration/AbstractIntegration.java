package ikube.integration;

import ikube.IConstants;
import ikube.database.IDataBase;
import ikube.index.IndexManager;
import ikube.integration.toolkit.DataUtilities;
import ikube.listener.ListenerManager;
import ikube.model.Action;
import ikube.model.IndexContext;
import ikube.model.medical.Address;
import ikube.toolkit.ApplicationContextManager;
import ikube.toolkit.FileUtilities;
import ikube.toolkit.Logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.Field.TermVector;
import org.apache.lucene.index.IndexWriter;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.junit.BeforeClass;
import org.junit.Ignore;

@Ignore
public abstract class AbstractIntegration {

	private static final Logger LOGGER = Logger.getLogger(AbstractIntegration.class);

	private static boolean INITIALIZED = Boolean.FALSE;

	@BeforeClass
	public static synchronized void beforeClass() {
		if (INITIALIZED) {
			return;
		}
		INITIALIZED = Boolean.TRUE;
		Logging.configure();
		try {
			FileUtilities.deleteFiles(new File("."), "btm1.tlog", "btm2.tlog", "ikube.h2.db", "ikube.lobs.db", "ikube.log", "openjpa.log");
			Thread.sleep(3000);
			startContext();
			Thread.sleep(3000);
			insertData();
		} catch (Exception e) {
			LOGGER.error("Exception inserting the data for the base test : ", e);
		}
	}

	private static void startContext() {
		ApplicationContextManager.getBean(ListenerManager.class).removeListeners();
		IDataBase dataBase = ApplicationContextManager.getBean(IDataBase.class);
		dataBase.find(Address.class, 0l);
		dataBase.find(ikube.model.File.class, 0l);
		ApplicationContextManager.getBean(ListenerManager.class).removeListeners();
	}

	private static void insertData() throws SQLException, FileNotFoundException {
		DataSource dataSource = ApplicationContextManager.getBean("nonXaDataSourceH2");
		Connection connection = dataSource.getConnection();
		File dotDirectory = new File(".");
		LOGGER.info("Dot directory : " + dotDirectory.getAbsolutePath());
		File allData = FileUtilities.findFileRecursively(dotDirectory, false, "allData");
		InputStream inputStream = new FileInputStream(allData);
		DataUtilities.setDataTypeFactory(new H2DataTypeFactory());
		DataUtilities.insertData(connection, inputStream);
	}

	public static void delete(final IDataBase dataBase, final Class<?>... klasses) {
		int batchSize = 1000;
		for (Class<?> klass : klasses) {
			try {
				List<?> list = dataBase.find(klass, 0, batchSize);
				do {
					dataBase.removeBatch(list);
					list = dataBase.find(klass, 0, batchSize);
				} while (list.size() > 0);
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	protected IndexContext<?> realIndexContext;
	protected Logger logger = Logger.getLogger(this.getClass());
	{
		realIndexContext = ApplicationContextManager.getBean("indexContext");
		realIndexContext.setAction(new Action());
		FileUtilities.deleteFile(new File(realIndexContext.getIndexDirectoryPath()), 1);
	}

	/**
	 * This method creates an index using the index path in the context, the time and the ip and returns the latest index directory, i.e.
	 * the index that has just been created. Note that if there are still cascading mocks from JMockit, the index writer sill not create the
	 * index! So you have to tear down all mocks prior to using this method.
	 * 
	 * @param indexContext the index context to use for the path to the index
	 * @param strings the data that must be in the index
	 * @return the latest index directory, i.e. the one that has just been created
	 */
	protected File createIndex(IndexContext<?> indexContext, String... strings) {
		IndexWriter indexWriter = null;
		String ip = null;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
			indexWriter = IndexManager.openIndexWriter(indexContext, System.currentTimeMillis(), ip);
			Document document = new Document();
			IndexManager.addStringField(IConstants.CONTENTS, "Michael Couck", document, Store.YES, Field.Index.ANALYZED, TermVector.YES);
			indexWriter.addDocument(document);
			for (String string : strings) {
				document = new Document();
				IndexManager.addStringField(IConstants.CONTENTS, string, document, Store.YES, Field.Index.ANALYZED, TermVector.YES);
				indexWriter.addDocument(document);
			}
		} catch (Exception e) {
			logger.error("Exception creating the index : ", e);
		} finally {
			IndexManager.closeIndexWriter(indexWriter);
		}
		File latestIndexDirectory = FileUtilities.getLatestIndexDirectory(indexContext.getIndexDirectoryPath());
		File serverIndexDirectory = new File(latestIndexDirectory, ip);
		logger.info("Created index in : " + serverIndexDirectory.getAbsolutePath());
		return latestIndexDirectory;
	}
	
	public static void main(String[] args) {
		FileUtilities.deleteFiles(new File("C:/Users/Administrator/AppData/Local/Temp"), "JF");
		FileUtilities.deleteFiles(new File("C:/Users/Administrator/db2admin/Local/Temp"), "JF", "xml", "txt", "dat", "Jetty", "ikube");
		FileUtilities.deleteFiles(new File("C:/Users/Administrator/Default/Local/Temp"), "JF", "xml", "txt", "dat", "Jetty", "ikube");
		FileUtilities.deleteFiles(new File("C:/Users/Administrator/Michael/Local/Temp"), "JF", "xml", "txt", "dat", "Jetty", "ikube");
		FileUtilities.deleteFiles(new File("C:/Users/Administrator/postgres/Local/Temp"), "JF", "xml", "txt", "dat", "Jetty", "ikube");
	}

}