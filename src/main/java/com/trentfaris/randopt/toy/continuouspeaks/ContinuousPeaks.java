package com.trentfaris.randopt.toy.continuouspeaks;

import com.trentfaris.randopt.toy.Toy;
import dist.DiscreteUniformDistribution;
import opt.example.ContinuousPeaksEvaluationFunction;

import java.io.File;
import java.util.Arrays;

public abstract class ContinuousPeaks extends Toy {
    private static final String OUTPUT = "ContinuousPeaks";
    private static final int N = 100;
    private static final int T = 29;

    protected final int[] ranges;

    public ContinuousPeaks(String output, String name, int id, int iterations) {
        super(OUTPUT + File.separator + output, name, id, iterations);

        this.ranges = new int[N];
        Arrays.fill(ranges, 2);

        this.evaluationFunction = new ContinuousPeaksEvaluationFunction(T);
        this.distribution = new DiscreteUniformDistribution(ranges);
    }
}
