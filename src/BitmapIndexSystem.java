import java.util.*;

public class BitmapIndexSystem {
    Map<String, BitmapIndex> indexes = new HashMap<>();

    public void createBitmapIndex(String column, List<FactTableRow> factTable) {
        BitmapIndex bitmapIndex = new BitmapIndex();
        bitmapIndex.setFactTable(factTable);

        Set<String> distinctValues = new HashSet<>();
        for (FactTableRow row: factTable) {
            distinctValues.add(row.getDimensions().get(column));
        }

        for (String value: distinctValues) {
            List<Boolean> bitmap = new ArrayList<>(Collections.nCopies(factTable.size(), false));
            bitmapIndex.getBitmaps().put(value, bitmap);
        }

        for (int i = 0; i < factTable.size(); i++) {
            FactTableRow row = factTable.get(i);
            String value = row.getDimensions().get(column);
            bitmapIndex.getBitmaps().get(value).set(i, true);
        }

        indexes.put(column, bitmapIndex);
    }
}
