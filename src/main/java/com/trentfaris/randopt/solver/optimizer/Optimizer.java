package com.trentfaris.randopt.solver.optimizer;

import com.trentfaris.randopt.Utility;
import com.trentfaris.randopt.metric.Metrics;
import com.trentfaris.randopt.solver.Solver;
import func.nn.NeuralNetwork;
import org.apache.commons.csv.CSVPrinter;
import shared.ErrorMeasure;
import shared.Instance;

import java.io.IOException;

public abstract class Optimizer extends Solver {
    protected final int numClasses;

    protected final Instance[] trainInstances;
    protected final Instance[] validationInstances;
    protected final Instance[] testInstances;

    protected ErrorMeasure errorMeasure;
    protected NeuralNetwork neuralNetwork;

    public Optimizer(String output, String name, int iterations, int numClasses, String train, String validation, String test) throws IOException {
        super(output, name, iterations);

        this.numClasses = numClasses;

        this.trainInstances = Utility.getInstances(train);
        this.validationInstances = Utility.getInstances(validation);
        this.testInstances = Utility.getInstances(test);
    }

    @Override
    protected void writeMetrics(int iteration, long time, CSVPrinter csvPrinter) throws IOException {
        Metrics trainMetrics = Metrics.computeMetrics(trainInstances, errorMeasure, neuralNetwork, numClasses);
        Metrics validationMetrics = Metrics.computeMetrics(validationInstances, errorMeasure, neuralNetwork, numClasses);
        Metrics testMetrics = Metrics.computeMetrics(testInstances, errorMeasure, neuralNetwork, numClasses);

        csvPrinter.printRecord(
                iteration,
                time,
                trainMetrics.getMse(),
                validationMetrics.getMse(),
                testMetrics.getMse(),
                trainMetrics.getAccuracy(),
                validationMetrics.getAccuracy(),
                testMetrics.getAccuracy(),
                trainMetrics.getConfusionMatrix().getF1Score(),
                validationMetrics.getConfusionMatrix().getF1Score(),
                testMetrics.getConfusionMatrix().getF1Score());

        csvPrinter.flush();
    }
}
