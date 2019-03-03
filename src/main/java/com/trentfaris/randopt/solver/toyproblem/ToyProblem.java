package com.trentfaris.randopt.solver.toyproblem;

import com.trentfaris.randopt.solver.Solver;
import dist.Distribution;
import opt.EvaluationFunction;
import opt.OptimizationAlgorithm;

public abstract class ToyProblem extends Solver {
    protected int N;
    protected int[] ranges;

    protected EvaluationFunction evaluationFunction;
    protected Distribution distribution;
    protected OptimizationAlgorithm optimizationAlgorithm;

    public ToyProblem(String output, String name, int iterations) {
        super(output, name, iterations);
    }
}
