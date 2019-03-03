package com.trentfaris.randopt.solver.toyproblem.toyproblems.tsp;

import com.trentfaris.randopt.solver.toyproblem.ToyProblem;
import dist.DiscretePermutationDistribution;
import opt.example.TravelingSalesmanRouteEvaluationFunction;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public abstract class TravelingSalesman extends ToyProblem {
    private static final String OUTPUT = "travelingsalesman";

    protected double[][] points;

    public TravelingSalesman(String output, String name, int iterations) {
        super(OUTPUT + File.separator + output, name, iterations);

        this.N = 100;

        this.ranges = new int[N];
        Arrays.fill(ranges, N);

        this.points = new double[N][2];

        Random random = new Random();
        for (int i = 0; i < N; i++) {
            points[i][0] = random.nextDouble();
            points[i][1] = random.nextDouble();
        }

        this.evaluationFunction = new TravelingSalesmanRouteEvaluationFunction(points);
        this.distribution = new DiscretePermutationDistribution(N);
    }

    @Override
    protected void writeMetrics(int iteration, long time, CSVPrinter csvPrinter) throws IOException {
        TravelingSalesmanRouteEvaluationFunction travelingSalesmanRouteEvaluationFunction = (TravelingSalesmanRouteEvaluationFunction) evaluationFunction;

        double score = travelingSalesmanRouteEvaluationFunction.value(optimizationAlgorithm.getOptimal());
        long fevals = travelingSalesmanRouteEvaluationFunction.fevals;

        csvPrinter.printRecord(iteration, time, score, fevals);

        csvPrinter.flush();

        travelingSalesmanRouteEvaluationFunction.fevals -= 1;
    }
}
