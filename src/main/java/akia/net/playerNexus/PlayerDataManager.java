package akia.net.playerNexus;

import java.util.Map;
import java.util.UUID;

public interface PlayerDataManager {

    boolean playerDataExists(UUID uuid);
    Map<String, String> loadPlayerData(UUID uuid);
    void savePlayerData(UUID uuid, Map<String, String> data);
    String getValue(UUID uuid, String key);
    void setValue(UUID uuid, String key, String value);
    void close();


}
