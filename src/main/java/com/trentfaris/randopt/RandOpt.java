package com.trentfaris.randopt;

import com.trentfaris.randopt.solver.solvers.Backprop;
import com.trentfaris.randopt.solver.solvers.GA;
import com.trentfaris.randopt.solver.solvers.RHC;
import com.trentfaris.randopt.solver.solvers.SA;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RandOpt {
  public static final String OUTPUT = "output";

  private static final String TEST_PHISHING = "data/phishing_test.csv";
  private static final String TRAIN_PHISHING = "data/phishing_train.csv";
  private static final String VALIDATION_PHISHING = "data/phishing_validation.csv";

  private static final int p = 50;
  private static final int[] mates = new int[] {20, 10};
  private static final int[] mutates = new int[] {20, 10};
  private static final double[] ces = new double[] {0.15, 0.35, 0.55, 0.7, 0.95};
  private static final int[] layers = new int[]{60, 60, 1};
  private static final int numClasses = 2;
  private static final int iterations = 3000;
  private static final String name = "phishing";

  public static void main(String[] args) {
    long start = System.currentTimeMillis();

    File file = new File(OUTPUT);
    if (!file.exists()) {
      if (!file.mkdir()) {
        System.out.println("RandOpt output directory already exists. Skipping creation.");
      }
    }

    List<Callable<Object>> callables = new ArrayList<>();
    try {
      // Backprop
      callables.add(
          new Backprop(
                  name,
                  numClasses,
                  iterations,
                  layers,
              TRAIN_PHISHING,
              VALIDATION_PHISHING,
                  TEST_PHISHING));

      // GA
      for (int mate : mates) {
        for (int mutate : mutates) {
          callables.add(
              new GA(
                      name + "_" + mate + "_" + mutate + "_",
                      numClasses,
                      iterations,
                      layers,
                  TRAIN_PHISHING,
                  VALIDATION_PHISHING,
                      TEST_PHISHING,
                  p,
                  mate,
                      mutate));
        }
      }

      // RHC
      callables.add(
          new RHC(
                  name,
                  numClasses,
                  iterations,
                  layers,
              TRAIN_PHISHING,
              VALIDATION_PHISHING,
                  TEST_PHISHING));

      // SA
      for (double ce : ces) {
        callables.add(
            new SA(
                    name + "_" + ce + "_",
                    numClasses,
                    iterations,
                    layers,
                TRAIN_PHISHING,
                VALIDATION_PHISHING,
                    TEST_PHISHING,
                    ce));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    ExecutorService executorService = Executors.newWorkStealingPool();

    try {
      executorService.invokeAll(callables);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    long end = System.currentTimeMillis();

    System.out.println("Finished executing in " + ((end - start) / 1000.0) + " seconds.");
  }
}
