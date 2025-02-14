package akia.net.playerNexus.cache;

import redis.clients.jedis.Jedis;
import java.util.Map;
import java.util.UUID;
import java.util.List;

public class RedisCacheStorage implements CacheStorage {

    private final Jedis jedis;
    private final List<String> modelKeys;

    public RedisCacheStorage(String host, int port, List<String> modelKeys) throws Exception {
        this.jedis = new Jedis(host, port);
        String pong = jedis.ping();
        if (!"PONG".equals(pong)) {
            throw new Exception("Impossible de se connecter à Redis (cache) : PING échoué");
        }
        this.modelKeys = modelKeys;
    }

    private String getPlayerKey(UUID uuid) {
        return "cache:player:" + uuid.toString();
    }

    @Override
    public boolean playerDataExists(UUID uuid) {
        return jedis.exists(getPlayerKey(uuid));
    }

    @Override
    public Map<String, String> loadPlayerData(UUID uuid) {
        return jedis.hgetAll(getPlayerKey(uuid));
    }

    @Override
    public void savePlayerData(UUID uuid, Map<String, String> data) {
        jedis.hmset(getPlayerKey(uuid), data);
    }

    @Override
    public String getValue(UUID uuid, String key) {
        return jedis.hget(getPlayerKey(uuid), key);
    }

    @Override
    public void setValue(UUID uuid, String key, String value) {
        jedis.hset(getPlayerKey(uuid), key, value);
    }

    @Override
    public void close() {
        if(jedis != null) {
            jedis.close();
        }
    }
}
