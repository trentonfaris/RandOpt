package com.trentfaris.randopt;

import com.trentfaris.randopt.solver.optimizer.optimizers.Backprop;
import com.trentfaris.randopt.solver.optimizer.optimizers.GA;
import com.trentfaris.randopt.solver.optimizer.optimizers.RHC;
import com.trentfaris.randopt.solver.optimizer.optimizers.SA;
import com.trentfaris.randopt.solver.toyproblem.toyproblems.continuouspeaks.ContinuousPeaksGA;
import com.trentfaris.randopt.solver.toyproblem.toyproblems.continuouspeaks.ContinuousPeaksMimic;
import com.trentfaris.randopt.solver.toyproblem.toyproblems.continuouspeaks.ContinuousPeaksRHC;
import com.trentfaris.randopt.solver.toyproblem.toyproblems.continuouspeaks.ContinuousPeaksSA;
import com.trentfaris.randopt.solver.toyproblem.toyproblems.flipflop.FlipFlopGA;
import com.trentfaris.randopt.solver.toyproblem.toyproblems.flipflop.FlipFlopMimic;
import com.trentfaris.randopt.solver.toyproblem.toyproblems.flipflop.FlipFlopRHC;
import com.trentfaris.randopt.solver.toyproblem.toyproblems.flipflop.FlipFlopSA;
import com.trentfaris.randopt.solver.toyproblem.toyproblems.tsp.TravelingSalesmanGA;
import com.trentfaris.randopt.solver.toyproblem.toyproblems.tsp.TravelingSalesmanMimic;
import com.trentfaris.randopt.solver.toyproblem.toyproblems.tsp.TravelingSalesmanRHC;
import com.trentfaris.randopt.solver.toyproblem.toyproblems.tsp.TravelingSalesmanSA;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RandOpt {
    public static final String OUTPUT = "output";

    private static final String TRAIN = "data/phishing_train.csv";
    private static final String VALIDATION = "data/phishing_validation.csv";
    private static final String TEST = "data/phishing_test.csv";

    private static final String NAME = "phishing";
    private static final int ITERATIONS = 3000;
    private static final int NUM_CLASSES = 2;
    private static final int[] LAYERS = new int[]{60, 60, 1};
    private static final int OPTIMIZER_POP = 50;
    private static final int[] OPTIMIZER_MATES = new int[]{20, 10};
    private static final int[] OPTIMIZER_MUTATES = new int[]{20, 10};
    private static final double[] OPTIMIZER_CES = new double[]{0.15, 0.35, 0.55, 0.7, 0.95};
    private static final int NUM_TRIALS = 5;
    private static final int TOY_POP = 100;
    private static final int[] TOY_MATES = new int[]{50, 30, 10};
    private static final int[] TOY_MUTATES = new int[]{50, 30, 10};
    private static final double[] TOY_MS = new double[]{0.1, 0.3, 0.5, 0.7, 0.9};
    private static final int TOY_SAMPLES = 100;
    private static final int TOY_TOKEEP = 50;
    private static final double[] TOY_CES = new double[]{0.15, 0.35, 0.55, 0.75, 0.95};

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
            // Optimizers
            // Backprop
            callables.add(
                    new Backprop(
                            NAME,
                            ITERATIONS,
                            NUM_CLASSES,
                            TRAIN,
                            VALIDATION,
                            TEST,
                            LAYERS));

            // GA
            for (int mate : OPTIMIZER_MATES) {
                for (int mutate : OPTIMIZER_MUTATES) {
                    callables.add(
                            new GA(
                                    NAME + "_" + OPTIMIZER_POP + "_" + mate + "_" + mutate,
                                    ITERATIONS,
                                    NUM_CLASSES,
                                    TRAIN,
                                    VALIDATION,
                                    TEST,
                                    LAYERS,
                                    OPTIMIZER_POP,
                                    mate,
                                    mutate));
                }
            }

            // RHC
            callables.add(
                    new RHC(
                            NAME,
                            ITERATIONS,
                            NUM_CLASSES,
                            TRAIN,
                            VALIDATION,
                            TEST,
                            LAYERS));

            // SA
            for (double ce : OPTIMIZER_CES) {
                callables.add(
                        new SA(
                                NAME + "_" + ce,
                                ITERATIONS,
                                NUM_CLASSES,
                                TRAIN,
                                VALIDATION,
                                TEST,
                                LAYERS,
                                ce));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Toy Problems
        for (int i = 0; i < NUM_TRIALS; i++) {
            // GA
            for (int mate : TOY_MATES) {
                for (int mutate : TOY_MUTATES) {
                    callables.add(new ContinuousPeaksGA(NAME + "_Trial_" + i + "_" + TOY_POP + "_" + mate + "_" + mutate, ITERATIONS, TOY_POP, mate, mutate));
                    callables.add(new FlipFlopGA(NAME + "_Trial_" + i + "_" + TOY_POP + "_" + mate + "_" + mutate, ITERATIONS, TOY_POP, mate, mutate));
                    callables.add(new TravelingSalesmanGA(NAME + "_Trial_" + i + "_" + TOY_POP + "_" + mate + "_" + mutate, ITERATIONS, TOY_POP, mate, mutate));
                }
            }

            // MIMIC
            for (double m : TOY_MS) {
                callables.add(new ContinuousPeaksMimic(NAME + "_Trial_" + i + "_" + m + "_" + TOY_SAMPLES + "_" + TOY_TOKEEP, ITERATIONS, m, TOY_SAMPLES, TOY_TOKEEP));
                callables.add(new FlipFlopMimic(NAME + "_Trial_" + i + "_" + m + "_" + TOY_SAMPLES + "_" + TOY_TOKEEP, ITERATIONS, m, TOY_SAMPLES, TOY_TOKEEP));
                callables.add(new TravelingSalesmanMimic(NAME + "_Trial_" + i + "_" + m + "_" + TOY_SAMPLES + "_" + TOY_TOKEEP, ITERATIONS, m, TOY_SAMPLES, TOY_TOKEEP));
            }

            // RHC
            callables.add(new ContinuousPeaksRHC(NAME + "_Trial_" + i, ITERATIONS));
            callables.add(new FlipFlopRHC(NAME + "_Trial_" + i, ITERATIONS));
            callables.add(new TravelingSalesmanRHC(NAME + "_Trial_" + i, ITERATIONS));

            // SA
            for (double ce : TOY_CES) {
                callables.add(new ContinuousPeaksSA(NAME + "_Trial_" + i + "_" + ce, ITERATIONS, ce));
                callables.add(new FlipFlopSA(NAME + "_Trial_" + i + "_" + ce, ITERATIONS, ce));
                callables.add(new TravelingSalesmanSA(NAME + "_Trial_" + i + "_" + ce, ITERATIONS, ce));
            }
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
