package ikube;

import ikube.listener.ListenerManager;
import ikube.model.IndexContext;
import ikube.model.faq.Attachment;
import ikube.model.faq.Faq;
import ikube.model.medical.Address;
import ikube.model.medical.Administration;
import ikube.model.medical.Condition;
import ikube.model.medical.Doctor;
import ikube.model.medical.Hospital;
import ikube.model.medical.Inpatient;
import ikube.model.medical.Medication;
import ikube.model.medical.Patient;
import ikube.model.medical.Person;
import ikube.model.medical.Record;
import ikube.model.medical.Treatment;
import ikube.toolkit.ApplicationContextManager;
import ikube.toolkit.FileUtilities;
import ikube.toolkit.PerformanceTester;
import ikube.toolkit.data.DataGeneratorFour;

import java.io.File;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * @author Michael Couck
 * @since 21.11.10
 * @version 01.00
 */
public abstract class BaseTest extends ATest {

	/** We only want the before class to execute once for the whole test batch. */
	private static boolean INIT = Boolean.FALSE;

	@BeforeClass
	public static void beforeClass() {
		if (INIT) {
			return;
		}
		INIT = Boolean.TRUE;
		ApplicationContextManager.getApplicationContext(IConstants.SPRING_CONFIGURATION_FILE);
		// Delete all the old index directories
		Map<String, IndexContext> contexts = ApplicationContextManager.getBeans(IndexContext.class);
		for (IndexContext indexContext : contexts.values()) {
			File baseIndexDirectory = FileUtilities.getFile(indexContext.getIndexDirectoryPath(), Boolean.TRUE);
			FileUtilities.deleteFile(baseIndexDirectory, 1);
		}
		try {
			final EntityManager entityManager = Persistence.createEntityManagerFactory(IConstants.PERSISTENCE_UNIT_NAME).createEntityManager();
			PerformanceTester.execute(new PerformanceTester.APerform() {
				@Override
				public void execute() throws Exception {
					try {
						Class<?>[] classes = new Class[] { Faq.class, Attachment.class, Address.class, Administration.class, Condition.class,
								Doctor.class, Hospital.class, Inpatient.class, Medication.class, Patient.class, Person.class, Record.class,
								Treatment.class };
						DataGeneratorFour dataGenerator = new DataGeneratorFour(entityManager, 10, classes);
						dataGenerator.before();
						dataGenerator.generate();
						dataGenerator.after();
					} finally {
						if (entityManager != null) {
							entityManager.close();
						}
					}
				}
			}, "Data generator two insertion : ", 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Remove all the listeners as they create havoc
		ListenerManager.removeListeners();
	}

	@AfterClass
	public static void afterClass() {
		// Try to delete all the old index files
		Map<String, IndexContext> contexts = ApplicationContextManager.getBeans(IndexContext.class);
		for (IndexContext indexContext : contexts.values()) {
			File indexDirectory = FileUtilities.getFile(indexContext.getIndexDirectoryPath(), Boolean.TRUE);
			FileUtilities.deleteFile(indexDirectory, 1);
		}
	}

	public BaseTest(Class<?> subClass) {
		super(subClass);
	}

	protected IndexContext indexContext = ApplicationContextManager.getBean("indexContext");

}