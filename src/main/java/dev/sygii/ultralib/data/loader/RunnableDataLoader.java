package dev.sygii.ultralib.data.loader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SinglePreparationResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.io.IOException;

public abstract class RunnableDataLoader<T> extends SinglePreparationResourceReloader<T> implements IdentifiableResourceReloadListener {
    private final Identifier loaderId;
    private final String directory;

    public RunnableDataLoader(Identifier loaderId, String directory) {
        this.loaderId = loaderId;
        this.directory = directory;
    }

    public void runResources(ResourceManager manager, Profiler profiler, RunnableResource runnable) {
        manager.findResources(directory, id -> id.getPath().endsWith(".json")).forEach((id, resourceRef) -> {
            try {
                JsonObject data = JsonParser.parseReader(resourceRef.getReader()).getAsJsonObject();
                String fileName = id.getPath().replace(directory + "/", "").replace(".json", "");
                Identifier resourceId = Identifier.of(id.getNamespace(), fileName);
                runnable.run(data, resourceId, fileName);
                resourceRef.getReader().close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    protected abstract T prepare(ResourceManager manager, Profiler profiler);

    @Override
    protected abstract void apply(T prepared, ResourceManager manager, Profiler profiler);

    @Override
    public Identifier getFabricId() {
        return loaderId;
    }
}
