package com.trentfaris.randopt.toy;

import com.trentfaris.randopt.RandOpt;
import dist.Distribution;
import opt.EvaluationFunction;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
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

public abstract class Toy implements Callable<Object> {
    private static final int STRIDE = 10;

    private final String output;
    private final String name;
    private final int id;
    private final int iterations;

    protected EvaluationFunction evaluationFunction;
    protected Distribution distribution;
    protected Trainer trainer;

    public Toy(String output, String name, int id, int iterations) {
        this.output = output;
        this.name = name;
        this.id = id;
        this.iterations = iterations;

        File file = new File(RandOpt.OUTPUT + File.separator + output);
        if (!file.exists()) {
            if (!file.mkdir()) {
                System.out.println(
                        "Toy output directory " + output + " already exists. Skipping creation.");
            }
        }
    }

    @Override
    public Object call() throws IOException {
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
                        writer, CSVFormat.DEFAULT.withHeader("Iteration", "Score", "Time", "F Evals"));

        for (int i = 0; i < iterations; i += STRIDE) {
            long start = System.currentTimeMillis();

            trainer.train();

            times.add(times.get(times.size() - 1) + (System.currentTimeMillis() - start));

        }

        return null;
    }
}
