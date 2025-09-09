package dev.sygii.ultralib.data.util;

import java.util.List;

public class SortedList {

    public static <T> T sortByWeight(List<T> list, WeightRunner<T> runner) {
        // Compute the total weight of all items together.
        // This can be skipped of course if sum is already 1.
        if (list.isEmpty()) {
            return null;
        }

        double totalWeight = 0.0;
        for (T i : list) {
            totalWeight += runner.run(i);
        }

        int idx = 0;
        for (double r = Math.random() * totalWeight; idx < list.size() - 1; ++idx) {
            r -= runner.run(list.get(idx));
            if (r <= 0.0) break;
        }
        return list.get(idx);
    }

    public interface WeightRunner<T> {
        int run(T instance);
    }
}
