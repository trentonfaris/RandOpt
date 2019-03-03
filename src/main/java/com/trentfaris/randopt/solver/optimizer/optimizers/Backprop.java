package com.trentfaris.randopt.solver.optimizer.optimizers;

import com.trentfaris.randopt.solver.optimizer.Optimizer;
import func.nn.activation.RELU;
import func.nn.backprop.BackPropagationNetwork;
import func.nn.backprop.BackPropagationNetworkFactory;
import func.nn.backprop.BatchBackPropagationTrainer;
import func.nn.backprop.RPROPUpdateRule;
import shared.DataSet;
import shared.GradientErrorMeasure;
import shared.SumOfSquaresError;

import java.io.IOException;

public final class Backprop extends Optimizer {
    private static final String OUTPUT = "Backprop";

    public Backprop(
            String name,
            int iterations,
            int numClasses,
            String train,
            String validation,
            String test,
            int[] layers)
            throws IOException {
        super(OUTPUT, name, iterations, numClasses, train, validation, test);

        DataSet dataSet = new DataSet(trainInstances);
        RELU relu = new RELU();
        RPROPUpdateRule rpropUpdateRule = new RPROPUpdateRule(0.064, 50, 0.000001);

        this.neuralNetwork = new BackPropagationNetworkFactory().createClassificationNetwork(layers, relu);
        this.errorMeasure = new SumOfSquaresError();
        this.trainer = new BatchBackPropagationTrainer(
                dataSet,
                (BackPropagationNetwork) neuralNetwork,
                (GradientErrorMeasure) errorMeasure,
                rpropUpdateRule
        );
    }
}
