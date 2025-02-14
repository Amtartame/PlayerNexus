package akia.net.playerNexus.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.List;

public class LocalCacheStorage implements CacheStorage {

    private final Map<UUID, Map<String, String>> cache;
    private final List<String> modelKeys;

    public LocalCacheStorage(List<String> modelKeys) {
        this.cache = new HashMap<>();
        this.modelKeys = modelKeys;
    }

    @Override
    public boolean playerDataExists(UUID uuid) {
        return cache.containsKey(uuid);
    }

    @Override
    public Map<String, String> loadPlayerData(UUID uuid) {
        return cache.getOrDefault(uuid, new HashMap<>());
    }

    @Override
    public void savePlayerData(UUID uuid, Map<String, String> data) {
        cache.put(uuid, data);
    }

    @Override
    public String getValue(UUID uuid, String key) {
        Map<String, String> data = cache.get(uuid);
        return (data != null) ? data.getOrDefault(key, "0") : "0";
    }

    @Override
    public void setValue(UUID uuid, String key, String value) {
        Map<String, String> data = cache.getOrDefault(uuid, new HashMap<>());
        data.put(key, value);
        cache.put(uuid, data);
    }

    @Override
    public void close() {
        // Pas d'action particulière pour le cache local
    }
}
