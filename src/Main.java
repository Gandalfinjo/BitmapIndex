import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        List<FactTableRow> factTable = DataLoader.loadFactTable("resources/factTable2.csv");
        Map<String, String> searchParams = DataLoader.loadSearchParameters("resources/search2.txt");

        BitmapIndexSystem indexSystem = new BitmapIndexSystem();
        String[] indexes = searchParams.get("Indexes").split(",");
        for (String index: indexes) {
            if (!index.isEmpty()) {
                indexSystem.createBitmapIndex(index, factTable);
            }
        }

        BitmapSearch bitmapSearch = new BitmapSearch(indexSystem);

        String searchCondition = searchParams.get("SearchConditions");
        String[] conditionParts = searchCondition.split(" ");
        Map<String, String> conditions = new HashMap<>();
        String operation = "AND";

        for (String part: conditionParts) {
            if (part.equals("AND") || part.equals("OR")) {
                operation = part;
            }
            else {
                String[] keyValue = part.split("=");
                conditions.put(keyValue[0], keyValue[1]);
            }
        }

        List<Integer> resultIndexes = bitmapSearch.searchWithBitmap(conditions, operation);

        List<FactTableRow> results = new ArrayList<>();
        for (int index: resultIndexes) {
            results.add(factTable.get(index));
        }

        System.out.println("Search with index:\n");
        if (!results.isEmpty()) {
            FactTableRow firstRow = results.getFirst();
            Map<String, String[]> dCols = firstRow.getDCols();
            Map<String, String> aggregateFunctions = DataLoader.parseAggregateFunctions(searchParams.get("AggregateFunctions"));

            for (Map.Entry<String, String[]> entry : dCols.entrySet()) {
                String dimName = entry.getKey();
                String[] cols = entry.getValue();
                System.out.println("Unindexed columns " + dimName + ": " + Arrays.toString(cols));
            }

            Set<String> factColumns = firstRow.getFacts().keySet();
            for (String factColumn : factColumns) {
                List<Integer> factValues = new ArrayList<>();

                for (FactTableRow row : results) {
                    factValues.add(row.getFacts().get(factColumn));
                }

                System.out.println("Aggregation results for " + factColumn + ":");
                String aggregateFunction = aggregateFunctions.getOrDefault(factColumn, "none");

                switch (aggregateFunction) {
                    case "count":
                        System.out.println("Count: " + Aggregation.count(factValues));
                        break;
                    case "sum":
                        System.out.println("Sum: " + Aggregation.sum(factValues));
                        break;
                    case "avg":
                        System.out.println("Avg: " + Aggregation.avg(factValues));
                        break;
                    case "min":
                        System.out.println("Min: " + Aggregation.min(factValues));
                        break;
                    case "max":
                        System.out.println("Max: " + Aggregation.max(factValues));
                        break;
                    default:
                        System.out.println("No aggregate function specified.");
                }
            }
        }

        System.out.println("\nMatching rows:");
        for (FactTableRow row: results) {
            System.out.println(row.getId() + ", " + row.getDimensions() + ", " + row.getFacts());
        }

        System.out.println("\nSearch without index:\n");
        List<FactTableRow> nonIndexedResults = SearchWithoutIndex.search(factTable, conditions, operation);
        for (FactTableRow row : nonIndexedResults) {
            System.out.println(row.getId() + ", " + row.getDimensions() + ", " + row.getFacts());
        }
    }
}