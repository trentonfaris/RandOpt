package com.trentfaris.randopt.solver.toyproblem.toyproblems.tsp;

import opt.example.TravelingSalesmanCrossOver;
import opt.example.TravelingSalesmanEvaluationFunction;
import opt.ga.*;
import shared.FixedIterationTrainer;

public final class TravelingSalesmanGA extends TravelingSalesman {
    private static final String OUTPUT = "GA";

    public TravelingSalesmanGA(String name, int iterations, int pop, int mate, int mutate) {
        super(OUTPUT, name, iterations);

        MutationFunction mutationFunction = new SwapMutation();
        CrossoverFunction crossoverFunction = new TravelingSalesmanCrossOver((TravelingSalesmanEvaluationFunction) evaluationFunction);
        GeneticAlgorithmProblem geneticAlgorithmProblem = new GenericGeneticAlgorithmProblem(evaluationFunction, distribution, mutationFunction, crossoverFunction);

        this.optimizationAlgorithm = new StandardGeneticAlgorithm(pop, mate, mutate, geneticAlgorithmProblem);
        this.trainer = new FixedIterationTrainer(optimizationAlgorithm, 10);
    }
}
