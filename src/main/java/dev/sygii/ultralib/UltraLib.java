package dev.sygii.ultralib;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class UltraLib implements ModInitializer {
	public static final String MOD_ID = "ultralib";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);



	@Override
	public void onInitialize() {
	}

	public static Identifier id(String string) {
		return Identifier.of(MOD_ID, string);
	}

	public static String getBaseName(String filename) {
		if (filename == null)
			return null;

		String name = new File(filename).getName();
		int extPos = name.lastIndexOf('.');

		if (extPos < 0)
			return name;

		return name.substring(0, extPos);
	}

	public static <K, V> void addToMapArray(Map<K, ArrayList<V>> receiver, K key, V value) {
        receiver.computeIfAbsent(key, k -> new ArrayList<>());
		receiver.get(key).add(value);
	}
}
