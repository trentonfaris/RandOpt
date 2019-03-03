package com.trentfaris.randopt.solver.optimizer.optimizers;

import com.trentfaris.randopt.solver.optimizer.Optimizer;
import func.nn.activation.RELU;
import func.nn.backprop.BackPropagationNetworkFactory;
import opt.SimulatedAnnealing;
import opt.example.NeuralNetworkOptimizationProblem;
import shared.DataSet;
import shared.SumOfSquaresError;

import java.io.IOException;

public final class SA extends Optimizer {
    private static final String OUTPUT = "SA";

    public SA(
            String name,
            int iterations,
            int numClasses,
            String train,
            String validation,
            String test,
            int[] layers,
            double ce)
            throws IOException {
        super(OUTPUT, name, iterations, numClasses, train, validation, test);

        DataSet dataSet = new DataSet(trainInstances);
        RELU relu = new RELU();

        this.neuralNetwork = new BackPropagationNetworkFactory().createClassificationNetwork(layers, relu);
        this.errorMeasure = new SumOfSquaresError();

        NeuralNetworkOptimizationProblem neuralNetworkOptimizationProblem = new NeuralNetworkOptimizationProblem(dataSet, neuralNetwork, errorMeasure);

        this.trainer = new SimulatedAnnealing(1e10, ce, neuralNetworkOptimizationProblem);
    }
}
