import java.util.List;

public class Aggregation {
    public static int count(List<Integer> values) {
        return values.size();
    }

    public static int sum(List<Integer> values) {
        int sum = 0;
        for (int value : values) sum += value;
        return sum;
    }

    public static double avg(List<Integer> values) {
        if (values.isEmpty()) return 0;
        return (double) sum(values) / values.size();
    }

    public static int min(List<Integer> values) {
        if (values.isEmpty()) return Integer.MAX_VALUE;
        int min = values.getFirst();
        for (int value : values) if (value < min) min = value;
        return min;
    }

    public static int max(List<Integer> values) {
        if (values.isEmpty()) return Integer.MIN_VALUE;
        int max = values.getFirst();
        for (int value : values) if (value > max) max = value;
        return max;
    }
}
