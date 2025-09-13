package dev.sygii.ultralib.data;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class CustomRegistry<T> {
    private Map<Identifier, T> registry = new HashMap<>();
}
