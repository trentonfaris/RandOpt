package com.trentfaris.randopt.solver;

import com.trentfaris.randopt.RandOpt;
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
import java.util.Stack;
import java.util.concurrent.Callable;

public abstract class Solver implements Callable<Object> {
    private final String output;
    private final String name;
    private final int iterations;

    protected Trainer trainer;

    public Solver(String output, String name, int iterations) {
        this.output = output;
        this.name = name;
        this.iterations = iterations;

        Stack<String> directories = new Stack<>();

        File file = new File(RandOpt.OUTPUT + File.separator + output);
        while (!file.exists()) {
            directories.push(file.getName());

            String parent = file.getParent();
            if (parent == null) {
                break;
            }

            file = new File(parent);
        }

        while (!directories.isEmpty()) {
            file = new File(file.getPath() + File.separator + directories.pop());
            if (!file.mkdir()) {
                System.out.println("Solver output directory " + file.getPath() + " was not created.");
                break;
            }
        }
    }

    protected abstract void writeMetrics(int iteration, long time, CSVPrinter csvPrinter) throws IOException;

    @Override
    public final Object call() throws IOException {
        if (trainer == null) {
            throw new IllegalStateException("Cannot train with a null trainer.");
        }

        List<Long> times = new ArrayList<>();
        times.add(0L);

        BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(
                        RandOpt.OUTPUT
                                + File.separator
                                + output
                                + File.separator
                                + name
                                + "_"
                                + new Date().getTime()
                                + ".csv"
                )
        );

        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);

        for (int i = 0; i < iterations; i++) {
            long start = System.currentTimeMillis();

            trainer.train();

            times.add(times.get(times.size() - 1) + (System.currentTimeMillis() - start));

            if (i % 100 == 0) {
                System.out.println("Output: " + output + ", Iteration: " + i);
            } else if (i % 10 == 0) {
                writeMetrics(i, times.get(times.size() - 1), csvPrinter);
            }
        }

        csvPrinter.close();
        writer.close();

        return null;
    }
}
