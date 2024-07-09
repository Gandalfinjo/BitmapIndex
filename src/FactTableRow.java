import java.util.HashMap;
import java.util.Map;

public class FactTableRow {
    private final int id;
    private final Map<String, String> dimensions;
    private final Map<String, Integer> facts;
    private final Map<String, String[]> dCols;

    public FactTableRow(int id, Map<String, String> dimensions, Map<String, Integer> facts) {
        this.id = id;
        this.dimensions = dimensions;
        this.facts = facts;
        this.dCols = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public Map<String, String> getDimensions() {
        return dimensions;
    }

    public Map<String, Integer> getFacts() {
        return facts;
    }

    public Map<String, String[]> getDCols() {
        return dCols;
    }

    public void setDCols(String dimName, String[] cols) {
        dCols.put(dimName, cols);
    }
}
