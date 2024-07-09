import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BitmapSearch {
    private final BitmapIndexSystem indexSystem;

    public BitmapSearch(BitmapIndexSystem indexSystem) {
        this.indexSystem = indexSystem;
    }

    public List<Integer> searchWithBitmap(Map<String, String> conditions, String operation) {
        List<Integer> result = new ArrayList<>();
        if (conditions.isEmpty()) return result;

        String firstColumn = conditions.keySet().iterator().next();
        String firstValue = conditions.get(firstColumn);
        List<Boolean> bitmap = indexSystem.indexes.get(firstColumn).getBitmaps().get(firstValue);

        List<Boolean> resultBitmap = new ArrayList<>(bitmap);

        for (Map.Entry<String, String> entry: conditions.entrySet()) {
            if (entry.getKey().equals(firstColumn)) continue;

            String column = entry.getKey();
            String value = entry.getValue();
            List<Boolean> currentBitmap = indexSystem.indexes.get(column).getBitmaps().get(value);

            for (int i = 0; i < resultBitmap.size(); i++) {
                if (operation.equals("AND")) {
                    resultBitmap.set(i, resultBitmap.get(i) && currentBitmap.get(i));
                }
                else if (operation.equals("OR")) {
                    resultBitmap.set(i, resultBitmap.get(i) || currentBitmap.get(i));
                }
            }
        }

        for (int i = 0; i < resultBitmap.size(); i++) {
            if (resultBitmap.get(i)) result.add(i);
        }

        return result;
    }
}
