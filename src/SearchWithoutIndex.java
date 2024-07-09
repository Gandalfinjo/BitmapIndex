import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchWithoutIndex {
    public static List<FactTableRow> search(List<FactTableRow> factTable, Map<String, String> conditions, String operation) {
        List<FactTableRow> result = new ArrayList<>();

        for (FactTableRow row : factTable) {
            boolean matches = false;
            for (Map.Entry<String, String> condition : conditions.entrySet()) {
                if (operation.equals("AND")) {
                    matches = row.getDimensions().get(condition.getKey()).equals(condition.getValue());
                    if (!matches) break;
                } else if (operation.equals("OR")) {
                    matches = row.getDimensions().get(condition.getKey()).equals(condition.getValue());
                    if (matches) break;
                }
            }
            if (matches) result.add(row);
        }

        return result;
    }
}
