package dev.sygii.ultralib.data.loader.list;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.sygii.ultralib.data.loader.resource.ReturnableResource;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SinglePreparationResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.apache.commons.compress.utils.Lists;

import java.io.IOException;
import java.util.List;

public abstract class ListDetachedRunnableDataLoader<T> extends SinglePreparationResourceReloader<List<T>> implements IdentifiableResourceReloadListener {
    private final Identifier loaderId;
    private final String directory;
    private final List<T> loaderList = Lists.newArrayList();

    public ListDetachedRunnableDataLoader(Identifier loaderId, String directory) {
        this.loaderId = loaderId;
        this.directory = directory;
    }

    @Override
    public Identifier getFabricId() {
        return loaderId;
    }

    public void runResources(ResourceManager manager, Profiler profiler, ReturnableResource<T> runnable) {
        manager.findResources(directory, id -> id.getPath().endsWith(".json")).forEach((id, resourceRef) -> {
            try {
                JsonObject data = JsonParser.parseReader(resourceRef.getReader()).getAsJsonObject();
                String fileName = id.getPath().replace(directory + "/", "").replace(".json", "");
                Identifier resourceId = Identifier.of(id.getNamespace(), fileName);
                loaderList.add(runnable.run(data, resourceId, fileName));
                resourceRef.getReader().close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    protected List<T> prepare(ResourceManager manager, Profiler profiler) {
        loaderList.clear();
        runResources(manager, profiler, this::reloadResource);
        return loaderList;
    }

    @Override
    protected abstract void apply(List<T> prepared, ResourceManager manager, Profiler profiler);

    public abstract T reloadResource(JsonObject data, Identifier id, String fileName);
}
