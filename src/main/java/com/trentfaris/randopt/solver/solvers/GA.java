package com.trentfaris.randopt.solver.solvers;

import com.trentfaris.randopt.solver.Solver;
import func.nn.activation.RELU;
import func.nn.backprop.BackPropagationNetworkFactory;
import opt.example.NeuralNetworkOptimizationProblem;
import opt.ga.StandardGeneticAlgorithm;
import shared.DataSet;
import shared.SumOfSquaresError;

import java.io.IOException;

public final class GA extends Solver {
  private static final String OUTPUT = "GA";

  public GA(
          String name,
          int numClasses,
          int iterations,
          int[] layers,
          String train,
          String validation,
          String test,
          int p,
          int mate,
          int mutate)
      throws IOException {
    super(OUTPUT, name, numClasses, iterations, train, validation, test);

    DataSet dataSet = new DataSet(trainInstances);
    RELU relu = new RELU();

    this.neuralNetwork =
        new BackPropagationNetworkFactory().createClassificationNetwork(layers, relu);
    this.errorMeasure = new SumOfSquaresError();

    NeuralNetworkOptimizationProblem neuralNetworkOptimizationProblem =
        new NeuralNetworkOptimizationProblem(dataSet, neuralNetwork, errorMeasure);

    this.trainer = new StandardGeneticAlgorithm(p, mate, mutate, neuralNetworkOptimizationProblem);
  }
}
