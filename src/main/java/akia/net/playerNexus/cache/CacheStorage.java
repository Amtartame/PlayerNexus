package akia.net.playerNexus.cache;

import java.util.Map;
import java.util.UUID;

public interface CacheStorage {
    boolean playerDataExists(UUID uuid);
    Map<String, String> loadPlayerData(UUID uuid);
    void savePlayerData(UUID uuid, Map<String, String> data);
    void setValue(UUID uuid, String key, String value);
    String getValue(UUID uuid, String key);
    void close();
}

