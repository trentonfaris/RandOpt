package com.trentfaris.randopt.solver.toyproblem.toyproblems.continuouspeaks;

import dist.DiscreteDependencyTree;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import shared.FixedIterationTrainer;

public final class ContinuousPeaksMimic extends ContinuousPeaks {
    private static final String OUTPUT = "Mimic";

    public ContinuousPeaksMimic(String name, int iterations, double m, int samples, int tokeep) {
        super(OUTPUT, name, iterations);

        DiscreteDependencyTree discreteDependencyTree = new DiscreteDependencyTree(m, ranges);
        GenericProbabilisticOptimizationProblem genericProbabilisticOptimizationProblem = new GenericProbabilisticOptimizationProblem(evaluationFunction, distribution, discreteDependencyTree);

        this.optimizationAlgorithm = new MIMIC(samples, tokeep, genericProbabilisticOptimizationProblem);
        this.trainer = new FixedIterationTrainer(optimizationAlgorithm, 10);
    }
}
