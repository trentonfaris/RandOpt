package com.trentfaris.randopt.metric;

import func.nn.NeuralNetwork;
import shared.ErrorMeasure;
import shared.Instance;
import util.linalg.Vector;

public final class Metrics {
  private final double mse;
  private final double accuracy;
  private final ConfusionMatrix confusionMatrix;

  public Metrics(double mse, double accuracy, ConfusionMatrix confusionMatrix) {
    this.mse = mse;
    this.accuracy = accuracy;
    this.confusionMatrix = confusionMatrix;
  }

  public double getMse() {
    return mse;
  }

  public double getAccuracy() {
    return accuracy;
  }

  public ConfusionMatrix getConfusionMatrix() {
    return confusionMatrix;
  }

  public static Metrics computeMetrics(
      Instance[] instances,
      ErrorMeasure errorMeasure,
      NeuralNetwork neuralNetwork,
      int numClasses) {
    double error = 0;
    int positive = 0;

    int[] labels = new int[instances.length];
    int[] predictions = new int[instances.length];

    for (int i = 0; i < instances.length; i++) {
      Instance instance = instances[i];

      neuralNetwork.setInputValues(instance.getData());
      neuralNetwork.run();

      Vector outputValues = neuralNetwork.getOutputValues();

      int label = instance.getLabel().getDiscrete();
      labels[i] = label;

      int prediction = (int) Math.round(outputValues.get(0));
      predictions[i] = prediction;

      if (label == prediction) {
        positive += 1;
      }

      Instance output = instance.getLabel();
      Instance example = new Instance(outputValues, new Instance(outputValues.get(0)));

      error += errorMeasure.value(output, example);
    }

    double mse = error / instances.length;
    double accuracy = (double) positive / instances.length;
    ConfusionMatrix confusionMatrix = new ConfusionMatrix(labels, predictions, numClasses);

    return new Metrics(mse, accuracy, confusionMatrix);
  }
}
