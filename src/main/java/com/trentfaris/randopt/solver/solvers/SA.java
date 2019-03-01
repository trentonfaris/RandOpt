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
          String name,
          int numClasses,
          int iterations,
          int[] layers,
          String train,
          String validation,
          String test,
          double ce)
      throws IOException {
    super(OUTPUT, name, numClasses, iterations, train, validation, test);

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
