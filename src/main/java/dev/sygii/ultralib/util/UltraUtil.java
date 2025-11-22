package dev.sygii.ultralib.util;

import java.util.ArrayList;
import java.util.Map;

public class UltraUtil {

    public static <K, V> void addToMapArray(Map<K, ArrayList<V>> receiver, K key, V value) {
        //receiver.computeIfAbsent(key, k -> new ArrayList<>()).add(value);

        receiver.computeIfAbsent(key, k -> new ArrayList<>());
        receiver.get(key).add(value);
    }

}
