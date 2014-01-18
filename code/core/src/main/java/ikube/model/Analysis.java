package ikube.model;

import javax.persistence.*;

import weka.classifiers.functions.SMO;

/**
 * This class represents data that is to be analyzed as well as the results from the analysis if any.
 *
 * @author Michael Couck
 * @version 01.00
 * @since 10.04.13
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Analysis<Input, Output> extends Persistable {

    /**
     * The name of the analyzer in the system, for example clusterer-em.
     */
    private String analyzer;

    /**
     * The class/cluster for the instance.
     */
    private String clazz;
    /**
     * The input data, for training too.
     */
    private Input input;
    /**
     * The output data, could be a string or a double array for distribution.
     */
    private Output output;
    /**
     * An algorithm specific output, could be toString from the {@link SMO} function.
     */
    private String algorithmOutput;
    /**
     * The correlation co-efficients for the data set.
     */
    @Transient
    private double[][] correlationCoefficients;
    @Transient
    private double[][] distributionForInstances;

    private double duration;
    private Exception exception;
    private boolean correlation;
    private boolean compressed;
    private boolean distribution;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Buildable buildable;

    public String getAnalyzer() {
        return analyzer;
    }

    public void setAnalyzer(String analyzer) {
        this.analyzer = analyzer;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public Input getInput() {
        return input;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public Output getOutput() {
        return output;
    }

    public void setOutput(Output output) {
        this.output = output;
    }

    public String getAlgorithmOutput() {
        return algorithmOutput;
    }

    public void setAlgorithmOutput(String algorithmOutput) {
        this.algorithmOutput = algorithmOutput;
    }

    public double[][] getCorrelationCoefficients() {
        return correlationCoefficients;
    }

    public void setCorrelationCoefficients(double[][] correlationCoefficients) {
        this.correlationCoefficients = correlationCoefficients;
    }

    public double[][] getDistributionForInstances() {
        return distributionForInstances;
    }

    public void setDistributionForInstances(double[][] distributionForInstances) {
        this.distributionForInstances = distributionForInstances;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public boolean isCorrelation() {
        return correlation;
    }

    public void setCorrelation(boolean correlation) {
        this.correlation = correlation;
    }

    public boolean isCompressed() {
        return compressed;
    }

    public void setCompressed(boolean compressed) {
        this.compressed = compressed;
    }

    public boolean isDistribution() {
        return distribution;
    }

    public void setDistribution(boolean distribution) {
        this.distribution = distribution;
    }

    public Buildable getBuildable() {
        return buildable;
    }

    public void setBuildable(Buildable buildable) {
        this.buildable = buildable;
    }
}