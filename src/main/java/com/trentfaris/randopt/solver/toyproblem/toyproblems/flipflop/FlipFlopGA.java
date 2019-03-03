package com.trentfaris.randopt.solver.toyproblem.toyproblems.flipflop;

import opt.ga.*;
import shared.FixedIterationTrainer;

public final class FlipFlopGA extends FlipFlop {
    private static final String OUTPUT = "GA";

    public FlipFlopGA(String name, int iterations, int pop, int mate, int mutate) {
        super(OUTPUT, name, iterations);

        MutationFunction mutationFunction = new DiscreteChangeOneMutation(ranges);
        SingleCrossOver singleCrossOver = new SingleCrossOver();
        GenericGeneticAlgorithmProblem genericGeneticAlgorithmProblem = new GenericGeneticAlgorithmProblem(evaluationFunction, distribution, mutationFunction, singleCrossOver);

        this.optimizationAlgorithm = new StandardGeneticAlgorithm(pop, mate, mutate, genericGeneticAlgorithmProblem);
        this.trainer = new FixedIterationTrainer(optimizationAlgorithm, 10);
    }
}
