import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BitmapIndex {
    private final Map<String, List<Boolean>> bitmaps;
    private List<FactTableRow> factTable;

    public BitmapIndex() {
        this.bitmaps = new HashMap<>();
        this.factTable = new ArrayList<>();
    }

    public Map<String, List<Boolean>> getBitmaps() {
        return bitmaps;
    }

    public void setFactTable(List<FactTableRow> factTable) {
        this.factTable = factTable;
    }
}
