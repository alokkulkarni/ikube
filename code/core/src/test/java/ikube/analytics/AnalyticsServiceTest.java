package ikube.analytics;

import ikube.AbstractTest;
import ikube.IConstants;
import ikube.analytics.weka.WekaClassifier;
import ikube.model.Analysis;
import ikube.model.AnalyzerInfo;
import ikube.model.Context;
import mockit.Deencapsulation;
import org.junit.Before;
import org.junit.Test;
import weka.classifiers.functions.SMO;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * @author Michael Couck
 * @version 01.00
 * @since 20-11-2013
 */
public class AnalyticsServiceTest extends AbstractTest {

    private Context context;
    private IAnalyzer analyzer;
    private Analysis<?, ?> analysis;
    private AnalyticsService analyticsService;

    @Before
    @SuppressWarnings("unchecked")
    public void before() throws Exception {
        analyzer = mock(IAnalyzer.class);
        analysis = mock(Analysis.class);

        SMO smo = mock(SMO.class);
        Filter filter = mock(Filter.class);
        AnalyzerInfo analyzerInfo = mock(AnalyzerInfo.class);

        when(analyzerInfo.getAnalyzer()).thenReturn(WekaClassifier.class.getName());
        when(analyzerInfo.getAlgorithm()).thenReturn(SMO.class.getName());
        when(analyzerInfo.getFilter()).thenReturn(StringToWordVector.class.getName());

        analyticsService = new AnalyticsService();

        context = mock(Context.class);
        when(context.getAnalyzerInfo()).thenReturn(analyzerInfo);
        when(context.getAlgorithm()).thenReturn(smo);
        when(context.getAnalyzer()).thenReturn(analyzer);
        when(context.getFilter()).thenReturn(filter);

        AnalyzerManager.getContexts().put(context.getName(), context);

        Deencapsulation.setField(analyticsService, "clusterManager", clusterManager);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void create() throws Exception {
        analyticsService.create(context);
        verify(context, atLeastOnce()).getName();

        IAnalyzer analyzer = analyticsService.new Creator(context).call();
        assertNotNull(analyzer);
        verify(context, atLeastOnce()).setFilter(any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void train() throws Exception {
        analyticsService.train(analysis);
        verify(analysis, atLeastOnce()).getAnalyzer();

        IAnalyzer analyzer = analyticsService.new Trainer(this.analysis).call();
        assertNotNull(analyzer);
        verify(analyzer, atLeastOnce()).train(any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void build() throws Exception {
        analyticsService.build(analysis);
        verify(analysis, atLeastOnce()).getAnalyzer();

        IAnalyzer analyzer = analyticsService.new Builder(this.analysis).call();
        assertNotNull(analyzer);
        verify(analyzer, atLeastOnce()).build(any(Context.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void analyze() throws Exception {
        Future future = mock(Future.class);
        when(clusterManager.sendTask(any(Callable.class))).thenReturn(future);
        when(future.get(anyLong(), any(TimeUnit.class))).thenReturn(analysis);
        when(analyzer.analyze(any())).thenReturn(analysis);

        Analysis analysis = analyticsService.analyze(this.analysis);
        assertEquals(analysis, this.analysis);

        when(analysis.isDistributed()).thenReturn(Boolean.TRUE);
        analyticsService.analyze(this.analysis);
        verify(clusterManager, atLeastOnce()).sendTask(any(Callable.class));

        verify(analyzer, atLeastOnce()).analyze(any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void classesOrClusters() throws Exception {
        Future future = mock(Future.class);
        when(clusterManager.sendTask(any(Callable.class))).thenReturn(future);
        when(future.get(anyLong(), any(TimeUnit.class))).thenReturn(analysis);
        when(analyzer.analyze(any())).thenReturn(analysis);

        Analysis analysis = analyticsService.classesOrClusters(this.analysis);
        assertEquals(analysis, this.analysis);

        when(this.analysis.isDistributed()).thenReturn(Boolean.TRUE);
        analyticsService.classesOrClusters(analysis);
        verify(analysis, atLeast(1)).setClassesOrClusters(any(Object[].class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void sizesForClassesOrClusters() throws Exception {
        Future future = mock(Future.class);
        when(clusterManager.sendTask(any(Callable.class))).thenReturn(future);
        when(future.get(anyLong(), any(TimeUnit.class))).thenReturn(analysis);
        when(analyzer.analyze(any())).thenReturn(analysis);

        when(analysis.getClassesOrClusters()).thenReturn(new Object[]{IConstants.POSITIVE, IConstants.NEGATIVE});
        Analysis analysis = analyticsService.sizesForClassesOrClusters(this.analysis);
        assertEquals(analysis, this.analysis);

        when(this.analysis.isDistributed()).thenReturn(Boolean.TRUE);
        analyticsService.sizesForClassesOrClusters(analysis);
        verify(analysis, atLeast(1)).setSizesForClassesOrClusters(any(int[].class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void destroy() throws Exception {
        Future future = mock(Future.class);
        when(clusterManager.sendTask(any(Callable.class))).thenReturn(future);
        when(future.get(anyLong(), any(TimeUnit.class))).thenReturn(analysis);

        analyticsService.destroy(context);
        verify(analyzer, atLeastOnce()).destroy(any(Context.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getAnalyzer() throws Exception {
        IAnalyzer analyzer = analyticsService.getAnalyzer(analysis.getAnalyzer());
        assertEquals(this.analyzer, analyzer);
    }

    @Test
    public void getAnalyzers() throws Exception {
        Map analyzers = analyticsService.getAnalyzers();
        assertNotNull(analyzers);
    }

}