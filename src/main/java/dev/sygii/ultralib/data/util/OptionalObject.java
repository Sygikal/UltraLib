package dev.sygii.ultralib.data.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class OptionalObject {

    public static JsonElement get(JsonObject object, String key, Object def) {
        JsonObject defaultObject = new JsonObject();
        if (object.has(key)) {
            defaultObject.add(key, object.get(key));
        }else {
            defaultObject.add(key, new Gson().toJsonTree(def));
        }
        return defaultObject.get(key);
    }
}
