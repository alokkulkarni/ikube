package ikube.action.index.handler.strategy;

import ikube.IConstants;
import ikube.action.index.handler.IStrategy;
import ikube.analytics.IAnalyzer;
import ikube.model.Analysis;
import ikube.model.Context;
import ikube.model.IndexContext;
import ikube.model.Indexable;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import static ikube.IConstants.CLASSIFICATION;

/**
 * @author Michael Couck
 * @version 01.00
 * @since 02-12-2013
 */
public class ClassifierTrainingStrategy extends AStrategy {

    private String language;
    private int buildThreshold = 100;
    private Context<?, ?, ?, ?> context;
    private Map<String, Boolean> trained;
    private ReentrantLock reentrantLock;

    public ClassifierTrainingStrategy() {
        this(null);
    }

    public ClassifierTrainingStrategy(final IStrategy nextStrategy) {
        super(nextStrategy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean aroundProcess(final IndexContext<?> indexContext, final Indexable<?> indexable,
                                 final Document document, final Object resource) throws Exception {
        String language = document.get(IConstants.LANGUAGE);
        String classification = document.get(CLASSIFICATION);
        String content = indexable.getContent() != null ? indexable.getContent().toString() : resource != null ? resource.toString() : null;
        if (language != null && this.language.equals(language) &&
                !StringUtils.isEmpty(classification) && !StringUtils.isEmpty(StringUtils.stripToEmpty(content))) {
            train(classification, content);
        }
        return super.aroundProcess(indexContext, indexable, document, resource);
    }

    @SuppressWarnings("unchecked")
    void train(final String clazz, final String content) throws Exception {
        Boolean trained = this.trained.get(clazz);
        if (trained != null && trained) {
            return;
        }
        try {
            reentrantLock.lock();
            IAnalyzer classifier = (IAnalyzer) context.getAnalyzer();
            Analysis<String, double[]> analysis = new Analysis<>();
            analysis.setClazz(clazz);
            analysis.setInput(content);
            int classSize = classifier.sizeForClassOrCluster(analysis);
            trained = classSize == 0 || classSize >= context.getMaxTraining();
            this.trained.put(clazz, trained);
            try {
                classifier.train(analysis);
                if (classSize % 10 == 0) {
                    logger.info("Training : " + clazz + ", language : " + this.language + ", class size : " + classSize);
                }
                if (buildThreshold == 0) {
                    buildThreshold = 100;
                }
                if (classSize % buildThreshold == 0) {
                    logger.info("Building : " + clazz + ", language : " + this.language + ", class size : " + classSize + ", build threshold : " + buildThreshold);
                    classifier.build(context);
                    buildThreshold = classSize / 10;
                }
            } catch (final Exception e) {
                logger.error("Exception building classifier : ", e);
            }
        } finally {
            reentrantLock.unlock();
        }
    }

    public void initialize() {
        trained = new HashMap<>();
        reentrantLock = new ReentrantLock(Boolean.TRUE);
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setBuildThreshold(int buildThreshold) {
        this.buildThreshold = buildThreshold;
    }

    public void setContext(Context<?, ?, ?, ?> context) {
        this.context = context;
    }
}