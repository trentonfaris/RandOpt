package com.trentfaris.randopt.solver.toyproblem.toyproblems.continuouspeaks;

import opt.*;
import shared.FixedIterationTrainer;

public final class ContinuousPeaksSA extends ContinuousPeaks {
    private static final String OUTPUT = "SA";

    public ContinuousPeaksSA(String name, int iterations, double ce) {
        super(OUTPUT, name, iterations);

        NeighborFunction neighborFunction = new DiscreteChangeOneNeighbor(ranges);
        HillClimbingProblem hillClimbingProblem = new GenericHillClimbingProblem(evaluationFunction, distribution, neighborFunction);

        this.optimizationAlgorithm = new SimulatedAnnealing(1e10, ce, hillClimbingProblem);
        this.trainer = new FixedIterationTrainer(optimizationAlgorithm, 10);
    }
}
