package com.trentfaris.randopt.solver;

import com.trentfaris.randopt.RandOpt;
import com.trentfaris.randopt.Utility;
import com.trentfaris.randopt.metric.Metrics;
import func.nn.NeuralNetwork;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import shared.ErrorMeasure;
import shared.Instance;
import shared.Trainer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

public abstract class Solver implements Callable<Object> {
  private final int numClasses;
  private final int iterations;
  private final String output;
  private final String name;

  protected final Instance[] trainInstances;
  protected final Instance[] validationInstances;
  protected final Instance[] testInstances;

  protected ErrorMeasure errorMeasure;
  protected NeuralNetwork neuralNetwork;
  protected Trainer trainer;

  public Solver(
          int numClasses,
          int iterations,
          String train,
          String validation,
          String test,
          String output,
          String name)
      throws IOException {
    this.numClasses = numClasses;
    this.iterations = iterations;
    this.output = output;
    this.name = name;

    this.trainInstances = Utility.getInstances(train);
    this.validationInstances = Utility.getInstances(validation);
    this.testInstances = Utility.getInstances(test);

    File file = new File(RandOpt.OUTPUT + File.separator + output);
    if (!file.exists()) {
      if (!file.mkdir()) {
        System.out.println(
            "Solver output directory " + output + " already exists. Skipping creation.");
      }
    }
  }

  @Override
  public final Object call() throws IOException {
    if (trainer == null) {
      throw new IllegalStateException("Cannot train with a null trainer.");
    }

    List<Long> times = new ArrayList<>();
    times.add(0L);

    BufferedWriter writer =
        Files.newBufferedWriter(
            Paths.get(
                RandOpt.OUTPUT
                    + File.separator
                    + output
                    + File.separator
                        + name
                        + "_"
                    + new Date().getTime()
                    + ".csv"));
    CSVPrinter csvPrinter =
        new CSVPrinter(
            writer,
            CSVFormat.DEFAULT.withHeader(
                "Iteration",
                "Train MSE",
                "Validation MSE",
                "Test MSE",
                "Train Accuracy",
                "Validation Accuracy",
                "Test Accuracy",
                "Train F1 " + "Score",
                "Validation F1 Score",
                "Test F1 Score",
                "Time"));

    for (int i = 0; i < iterations; i++) {
      long start = System.currentTimeMillis();

      trainer.train();

      times.add(times.get(times.size() - 1) + (System.currentTimeMillis() - start));

      if (i % 100 == 0) {
        System.out.println("Output: " + output + ", Iteration: " + i);
      } else if (i % 10 == 0) {
        Metrics trainMetrics =
            Metrics.computeMetrics(trainInstances, errorMeasure, neuralNetwork, numClasses);
        Metrics validationMetrics =
            Metrics.computeMetrics(validationInstances, errorMeasure, neuralNetwork, numClasses);
        Metrics testMetrics =
            Metrics.computeMetrics(testInstances, errorMeasure, neuralNetwork, numClasses);

        csvPrinter.printRecord(
            Integer.toString(i),
            trainMetrics.getMse(),
            validationMetrics.getMse(),
            testMetrics.getMse(),
            trainMetrics.getAccuracy(),
            validationMetrics.getAccuracy(),
            testMetrics.getAccuracy(),
            trainMetrics.getConfusionMatrix().getF1Score(),
            validationMetrics.getConfusionMatrix().getF1Score(),
            testMetrics.getConfusionMatrix().getF1Score(),
            times.get(times.size() - 1));

        csvPrinter.flush();
      }
    }

    return null;
  }
}
