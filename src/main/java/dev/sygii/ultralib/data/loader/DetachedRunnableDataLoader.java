package dev.sygii.ultralib.data.loader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.sygii.ultralib.data.loader.resource.RunnableResource;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SinglePreparationResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.io.IOException;

public abstract class DetachedRunnableDataLoader<T> extends SinglePreparationResourceReloader<T> implements IdentifiableResourceReloadListener {
    private final Identifier loaderId;
    private final String directory;

    public DetachedRunnableDataLoader(Identifier loaderId, String directory) {
        this.loaderId = loaderId;
        this.directory = directory;
    }

    @Override
    public Identifier getFabricId() {
        return loaderId;
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
    protected T prepare(ResourceManager manager, Profiler profiler) {
        prePrepare();
        runResources(manager, profiler, this::reloadResource);
        return postPrepare();
    }

    @Override
    protected abstract void apply(T prepared, ResourceManager manager, Profiler profiler);

    public void prePrepare() {}

    public void reloadResource(JsonObject data, Identifier id, String fileName) {}

    public abstract T postPrepare();
}
