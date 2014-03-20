package ikube.analytics.action;

import ikube.analytics.IAnalyzer;
import ikube.model.Analysis;

/**
 * This class is just a serializable snippet of logic that can be distributed over the
 * wire and executed on a remote server, essentially distributing the analysis throughout
 * the cluster.
 *
 * @author Michael Couck
 * @version 01.00
 * @since 15-03-2014
 */
public class ClassesOrClusters extends Action<Analysis> {

    /**
     * The analysis object to use for the analysis
     */
    private Analysis analysis;

    public ClassesOrClusters(final Analysis analysis) {
        this.analysis = analysis;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Analysis call() throws Exception {
        // Get the remote analysis service
        IAnalyzer analyzer = getAnalyticsService().getAnalyzer(analysis.getAnalyzer());
        Object[] classesOrClusters = analyzer.classesOrClusters();
        analysis.setClassesOrClusters(classesOrClusters);
        // Return the analysis to the now local caller
        return analysis;
    }
}