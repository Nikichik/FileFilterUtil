package processor;

import model.DataType;

import java.util.List;
import java.util.Map;

public class StatCollector {
    private final Map<DataType, List<?>> dataMap;

    public StatCollector(Map<DataType, List<?>> dataMap) {
        this.dataMap = dataMap;
    }

    public void print(StatMode mode) {
        switch (mode) {
            case SHORT -> printShort();
            case FULL -> printFull();
            case NONE -> {
            }
        }
    }

    private void printShort() {
        dataMap.forEach((type, list) ->
                System.out.println(type + ": count = " + (list == null ? 0 : list.size()))
        );
    }

    private void printFull() {
        printIntegers();
        printFloats();
        printStrings();
    }

    private void printIntegers() {
        List<Long> list = (List<Long>) dataMap.get(DataType.INTEGER);
        if (list == null || list.isEmpty()) return;

        long sum = list.stream().mapToLong(Long::longValue).sum();
        double avg = list.stream().mapToLong(Long::longValue).average().orElse(0);
        System.out.printf("Integers: count=%d, min=%d, max=%d, sum=%d, avg=%.2f%n",
                list.size(),
                list.stream().min(Long::compareTo).orElse(0L),
                list.stream().max(Long::compareTo).orElse(0L),
                sum, avg);
    }

    private void printFloats() {
        List<Double> list = (List<Double>) dataMap.get(DataType.FLOAT);
        if (list == null || list.isEmpty()) return;

        double sum = list.stream().mapToDouble(Double::doubleValue).sum();
        double avg = list.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        System.out.printf("Floats: count=%d, min=%.2f, max=%.2f, sum=%.2f, avg=%.2f%n",
                list.size(),
                list.stream().min(Double::compareTo).orElse(0.0),
                list.stream().max(Double::compareTo).orElse(0.0),
                sum, avg);
    }

    private void printStrings() {
        List<String> list = (List<String>) dataMap.get(DataType.STRING);
        if (list == null || list.isEmpty()) return;

        int min = list.stream().mapToInt(String::length).min().orElse(0);
        int max = list.stream().mapToInt(String::length).max().orElse(0);
        System.out.printf("Strings: count=%d, shortest=%d, longest=%d%n", list.size(), min, max);
    }
}
