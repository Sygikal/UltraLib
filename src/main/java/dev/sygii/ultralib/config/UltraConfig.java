package dev.sygii.ultralib.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UltraConfig<T> {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public final String configDir;
    public final String[] dirs;
    public final Path configFile;
    public final Class<T> instance;
    public final T instance2;

    public UltraConfig(String modId, String[] dirs, Class<T> instance, T instance2) {
        this.configDir = FabricLoader.getInstance().getConfigDir() + File.separator + modId + File.separator;
        this.dirs = dirs;
        this.configFile = Path.of(configDir + instance.getName() + ".json");
        this.instance = instance;
        this.instance2 = instance2;
    }

    private T config;

    public T getConfig() {
        return config;
    }

    public void init() {
        try {
            for (String dir : dirs) {
                Path path = Path.of(configDir + dir + File.separator);
                if (!Files.exists(path)) {
                    Files.createDirectory(path);
                }
            }
            /*if (!Files.exists(panoramaFile)) {
                Files.write(panoramaFile, GSON.toJson(new Panorama()).getBytes());
            }*/
            load();
            config = getConfig();
            onInit();

            //if (Files.exists(Path.of(fullPath))) {
            /*BufferedReader inputStream = Files.newBufferedReader(panoramaFile);
            JsonObject data = JsonParser.parseReader(inputStream).getAsJsonObject();
            System.out.println(data);*/
            //String fileName = id.getPath().replace(directory + "/", "").replace(".json", "");
            //reloadResource(data, id, fileName);
        } catch (Exception ignored) {}
    }

    public void onInit() {

    }

    public void load() {
        if (!Files.exists(configFile)) {
            config = instance2;
            save();
            return;
        }
        try {
            String json = new String(Files.readAllBytes(Paths.get(configFile.toUri())));
            T data = GSON.fromJson(json, instance);
            if (data != null) {
                config = data;
            }
        } catch (IOException ignored) {
        }
    }

    public void save() {
        try {
            Files.write(configFile, GSON.toJson(config).getBytes());
        } catch (IOException ignored) {

        }
    }
}