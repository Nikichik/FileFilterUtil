import processor.FilterProcessor;
import processor.StatMode;

import java.util.ArrayList;
import java.util.List;

public class FileFilterUtil {
    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        List<String> inputFiles = new ArrayList<>();
        String outputPath = ".";
        String prefix = "";
        boolean append = false;
        StatMode statMode = StatMode.NONE;

        try {
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "-o" -> {
                        if (i + 1 >= args.length) throw new IllegalArgumentException("Missing path after -o");
                        outputPath = args[++i];
                    }
                    case "-p" -> {
                        if (i + 1 >= args.length) throw new IllegalArgumentException("Missing prefix after -p");
                        prefix = args[++i];
                    }
                    case "-a" -> append = true;
                    case "-s" -> statMode = StatMode.SHORT;
                    case "-f" -> statMode = StatMode.FULL;
                    default -> {
                        if (args[i].startsWith("-")) {
                            System.err.println("Unknown option: " + args[i]);
                            printUsage();
                            return;
                        } else {
                            inputFiles.add(args[i]);
                        }
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            printUsage();
            return;
        }

        if (inputFiles.isEmpty()) {
            System.err.println("Error: No input files specified.");
            printUsage();
            return;
        }

        new FilterProcessor(inputFiles, outputPath, prefix, append, statMode).process();
    }

    private static void printUsage() {
        System.out.println("""
                Usage: java FileFilterUtil [-s|-f] [-a] [-o path] [-p prefix] file1 file2 ...
                Options:
                  -o <path>      Output directory (default: current)
                  -p <prefix>    Prefix for output filenames
                  -a             Append mode
                  -s             Short statistics
                  -f             Full statistics
                """);
    }
}
