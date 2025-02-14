package akia.net.playerNexus.storage;

import java.util.Map;
import java.util.UUID;

public interface PersistentStorage {
    boolean playerDataExists(UUID uuid);
    Map<String, String> loadPlayerData(UUID uuid);
    void savePlayerData(UUID uuid, Map<String, String> data);
    void setValue(UUID uuid, String key, String value);
    String getValue(UUID uuid, String key);
    void close();
}
