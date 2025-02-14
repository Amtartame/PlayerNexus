package akia.net.playerNexus.storage;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class YamlPersistentStorage implements PersistentStorage {

    private final File file;
    private final YamlConfiguration config;
    private final List<String> modelKeys;

    public YamlPersistentStorage(String filePath, List<String> modelKeys) throws Exception {
        this.file = new File(filePath);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                throw new Exception("Impossible de créer le fichier YAML: " + e.getMessage());
            }
        }
        this.config = YamlConfiguration.loadConfiguration(file);
        this.modelKeys = modelKeys;
        if (!file.canWrite()) {
            throw new Exception("Le fichier YAML n'est pas accessible en écriture.");
        }
    }

    private String getPlayerPath(UUID uuid) {
        return "players." + uuid.toString();
    }

    @Override
    public boolean playerDataExists(UUID uuid) {
        return config.contains(getPlayerPath(uuid));
    }

    @Override
    public Map<String, String> loadPlayerData(UUID uuid) {
        Map<String, String> data = new HashMap<>();
        String path = getPlayerPath(uuid);
        for (String key : modelKeys) {
            String value = config.getString(path + "." + key, "0");
            data.put(key, value);
        }
        return data;
    }

    @Override
    public void savePlayerData(UUID uuid, Map<String, String> data) {
        String path = getPlayerPath(uuid);
        for (Map.Entry<String, String> entry : data.entrySet()) {
            config.set(path + "." + entry.getKey(), entry.getValue());
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getValue(UUID uuid, String key) {
        if (!modelKeys.contains(key)) return null;
        return config.getString(getPlayerPath(uuid) + "." + key, "0");
    }

    @Override
    public void setValue(UUID uuid, String key, String value) {
        if (!modelKeys.contains(key)) return;
        config.set(getPlayerPath(uuid) + "." + key, value);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        // Aucun traitement nécessaire pour YAML
    }
}
