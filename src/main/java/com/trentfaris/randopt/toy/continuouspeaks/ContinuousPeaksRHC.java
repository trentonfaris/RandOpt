package com.trentfaris.randopt.toy.continuouspeaks;

import opt.*;
import shared.FixedIterationTrainer;

public final class ContinuousPeaksRHC extends ContinuousPeaks {
    private static final String OUTPUT = "RHC";
    private final NeighborFunction neighborFunction;
    private final HillClimbingProblem hillClimbingProblem;

    public ContinuousPeaksRHC(String name, int id, int iterations) {
        super(OUTPUT, name, id, iterations);

        this.neighborFunction = new DiscreteChangeOneNeighbor(ranges);
        this.hillClimbingProblem =
                new GenericHillClimbingProblem(evaluationFunction, distribution, neighborFunction);
        this.trainer = new FixedIterationTrainer(new RandomizedHillClimbing(hillClimbingProblem), 10);
    }
}
