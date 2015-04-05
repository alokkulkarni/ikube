package ikube.data;

import co.uk.hjcs.canyon.session.Session;
import co.uk.hjcs.canyon.session.SessionFactory;
import ikube.IConstants;
import ikube.database.ADataBaseJpa;
import ikube.database.DataBaseJpaDb2;
import ikube.database.DataBaseJpaH2;
import ikube.database.IDataBase;
import ikube.model.geospatial.GeoName;
import ikube.toolkit.OBJECT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * This class will read the Geonames data from the CSV file and persist the
 * entities as {@link GeoName} objects in the database using the JPA from Ikube.
 *
 * @author Michael Couck
 * @version 01.00
 * @since long time
 */
public final class GeonamePopulator extends ADatabase {

	static Logger LOGGER = LoggerFactory.getLogger(GeonamePopulator.class);

	public static void main(String[] args) throws Exception {
		persist(GeoName.class);
		// persist(GeoAltName.class);
	}

	protected static void persist(final Class<?> clazz) throws Exception {
		int start = 0;
		int count = 0;
		int batchSize = 10000;
		String sessionName = "geoname";
		Session session = SessionFactory.getSession(sessionName);
		ADataBaseJpa dataBase = getDataBase(DataBaseJpaDb2.class, IConstants.PERSISTENCE_UNIT_DB2);
		List<Object> geoNames = new ArrayList<>();
		skipTo(clazz, session, start);
		while (session.hasNext(clazz)) {
			count++;
			try {
				Object geoName = session.next(clazz);
				geoNames.add(geoName);
				if (geoNames.size() >= batchSize || !session.hasNext(clazz)) {
					LOGGER.info("Count : " + count);
					persistBatch(dataBase, geoNames);
				}
			} catch (Exception e) {
				LOGGER.error("Exception inserting geoname : ", e);
				persistBatch(dataBase, geoNames);
			}
		}
	}

	private static void skipTo(final Class<?> clazz, final Session session, final int index) {
		int start = index;
		while (session.hasNext(clazz) && start-- > 0) {
			try {
				session.next(clazz);
			} catch (Exception e) {
				LOGGER.error("Exception scrolling to the correct index in the data : ", e);
			}
		}
	}

	private static void persistBatch(IDataBase dataBase, List<Object> geoNames) {
		EntityManager entityManager = (EntityManager) OBJECT.getFieldValue(dataBase, "entityManager");
		// EntityManager entityManager = Deencapsulation.getField(dataBase, EntityManager.class);
		try {
			entityManager.getTransaction().begin();
			dataBase.persistBatch(geoNames);
			geoNames.clear();
		} catch (Exception e) {
			LOGGER.error("Exception inserting geoname : ", e);
		} finally {
			for (final Object geoName : geoNames) {
				try {
					dataBase.persist(geoName);
				} catch (Exception ex) {
					LOGGER.error(ex.getMessage());
				}
			}
			geoNames.clear();
			entityManager.flush();
			entityManager.clear();
			entityManager.getTransaction().commit();
		}
	}

}