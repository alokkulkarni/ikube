package ikube.action.rule;

import ikube.model.IndexContext;

import org.apache.log4j.Logger;
import org.apache.lucene.search.MultiSearcher;

/**
 * @author Michael Couck
 * @since 12.02.2011
 * @version 01.00
 */
public class AreSearchablesInitialised implements IRule<IndexContext> {

	private static final transient Logger LOGGER = Logger.getLogger(AreSearchablesInitialised.class);

	public boolean evaluate(final  IndexContext indexContext) {
		// No searchables, also try to reopen an index searcher
		if (indexContext.getIndex() == null) {
			return Boolean.FALSE;
		}
		MultiSearcher searcher = indexContext.getIndex().getMultiSearcher();
		if (searcher == null || searcher.getSearchables() == null || searcher.getSearchables().length == 0) {
			LOGGER.debug("No searchables open, should try to reopen : ");
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

}