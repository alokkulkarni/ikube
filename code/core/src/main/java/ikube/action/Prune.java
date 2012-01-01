package ikube.action;

import ikube.IConstants;
import ikube.database.IDataBase;
import ikube.model.IndexContext;

import java.util.List;

/**
 * This class will prune any data that needs to be cleaned in the database from time to time.
 * 
 * @author Michael Couck
 * @since 29.09.2011
 * @version 01.00
 */
public class Prune extends Action<IndexContext<?>, Boolean> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean execute(final IndexContext<?> indexContext) {
		ikube.model.Action action = null;
		try {
			action = start(indexContext.getIndexName(), "");
			delete(dataBase, ikube.model.Action.class, new String[] { "startTime" }, new boolean[] { true },
					(int) IConstants.MAX_ACTIONS / 5);
		} finally {
			stop(action);
		}
		return Boolean.TRUE;
	}

	protected void delete(final IDataBase dataBase, final Class<?> klass, final String[] fieldsToSortOn, final boolean[] directionOfSort,
			final int batchSize) {
		List<?> entities = dataBase.find(klass, fieldsToSortOn, directionOfSort, 0, batchSize);
		if (entities.size() >= batchSize) {
			do {
				dataBase.removeBatch(entities);
				entities = dataBase.find(klass, fieldsToSortOn, directionOfSort, 0, batchSize);
			} while (entities.size() >= batchSize);
		}
	}

}