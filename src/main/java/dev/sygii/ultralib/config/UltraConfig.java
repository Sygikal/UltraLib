package dev.sygii.ultralib.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.sygii.ultralib.UltraLib;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class UltraConfig<T> {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public final List<Path> directories = new ArrayList<>();
    public final String configDir;
    public final Path configFile;
    public final Class<T> instance;
    public final T instance2;
    public final String modid;

    private T config;

    public UltraConfig(String modId, Class<T> instance, T instance2) {
        this.modid = modId;
        this.configDir = FabricLoader.getInstance().getConfigDir() + File.separator + modId + File.separator;
        this.configFile = Path.of(configDir + instance.getName() + ".json");
        this.instance = instance;
        this.instance2 = instance2;
        directories.add(Path.of(configDir));
    }

    public void addDirectory(String dir) {
        this.directories.add(Path.of(configDir + dir + File.separator));
    }

    public T getConfig() {
        return config;
    }

    public void init() {
        try {
            directories.forEach(path -> {
                if (!Files.exists(path)) {
                    try {
                        Files.createDirectory(path);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            load();
            config = getConfig();
            onInit();

            //if (Files.exists(Path.of(fullPath))) {
            /*BufferedReader inputStream = Files.newBufferedReader(panoramaFile);
            JsonObject data = JsonParser.parseReader(inputStream).getAsJsonObject();
            System.out.println(data);*/
            //String fileName = id.getPath().replace(directory + "/", "").replace(".json", "");
            //reloadResource(data, id, fileName);
        } catch (Exception e) {
            UltraLib.LOGGER.error("Initializing config for {}, {}", modid, e);
        }
    }

    public void onInit() {

    }

    public void load() {
        if (!Files.exists(configFile)) {
            config = instance2;
            return;
        }
        try {
            String json = new String(Files.readAllBytes(Paths.get(configFile.toUri())));
            T data = GSON.fromJson(json, instance);
            if (data != null) {
                config = data;
            }
            save();
        } catch (IOException e) {
            UltraLib.LOGGER.error("Loading config for {}, {}", modid, e);
        }
    }

    public void save() {
        try {
            Files.write(configFile, GSON.toJson(config).getBytes());
        } catch (IOException e) {
            UltraLib.LOGGER.error("Saving config for {}, {}", modid, e);
        }
    }
}