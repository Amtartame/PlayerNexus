package akia.net.playerNexus;

import akia.net.playerNexus.cache.CacheStorage;
import akia.net.playerNexus.storage.PersistentStorage;

import java.util.Map;
import java.util.UUID;
import java.util.List;

public class PlayerDataManagerImpl implements PlayerDataManager {

    private final PersistentStorage persistentStorage;
    private final CacheStorage cacheStorage;
    private final List<String> modelKeys;

    public PlayerDataManagerImpl(PersistentStorage persistentStorage, CacheStorage cacheStorage, List<String> modelKeys) {
        this.persistentStorage = persistentStorage;
        this.cacheStorage = cacheStorage;
        this.modelKeys = modelKeys;
    }

    @Override
    public boolean playerDataExists(UUID uuid) {
        return persistentStorage.playerDataExists(uuid);
    }

    @Override
    public Map<String, String> loadPlayerData(UUID uuid) {
        if (cacheStorage.playerDataExists(uuid)) {
            return cacheStorage.loadPlayerData(uuid);
        } else {
            Map<String, String> data = persistentStorage.loadPlayerData(uuid);
            cacheStorage.savePlayerData(uuid, data);
            return data;
        }
    }

    @Override
    public void savePlayerData(UUID uuid, Map<String, String> data) {
        persistentStorage.savePlayerData(uuid, data);
        cacheStorage.savePlayerData(uuid, data);
    }

    @Override
    public String getValue(UUID uuid, String key) {
        if (!modelKeys.contains(key)) return null;
        Map<String, String> data = cacheStorage.loadPlayerData(uuid);
        return data.get(key);
    }

    @Override
    public void setValue(UUID uuid, String key, String value) {
        if (!modelKeys.contains(key)) return;
        persistentStorage.setValue(uuid, key, value);
        cacheStorage.setValue(uuid, key, value);
    }

    @Override
    public void close() {
        persistentStorage.close();
        cacheStorage.close();
    }
}
