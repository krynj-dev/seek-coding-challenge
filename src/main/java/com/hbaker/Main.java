package com.hbaker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java Main <file-path>");
            System.exit(1);
        }

        String filePath = args[0];

        List<String> lines;

        try {
            lines = readFileLines(filePath);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            lines = new ArrayList<>();
        }

        TrafficCounterSummary summary = new TrafficCounterSummary(lines);

        printSummaries(summary);
    }

    public static List<String> readFileLines(String path) throws IOException {
        return Files.readAllLines(Paths.get(path));
    }

    public static void printSummaries(TrafficCounterSummary summary) {
        printTotal(summary);
        printGrouped(summary, TimeGroup.DAY);
        printBusiestPeriods(summary, 3);
        printSmallest(summary, 3);
    }

    public static void printTotal(TrafficCounterSummary summary) {
        System.out.println("Total cars:\n\t" + summary.getTotal());
    }

    public static void printGrouped(TrafficCounterSummary summary, TimeGroup group) {
        System.out.println("Cars per day:\n\t" + String.join("\n\t",
                summary.getGrouped(group)));
    }

    public static void printBusiestPeriods(TrafficCounterSummary summary, Integer count) {
        System.out.println("Busiest " + count + " periods:\n\t"
                + String.join("\n\t", summary.getTopN(count)));
    }

    public static void printSmallest(TrafficCounterSummary summary, Integer count) {
        System.out.println("Sparsest " + count + " consecutive periods:\n\t"
                + String.join("\n\t", summary.getSmallestPeriod(count)));
    }
}