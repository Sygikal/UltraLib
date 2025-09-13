package dev.sygii.ultralib.data.loader.resource;

import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;

public interface RunnableResource {
    void run(JsonObject data, Identifier id, String fileName);
}
