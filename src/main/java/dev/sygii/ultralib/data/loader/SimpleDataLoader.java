package dev.sygii.ultralib.data.loader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SimpleDataLoader implements SimpleSynchronousResourceReloadListener {
    private final Identifier loaderId;
    private final String directory;

    public SimpleDataLoader(Identifier loaderId, String directory) {
        this.loaderId = loaderId;
        this.directory = directory;
    }

    @Override
    public Identifier getFabricId() {
        return loaderId;
    }

    @Override
    public void reload(ResourceManager manager) {
        preReload();
        manager.findResources(directory, id -> id.getPath().endsWith(".json")).forEach((id, resourceRef) -> {
            try {
                InputStream stream = null;
                stream = resourceRef.getInputStream();
                JsonObject data = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject();
                String fileName = id.getPath().replace(directory + "/", "").replace(".json", "");
                reloadResource(data, id, fileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        postReload();
    }

    public void preReload() {}

    public void reloadResource(JsonObject data, Identifier id, String fileName) {}

    public void postReload() {}
}
