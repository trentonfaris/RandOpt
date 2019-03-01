package com.trentfaris.randopt.metric;

import java.util.stream.IntStream;

public final class ConfusionMatrix {
  private final int[][] matrix;
  private final double precision;
  private final double recall;
  private final double f1Score;

  public ConfusionMatrix(int[] labels, int[] predictions, int numClasses) {
    if (numClasses <= 0) {
      throw new IllegalArgumentException("Cannot create a ConfusionMatrix from no classes.");
    }

    if (labels.length == 0 || predictions.length == 0) {
      throw new IllegalArgumentException(
          "Cannot create a ConfusionMatrix from no labels or predictions.");
    }

    if (labels.length != predictions.length) {
      throw new IllegalArgumentException("Labels and predictions should have matching sizes.");
    }

    this.matrix = new int[numClasses][numClasses];

    int[] classes = IntStream.range(0, numClasses).toArray();

    int numInstances = labels.length;
    for (int r = 0; r < numClasses; r++) {
      for (int c = 0; c < numClasses; c++) {
        for (int i = 0; i < numInstances; i++) {
          int label = labels[i];
          int prediction = predictions[i];

          if (label == classes[r] && prediction == classes[c]) {
            matrix[r][c] += 1;
          }
        }
      }
    }

    double precisions = 0;
    for (int c = 0; c < numClasses; c++) {
      double colSum = 0;
      for (int r = 0; r < numClasses; r++) {
        colSum += matrix[r][c];
      }

      precisions += matrix[c][c] / colSum;
    }

    this.precision = precisions / numClasses;

    double recalls = 0;
    for (int r = 0; r < numClasses; r++) {
      double rowSum = 0;
      for (int c = 0; c < numClasses; c++) {
        rowSum += matrix[r][c];
      }

      recalls += matrix[r][r] / rowSum;
    }

    this.recall = recalls / numClasses;

    this.f1Score = 2 * precision * recall / (precision + recall);
  }

  public int[][] getMatrix() {
    return matrix;
  }

  public double getPrecision() {
    return precision;
  }

  public double getRecall() {
    return recall;
  }

  public double getF1Score() {
    return f1Score;
  }
}
