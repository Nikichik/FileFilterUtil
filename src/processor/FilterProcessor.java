package processor;

import model.DataType;

import java.io.*;
import java.util.*;

public class FilterProcessor {
    private final List<String> inputFiles;
    private final String outputPath;
    private final String prefix;
    private final boolean append;
    private final StatMode statMode;

    private final Map<DataType, List<?>> dataMap = new EnumMap<>(DataType.class);

    public FilterProcessor(List<String> inputFiles, String outputPath, String prefix, boolean append, StatMode statMode) {
        this.inputFiles = inputFiles;
        this.outputPath = outputPath;
        this.prefix = prefix;
        this.append = append;
        this.statMode = statMode;

        dataMap.put(DataType.INTEGER, new ArrayList<Long>());
        dataMap.put(DataType.FLOAT, new ArrayList<Double>());
        dataMap.put(DataType.STRING, new ArrayList<String>());
    }

    public void process() {
        for (String file : inputFiles) {
            readFile(file);
        }

        for (DataType type : DataType.values()) {
            writeToFile(type, dataMap.get(type));
        }

        new StatCollector(dataMap).print(statMode);
    }

    private void readFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            System.err.println("Warning: File not found or not a file: " + filePath);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processLine(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + filePath + ": " + e.getMessage());
        }
    }

    private void processLine(String line) {
        if (line.isEmpty()) return;

        DataType type = TypeClassifier.classify(line);
        try {
            switch (type) {
                case INTEGER -> addToMap(DataType.INTEGER, Long.parseLong(line));
                case FLOAT -> addToMap(DataType.FLOAT, Double.parseDouble(line));
                case STRING -> addToMap(DataType.STRING, line);
            }
        } catch (Exception e) {
            System.err.println("Warning: Failed to parse line: \"" + line + "\". Stored as string.");
            addToMap(DataType.STRING, line);
        }
    }

    private <T> void addToMap(DataType type, T value) {
        List<T> list = (List<T>) dataMap.get(type);
        if (list != null) {
            list.add(value);
        }
    }

    private void writeToFile(DataType type, List<?> list) {
        if (list == null || list.isEmpty()) return;

        File dir = new File(outputPath);
        if (!dir.exists() && !dir.mkdirs()) {
            System.err.println("Failed to create directory: " + outputPath);
            return;
        }

        File outFile = new File(dir, prefix + type.getFileName());
        try (PrintWriter writer = new PrintWriter(new FileWriter(outFile, append))) {
            list.forEach(item -> writer.println(item.toString()));
        } catch (IOException e) {
            System.err.println("Error writing file: " + outFile.getAbsolutePath() + ": " + e.getMessage());
        }
    }
}
