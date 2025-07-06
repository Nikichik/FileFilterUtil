package processor;

import model.DataType;

public class TypeClassifier {

    public static DataType classify(String line) {
        try {
            Long.parseLong(line);
            return DataType.INTEGER;
        } catch (NumberFormatException ignored) {
        }

        try {
            if (line.matches("[-+]?\\d*\\.?\\d+([eE][-+]?\\d+)?")) {
                Double.parseDouble(line);
                return DataType.FLOAT;
            }
        } catch (NumberFormatException ignored) {
        }

        return DataType.STRING;
    }
}
