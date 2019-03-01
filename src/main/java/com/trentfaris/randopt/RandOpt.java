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

  private static final String TEST_SATIMAGE = "data/satimage_test.csv";
  private static final String TRAIN_SATIMAGE = "data/satimage_train.csv";
  private static final String VALIDATION_SATIMAGE = "data/satimage_validation.csv";

  private static final int p = 50;
  private static final int[] mates = new int[] {20, 10};
  private static final int[] mutates = new int[] {20, 10};
  private static final double[] ces = new double[] {0.15, 0.35, 0.55, 0.7, 0.95};
  private static final int[] phishingLayers = new int[] {60, 60, 1};
  private static final int[] satimageLayers = new int[] {72, 72, 72, 1};
  private static final int phishingNumClasses = 2;
  private static final int satimageNumClasses = 7;
  private static final int phishingIterations = 3000;
  private static final int satimageIterations = 3000;

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
              phishingLayers,
              phishingNumClasses,
              phishingIterations,
              TRAIN_PHISHING,
              VALIDATION_PHISHING,
              TEST_PHISHING));
      callables.add(
          new Backprop(
              satimageLayers,
              satimageNumClasses,
              satimageIterations,
              TRAIN_SATIMAGE,
              VALIDATION_SATIMAGE,
              TEST_SATIMAGE));

      // GA
      for (int mate : mates) {
        for (int mutate : mutates) {
          callables.add(
              new GA(
                  p,
                  mate,
                  mutate,
                  phishingLayers,
                  phishingNumClasses,
                  phishingIterations,
                  TRAIN_PHISHING,
                  VALIDATION_PHISHING,
                  TEST_PHISHING));
          callables.add(
              new GA(
                  p,
                  mate,
                  mutate,
                  satimageLayers,
                  satimageNumClasses,
                  satimageIterations,
                  TRAIN_SATIMAGE,
                  VALIDATION_SATIMAGE,
                  TEST_SATIMAGE));
        }
      }

      // RHC
      callables.add(
          new RHC(
              phishingLayers,
              phishingNumClasses,
              phishingIterations,
              TRAIN_PHISHING,
              VALIDATION_PHISHING,
              TEST_PHISHING));
      callables.add(
          new RHC(
              satimageLayers,
              satimageNumClasses,
              satimageIterations,
              TRAIN_SATIMAGE,
              VALIDATION_SATIMAGE,
              TEST_SATIMAGE));

      for (double ce : ces) {
        callables.add(
            new SA(
                ce,
                phishingLayers,
                phishingNumClasses,
                phishingIterations,
                TRAIN_PHISHING,
                VALIDATION_PHISHING,
                TEST_PHISHING));
        callables.add(
            new SA(
                ce,
                satimageLayers,
                satimageNumClasses,
                satimageIterations,
                TRAIN_SATIMAGE,
                VALIDATION_SATIMAGE,
                TEST_SATIMAGE));
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
