package com.trentfaris.randopt.solver.toyproblem.toyproblems.tsp;

import dist.DiscreteDependencyTree;
import dist.DiscreteUniformDistribution;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import shared.FixedIterationTrainer;

public final class TravelingSalesmanMimic extends TravelingSalesman {
    private static final String OUTPUT = "Mimic";

    public TravelingSalesmanMimic(String name, int iterations, double m, int samples, int tokeep) {
        super(OUTPUT, name, iterations);

        this.distribution = new DiscreteUniformDistribution(ranges);

        DiscreteDependencyTree discreteDependencyTree = new DiscreteDependencyTree(m, ranges);
        GenericProbabilisticOptimizationProblem genericProbabilisticOptimizationProblem = new GenericProbabilisticOptimizationProblem(evaluationFunction, distribution, discreteDependencyTree);

        this.optimizationAlgorithm = new MIMIC(samples, tokeep, genericProbabilisticOptimizationProblem);
        this.trainer = new FixedIterationTrainer(optimizationAlgorithm, 10);
    }
}
