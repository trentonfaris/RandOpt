package com.trentfaris.randopt.solver.toyproblem.toyproblems.tsp;

import opt.*;
import shared.FixedIterationTrainer;

public final class TravelingSalesmanSA extends TravelingSalesman {
    private static final String OUTPUT = "SA";

    public TravelingSalesmanSA(String name, int iterations, double ce) {
        super(OUTPUT, name, iterations);

        NeighborFunction neighborFunction = new SwapNeighbor();
        HillClimbingProblem hillClimbingProblem = new GenericHillClimbingProblem(evaluationFunction, distribution, neighborFunction);

        this.optimizationAlgorithm = new SimulatedAnnealing(1e10, ce, hillClimbingProblem);
        this.trainer = new FixedIterationTrainer(optimizationAlgorithm, 10);
    }
}
