package com.trentfaris.randopt.solver.toyproblem.toyproblems.tsp;

import opt.*;
import shared.FixedIterationTrainer;

public final class TravelingSalesmanRHC extends TravelingSalesman {
    private static final String OUTPUT = "RHC";

    public TravelingSalesmanRHC(String name, int iterations) {
        super(OUTPUT, name, iterations);

        NeighborFunction neighborFunction = new SwapNeighbor();
        HillClimbingProblem hillClimbingProblem = new GenericHillClimbingProblem(evaluationFunction, distribution, neighborFunction);

        this.optimizationAlgorithm = new RandomizedHillClimbing(hillClimbingProblem);
        this.trainer = new FixedIterationTrainer(optimizationAlgorithm, 10);
    }
}
