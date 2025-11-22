package dev.sygii.ultralib;

import net.fabricmc.api.ModInitializer;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UltraLib implements ModInitializer {
	public static final String MOD_ID = "ultralib";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Map<String, String> PATH_TO_ID = new HashMap<>();

	static Identifier internalId(String string) {
		return Identifier.of(MOD_ID, string);
	}

	public static void registerPackage(Class<?> clazz, String id) {
		PATH_TO_ID.put(clazz.getPackageName(), id);
	}

	@Override
	public void onInitialize() {}

	public static Identifier texture(String name) {
		return id("textures/" + name + ".png");
	}

	public static Identifier id(String name) {
		String pkg = getCallerPackage();
		if (pkg != null) {
			for (String s : PATH_TO_ID.keySet()) {
				if (pkg.startsWith(s)) {
					return Identifier.of(PATH_TO_ID.get(s), name);
				}
			}
		}
		return Identifier.of(MOD_ID, name);
	}

	public static Text getGuiText(String path, Object... args) {
		return getArgText("text", new String[] {"gui", path}, args);
	}

	public static Text getText(String title, String... paths) {
		return Text.translatable(title + "." + getCallerId() + "." + String.join(".", paths));
	}

	public static Text getArgText(String title, String[] paths, Object... args) {
		return Text.translatable(title + "." + getCallerId() + "." + String.join(".", paths), args);
	}

	private static String getCallerId() {
		String pkg = getCallerPackage();
		if (pkg != null) {
			for (String s : PATH_TO_ID.keySet()) {
				if (pkg.startsWith(s)) {
					return PATH_TO_ID.get(s);
				}
			}
		}
		return MOD_ID;
	}

	public static Logger log() {
		String pkg = getCallerPackage();
		if (pkg != null) {
			for (String s : PATH_TO_ID.keySet()) {
				if (pkg.startsWith(s)) {
					return LoggerFactory.getLogger(PATH_TO_ID.get(s));
				}
			}
		}
		return LOGGER;
	}

	//UTILITY

	public static String getCallerPackage() {
		StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
		for (int i = 1; i < stElements.length; i++) {
			StackTraceElement ste = stElements[i];
			String className = ste.getClassName();
			if (!className.equals(UltraLib.class.getName()) && className.indexOf("java.lang.Thread") != 0) {
				int dot = className.lastIndexOf('.');
				return (dot != -1) ? className.substring(0, dot).intern() : "";
			}
		}
		return null;
	}

	public static String getCallerClassName() {
		StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
		for (int i=1; i<stElements.length; i++) {
			StackTraceElement ste = stElements[i];
			if (!ste.getClassName().equals(UltraLib.class.getName()) && ste.getClassName().indexOf("java.lang.Thread")!=0) {
				return ste.getClassName();
			}
		}
		return null;
	}

	public static String getCallerCallerClassName() {
		StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
		String callerClassName = null;
		for (int i=1; i<stElements.length; i++) {
			StackTraceElement ste = stElements[i];
			if (!ste.getClassName().equals(UltraLib.class.getName())&& ste.getClassName().indexOf("java.lang.Thread")!=0) {
				if (callerClassName==null) {
					callerClassName = ste.getClassName();
				} else if (!callerClassName.equals(ste.getClassName())) {
					return ste.getClassName();
				}
			}
		}
		return null;
	}
}
