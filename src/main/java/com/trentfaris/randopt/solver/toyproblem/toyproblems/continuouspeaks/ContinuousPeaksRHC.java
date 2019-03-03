package com.trentfaris.randopt.solver.toyproblem.toyproblems.continuouspeaks;

import opt.*;
import shared.FixedIterationTrainer;

public final class ContinuousPeaksRHC extends ContinuousPeaks {
    private static final String OUTPUT = "RHC";

    public ContinuousPeaksRHC(String name, int iterations) {
        super(OUTPUT, name, iterations);

        NeighborFunction neighborFunction = new DiscreteChangeOneNeighbor(ranges);
        HillClimbingProblem hillClimbingProblem = new GenericHillClimbingProblem(evaluationFunction, distribution, neighborFunction);

        this.optimizationAlgorithm = new RandomizedHillClimbing(hillClimbingProblem);
        this.trainer = new FixedIterationTrainer(optimizationAlgorithm, 10);
    }
}
