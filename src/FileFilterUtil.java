import java.io.*;
import java.util.*;

public class FileFilterUtil {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java FilterUtil [-s|-f] [-a] [-o path] [-p prefix] file1 file2 ...");
            return;
        }

        List<String> inputFiles = new ArrayList<>();
        String outputPath = ".";
        String prefix = "";
        boolean append = false;
        boolean shortStats = false;
        boolean fullStats = false;

        // Разбор аргументов
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-o":
                    outputPath = args[++i];
                    break;
                case "-p":
                    prefix = args[++i];
                    break;
                case "-a":
                    append = true;
                    break;
                case "-s":
                    shortStats = true;
                    break;
                case "-f":
                    fullStats = true;
                    break;
                default:
                    if (args[i].startsWith("-")) {
                        System.out.println("Unknown option: " + args[i]);
                        return;
                    } else {
                        inputFiles.add(args[i]);
                    }
            }
        }

        if (shortStats && fullStats) {
            System.out.println("Please choose only one of -s or -f");
            return;
        }

        // Обработка файлов
        new FilterProcessor(inputFiles, outputPath, prefix, append, shortStats, fullStats).process();
    }
}
