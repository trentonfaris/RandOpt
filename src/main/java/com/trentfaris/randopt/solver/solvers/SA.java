package com.trentfaris.randopt.solver.solvers;

import com.trentfaris.randopt.solver.Solver;
import func.nn.activation.RELU;
import func.nn.backprop.BackPropagationNetworkFactory;
import opt.SimulatedAnnealing;
import opt.example.NeuralNetworkOptimizationProblem;
import shared.DataSet;
import shared.SumOfSquaresError;

import java.io.IOException;

public final class SA extends Solver {
  private static final String OUTPUT = "SA";

  public SA(
      double ce,
      int[] layers,
      int numClasses,
      int iterations,
      String train,
      String validation,
      String test)
      throws IOException {
    super(numClasses, iterations, train, validation, test, OUTPUT);

    DataSet dataSet = new DataSet(trainInstances);
    RELU relu = new RELU();

    this.neuralNetwork =
        new BackPropagationNetworkFactory().createClassificationNetwork(layers, relu);
    this.errorMeasure = new SumOfSquaresError();

    NeuralNetworkOptimizationProblem neuralNetworkOptimizationProblem =
        new NeuralNetworkOptimizationProblem(dataSet, neuralNetwork, errorMeasure);

    this.trainer = new SimulatedAnnealing(1e10, ce, neuralNetworkOptimizationProblem);
  }
}
