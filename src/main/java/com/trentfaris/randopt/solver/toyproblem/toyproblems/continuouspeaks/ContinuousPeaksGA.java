package com.trentfaris.randopt.solver.toyproblem.toyproblems.continuouspeaks;

import opt.ga.*;
import shared.FixedIterationTrainer;

public final class ContinuousPeaksGA extends ContinuousPeaks {
    private static final String OUTPUT = "GA";

    public ContinuousPeaksGA(String name, int iterations, int pop, int mate, int mutate) {
        super(OUTPUT, name, iterations);

        MutationFunction mutationFunction = new DiscreteChangeOneMutation(ranges);
        SingleCrossOver singleCrossOver = new SingleCrossOver();
        GenericGeneticAlgorithmProblem genericGeneticAlgorithmProblem = new GenericGeneticAlgorithmProblem(evaluationFunction, distribution, mutationFunction, singleCrossOver);

        this.optimizationAlgorithm = new StandardGeneticAlgorithm(pop, mate, mutate, genericGeneticAlgorithmProblem);
        this.trainer = new FixedIterationTrainer(optimizationAlgorithm, 10);
    }
}
