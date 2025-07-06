import java.io.*;
import java.util.*;

public class FilterProcessor {
    private final List<String> files;
    private final String path;
    private final String prefix;
    private final boolean append;
    private final boolean shortStats;
    private final boolean fullStats;

    // Для записи
    private final List<Long> integers = new ArrayList<>();
    private final List<Double> floats = new ArrayList<>();
    private final List<String> strings = new ArrayList<>();

    public FilterProcessor(List<String> files, String path, String prefix, boolean append, boolean shortStats, boolean fullStats) {
        this.files = files;
        this.path = path;
        this.prefix = prefix;
        this.append = append;
        this.shortStats = shortStats;
        this.fullStats = fullStats;
    }

    public void process() {
        for (String file : files) {
            File inputFile = new File(file);
            if (!inputFile.exists() || !inputFile.isFile()) {
                System.out.println("Warning: File not found or not a regular file: " + file);
                continue; // продолжаем с остальными
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) continue;
                    categorize(line);
                }
            } catch (IOException e) {
                System.out.println("Error reading file: " + file + " - " + e.getMessage());
            }
        }

        writeOutput("integers.txt", integers, String::valueOf);
        writeOutput("floats.txt", floats, String::valueOf);
        writeOutput("strings.txt", strings, s -> s);

        printStats();
    }


    private void categorize(String line) {
        try {
            if (line.matches("-?\\d+")) {
                integers.add(Long.parseLong(line));
            } else if (line.matches("-?\\d*\\.\\d+([eE][-+]?\\d+)?")) {
                floats.add(Double.parseDouble(line));
            } else {
                strings.add(line);
            }
        } catch (Exception e) {
            System.out.println("Failed to parse line: \"" + line + "\". Treated as string.");
            strings.add(line);
        }
    }


    private <T> void writeOutput(String baseName, List<T> data, Formatter<T> formatter) {
        if (data.isEmpty()) return;

        File dir = new File(path);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                System.out.println("Failed to create output directory: " + path);
                return;
            }
        }

        File file = new File(dir, prefix + baseName);
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, append))) {
            for (T item : data) {
                writer.println(formatter.format(item));
            }
        } catch (IOException e) {
            System.out.println("Error writing to file " + file.getAbsolutePath() + ": " + e.getMessage());
        }
    }

    private void printStats() {
        if (shortStats) {
            System.out.println("Integers: " + integers.size());
            System.out.println("Floats: " + floats.size());
            System.out.println("Strings: " + strings.size());
        } else if (fullStats) {
            System.out.println("Integers: count=" + integers.size()
                    + ", min=" + integers.stream().min(Long::compareTo).orElse(0L)
                    + ", max=" + integers.stream().max(Long::compareTo).orElse(0L)
                    + ", sum=" + integers.stream().mapToLong(Long::longValue).sum()
                    + ", avg=" + integers.stream().mapToLong(Long::longValue).average().orElse(0));

            System.out.println("Floats: count=" + floats.size()
                    + ", min=" + floats.stream().min(Double::compareTo).orElse(0.0)
                    + ", max=" + floats.stream().max(Double::compareTo).orElse(0.0)
                    + ", sum=" + floats.stream().mapToDouble(Double::doubleValue).sum()
                    + ", avg=" + floats.stream().mapToDouble(Double::doubleValue).average().orElse(0));

            System.out.println("Strings: count=" + strings.size()
                    + ", shortest=" + strings.stream().mapToInt(String::length).min().orElse(0)
                    + ", longest=" + strings.stream().mapToInt(String::length).max().orElse(0));
        }
    }

    interface Formatter<T> {
        String format(T t);
    }
}
