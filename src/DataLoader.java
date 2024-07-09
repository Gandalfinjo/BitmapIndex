import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataLoader {
    public static List<FactTableRow> loadFactTable(String filePath) throws IOException {
        List<FactTableRow> factTable = new ArrayList<>();
        Map<String, String[]> dimensionTables = new HashMap<>();
        boolean isFactTable = false;
        boolean isDTable = false;
        Map<String, Integer> columnIndexes = new HashMap<>();

        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;

        if ((line = reader.readLine()) != null) {
            if (line.startsWith("#FactTable")) {
                isFactTable = true;
            }
        }

        if ((line = reader.readLine()) != null) {
            String[] header = line.split(",");

            for (int i = 0; i < header.length; i++) {
                columnIndexes.put(header[i].trim(), i);
            }
        }

        while ((line = reader.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                if (line.startsWith("#D")) {
                    isFactTable = false;
                    isDTable = true;
                    continue;
                }
                if (isFactTable) {
                    String[] values = line.split(",");
                    int id = Integer.parseInt(values[columnIndexes.get("ID")]);
                    Map<String, String> dimensions = new HashMap<>();
                    for (Map.Entry<String, Integer> entry : columnIndexes.entrySet()) {
                        String columnName = entry.getKey();
                        if (columnName.startsWith("D")) {
                            dimensions.put(columnName, values[entry.getValue()]);
                        }
                    }
                    Map<String, Integer> facts = new HashMap<>();
                    for (Map.Entry<String, Integer> entry : columnIndexes.entrySet()) {
                        String columnName = entry.getKey();
                        if (columnName.startsWith("Fact")) {
                            facts.put(columnName, Integer.parseInt(values[entry.getValue()]));
                        }
                    }
                    factTable.add(new FactTableRow(id, dimensions, facts));
                }
                else if (isDTable) {
                    String[] values = line.split(",");
                    dimensionTables.put(values[0], new String[]{values[1], values[2]});
                }
            }
        }

        for (FactTableRow row : factTable) {
            for (Map.Entry<String, String> dimension : row.getDimensions().entrySet()) {
                String dimName = dimension.getKey();
                String dimValue = dimension.getValue();
                if (dimensionTables.containsKey(dimValue)) {
                    row.setDCols(dimName, dimensionTables.get(dimValue));
                }
            }
        }

        reader.close();
        return factTable;
    }

    public static Map<String, String> loadSearchParameters(String filePath) throws IOException {
        Map<String, String> searchParams = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] keyValue = line.split(":");
            if (keyValue.length == 2) {
                searchParams.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }

        reader.close();
        return searchParams;
    }

    public static Map<String, String> parseAggregateFunctions(String aggregateFunctions) {
        Map<String, String> functions = new HashMap<>();
        String[] functionPairs = aggregateFunctions.split(",");

        for (String pair: functionPairs) {
            String[] parts = pair.split("=");
            if (parts.length == 2) {
                functions.put(parts[0].trim(), parts[1].trim());
            }
        }

        return functions;
    }
}
