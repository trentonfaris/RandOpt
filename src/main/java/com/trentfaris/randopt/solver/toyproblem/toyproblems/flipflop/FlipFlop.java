package com.trentfaris.randopt.solver.toyproblem.toyproblems.flipflop;

import com.trentfaris.randopt.solver.toyproblem.ToyProblem;
import dist.DiscreteUniformDistribution;
import opt.example.FlipFlopEvaluationFunction;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public abstract class FlipFlop extends ToyProblem {
    private static final String OUTPUT = "flipflop";

    public FlipFlop(String output, String name, int iterations) {
        super(OUTPUT + File.separator + output, name, iterations);

        this.N = 1000;

        this.ranges = new int[N];
        Arrays.fill(ranges, 2);

        this.evaluationFunction = new FlipFlopEvaluationFunction();
        this.distribution = new DiscreteUniformDistribution(ranges);
    }

    @Override
    protected void writeMetrics(int iteration, long time, CSVPrinter csvPrinter) throws IOException {
        FlipFlopEvaluationFunction flipFlopEvaluationFunction = (FlipFlopEvaluationFunction) evaluationFunction;

        double score = flipFlopEvaluationFunction.value(optimizationAlgorithm.getOptimal());
        long fevals = flipFlopEvaluationFunction.fevals;

        csvPrinter.printRecord(iteration, time, score, fevals);

        csvPrinter.flush();

        flipFlopEvaluationFunction.fevals -= 1;
    }
}
