package ikube.analytics.weka;

import com.google.common.collect.Lists;
import ikube.model.Analysis;
import ikube.model.Context;
import ikube.toolkit.ThreadUtilities;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import static com.google.common.collect.Lists.newArrayList;
import static ikube.toolkit.ThreadUtilities.waitForAnonymousFutures;

/**
 * This is a wrapper for the Weka classifiers. It is essentially a holder with some methods for
 * building and training and using the underlying Weka classification(any one, for example {@link weka.classifiers.functions.SMO}) algorithm.
 *
 * @author Michael Couck
 * @version 01.00
 * @since 14-08-2013
 */
public class WekaClassifier extends WekaAnalyzer {

    @Override
    public void init(final Context context) {
        try {
            super.init(context);
            Instances[] instanceses = (Instances[]) context.getModels();
            for (final Instances instances : instanceses) {
                instances.setClassIndex(0);
            }
            // Set the logistic models so that the distribution for instances will be
            // the actual probabilities and not the class index, i.e. 0.29993949 and not 1 or 0
            for (final Object algorithm : context.getAlgorithms()) {
                if (SMO.class.isAssignableFrom(algorithm.getClass())) {
                    ((SMO) algorithm).setBuildLogisticModels(Boolean.TRUE);
                }
            }
        } catch (final Exception e) {
            logger.error("Exception building analyzer : ", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void build(final Context context) throws Exception {
        // If this analyzer can be persisted, then first check the file system
        // for serialized classifiers that have already been built
        List<Future> futures = newArrayList();
        final String[] evaluations = new String[context.getAlgorithms().length];
        final List<Object> classifiers = Lists.newArrayList();
        if (context.isPersisted()) {
            Object[] deserializeAnalyzers = deserializeAnalyzers(context);
            if (deserializeAnalyzers != null) {
                classifiers.addAll(Arrays.asList(deserializeAnalyzers));
            }
        }
        if (classifiers.isEmpty()) {
            for (int i = 0; i < context.getAlgorithms().length; i++) {
                final int index = i;
                class ClassifierBuilder implements Runnable {
                    public void run() {
                        try {
                            // Get the components to create the model
                            Classifier classifier = (Classifier) context.getAlgorithms()[index];
                            Instances instances = (Instances) context.getModels()[index];
                            Filter filter = getFilter(context, index);

                            // Filter the data if necessary
                            Instances filteredInstances = filter(instances, filter);
                            filteredInstances.setRelationName("filtered-instances");

                            // And build the model
                            logger.info("Building classifier : " + instances.numInstances() + ", " + context.getName());
                            classifier.buildClassifier(filteredInstances);
                            logger.info("Classifier built : " + filteredInstances.numInstances());
                            classifiers.add(classifier);

                            // Set the evaluation of the classifier and the training model
                            evaluations[index] = evaluate(classifier, filteredInstances);
                        } catch (final Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                Future<?> future = ThreadUtilities.submit(this.getClass().getName(), new ClassifierBuilder());
                futures.add(future);
            }
        }
        waitForAnonymousFutures(futures, Long.MAX_VALUE);
        context.setAlgorithms(classifiers.toArray());

        context.setBuilt(Boolean.TRUE);
        context.setEvaluations(evaluations);
        if (context.isPersisted()) {
            serializeAnalyzers(context);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean train(final Context context, final Analysis analysis) throws Exception {
        for (int i = 0; i < context.getAlgorithms().length; i++) {
            Instances instances = (Instances) context.getModels()[i];
            Instance instance = instance(analysis.getInput(), instances);
            instance.setClassValue(analysis.getClazz());
            instances.add(instance);
        }
        return Boolean.TRUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Analysis<Object, Object> analyze(final Context context, final Analysis analysis) throws Exception {
        String majorityClass = null;
        double[] majorityDistributionForInstance = null;
        StringBuilder algorithmsOutput = new StringBuilder();

        double highestProbability = -1.0;
        for (int i = 0; i < context.getAlgorithms().length; i++) {
            Classifier classifier = (Classifier) context.getAlgorithms()[i];
            Instances instances = (Instances) context.getModels()[i];
            Filter filter = getFilter(context, i);

            // Create the instance from the data
            Object input = analysis.getInput();
            Instance instance = instance(input, instances);
            instance.setMissing(0);

            // Classify the instance
            Instance filteredInstance = filter(instance, filter);
            double classification = classifier.classifyInstance(filteredInstance);
            String clazz = instances.classAttribute().value((int) classification);
            double[] distributionForInstance = classifier.distributionForInstance(filteredInstance);

            for (final double probability : distributionForInstance) {
                if (probability > highestProbability) {
                    majorityClass = clazz;
                    highestProbability = probability;
                    majorityDistributionForInstance = distributionForInstance;
                }
            }

            if (analysis.isAddAlgorithmOutput()) {
                algorithmsOutput.append(classifier.toString());
                algorithmsOutput.append("\n\r\n\r");
            }
        }

        analysis.setClazz(majorityClass);
        analysis.setOutput(majorityDistributionForInstance);
        analysis.setAlgorithmOutput(algorithmsOutput.toString());

        return analysis;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    double[][] distributionForInstance(final Context context, final Instance instance) throws Exception {
        double[][] distributionForInstance = new double[context.getAlgorithms().length][];
        Object[] classifiers = context.getAlgorithms();
        Object[] filters = context.getFilters();
        for (int i = 0; i < classifiers.length; i++) {
            Classifier classifier = (Classifier) classifiers[i];

            Filter filter = null;
            if (filters != null && filters.length > i) {
                filter = (Filter) filters[i];
            }

            // Instance filteredInstance = filter(instance, filter);
            Instance filteredInstance = filter((Instance) instance.copy(), filter);
            distributionForInstance[i] = classifier.distributionForInstance(filteredInstance);
        }
        return distributionForInstance;
    }

    private String evaluate(final Classifier classifier, final Instances instances) throws Exception {
        Evaluation evaluation = new Evaluation(instances);
        evaluation.evaluateModel(classifier, instances);
        return evaluation.toSummaryString();
    }

    @SuppressWarnings("UnusedDeclaration")
    private void log(final Instances instances) throws Exception {
        int numClasses = instances.numClasses();
        int numAttributes = instances.numAttributes();
        int numInstances = instances.numInstances();
        String expression = //
            "Classes : " + numClasses + //
                ", instances : " + numInstances +
                ", attributes : " + numAttributes;
        logger.info(expression);
    }

}