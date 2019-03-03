package com.trentfaris.randopt.solver.toyproblem.toyproblems.flipflop;

import opt.*;
import shared.FixedIterationTrainer;

public final class FlipFlopRHC extends FlipFlop {
    private static final String OUTPUT = "RHC";

    public FlipFlopRHC(String name, int iterations) {
        super(OUTPUT, name, iterations);

        NeighborFunction neighborFunction = new DiscreteChangeOneNeighbor(ranges);
        HillClimbingProblem hillClimbingProblem = new GenericHillClimbingProblem(evaluationFunction, distribution, neighborFunction);

        this.optimizationAlgorithm = new RandomizedHillClimbing(hillClimbingProblem);
        this.trainer = new FixedIterationTrainer(optimizationAlgorithm, 10);
    }
}
