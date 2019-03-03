package com.trentfaris.randopt.solver.toyproblem.toyproblems.continuouspeaks;

import com.trentfaris.randopt.solver.toyproblem.ToyProblem;
import dist.DiscreteUniformDistribution;
import opt.example.ContinuousPeaksEvaluationFunction;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public abstract class ContinuousPeaks extends ToyProblem {
    private static final String OUTPUT = "continuouspeaks";
    private static final int T = 29;

    public ContinuousPeaks(String output, String name, int iterations) {
        super(OUTPUT + File.separator + output, name, iterations);

        this.N = 100;

        this.ranges = new int[N];
        Arrays.fill(ranges, 2);

        this.evaluationFunction = new ContinuousPeaksEvaluationFunction(T);
        this.distribution = new DiscreteUniformDistribution(ranges);
    }

    @Override
    protected void writeMetrics(int iteration, long time, CSVPrinter csvPrinter) throws IOException {
        ContinuousPeaksEvaluationFunction continuousPeaksEvaluationFunction = (ContinuousPeaksEvaluationFunction) evaluationFunction;

        double score = continuousPeaksEvaluationFunction.value(optimizationAlgorithm.getOptimal());
        long fevals = continuousPeaksEvaluationFunction.fevals;

        csvPrinter.printRecord(iteration, time, score, fevals);

        csvPrinter.flush();

        continuousPeaksEvaluationFunction.fevals -= 1;
    }
}
