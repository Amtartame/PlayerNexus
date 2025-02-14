package akia.net.playerNexus;

import akia.net.playerNexus.api.PlayerNexusAPI;
import akia.net.playerNexus.cache.CacheStorage;
import akia.net.playerNexus.cache.LocalCacheStorage;
import akia.net.playerNexus.cache.RedisCacheStorage;
import akia.net.playerNexus.command.PlayerDataCommand;
import akia.net.playerNexus.storage.MysqlPersistentStorage;
import akia.net.playerNexus.storage.PersistentStorage;
import akia.net.playerNexus.storage.YamlPersistentStorage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Logger;

public class PlayerNexus extends JavaPlugin implements Listener {

    private PlayerDataManager dataManager;
    private List<String> modelKeys;
    private Logger logger;
    private PlayerNexusAPI api;


    @Override
    public void onEnable() {
        logger = this.getLogger();
        // Sauvegarder le fichier config.yml par défaut s'il n'existe pas
        saveDefaultConfig();
        FileConfiguration config = getConfig();

        // Chargement du schéma défini dans config.yml (section model.player)
        if (config.isList("model.player")) {
            modelKeys = config.getStringList("model.player");
        } else {
            modelKeys = Arrays.asList("money", "experience");
            config.set("model.player", modelKeys);
            saveConfig();
        }

        // Instanciation du stockage permanent
        PersistentStorage persistentStorage;
        String persType = config.getString("storage.persistent", "yml").toLowerCase();
        try {
            switch (persType) {
                case "mysql":
                    String host = config.getString("storage.persistent_mysql.host", "localhost");
                    int port = config.getInt("storage.persistent_mysql.port", 3306);
                    String database = config.getString("storage.persistent_mysql.database", "playerdata");
                    String username = config.getString("storage.persistent_mysql.username", "user");
                    String password = config.getString("storage.persistent_mysql.password", "pass");
                    persistentStorage = new MysqlPersistentStorage(host, port, database, username, password, modelKeys);
                    break;
                case "yml":
                default:
                    String filePath = config.getString("storage.persistent_yaml.path", getDataFolder() + "/data.yml");
                    persistentStorage = new YamlPersistentStorage(filePath, modelKeys);
                    break;
            }
        } catch (Exception e) {
            logger.severe("Erreur lors de l'initialisation du stockage permanent (" + persType + "): " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Instanciation du cache
        CacheStorage cacheStorage;
        String cacheType = config.getString("storage.cache", "local").toLowerCase();
        try {
            switch (cacheType) {
                case "redis":
                    String rHost = config.getString("storage.cache_redis.host", "localhost");
                    int rPort = config.getInt("storage.cache_redis.port", 6379);
                    cacheStorage = new RedisCacheStorage(rHost, rPort, modelKeys);
                    break;
                case "local":
                default:
                    cacheStorage = new LocalCacheStorage(modelKeys);
                    break;
            }
        } catch (Exception e) {
            logger.severe("Erreur lors de l'initialisation du cache (" + cacheType + "): " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Création du manager global qui combine stockage permanent et cache
        dataManager = new PlayerDataManagerImpl(persistentStorage, cacheStorage, modelKeys);

        // Enregistrement des events et de la commande
        getServer().getPluginManager().registerEvents(this, this);
        Objects.requireNonNull(getCommand("pdata")).setExecutor(new PlayerDataCommand(dataManager));

        this.api = new PlayerNexusAPI(this);

        logger.info("PlayerNexus plugin activé!");
    }

    @Override
    public void onDisable() {
        if (dataManager != null) {
            dataManager.close();
        }
        logger.info("PlayerNexus plugin désactivé!");
    }

    // À la connexion, on initialise les données du joueur si elles n'existent pas déjà
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!dataManager.playerDataExists(uuid)) {
            Map<String, String> defaultData = new HashMap<>();
            for (String key : modelKeys) {
                defaultData.put(key, "0");
            }
            dataManager.savePlayerData(uuid, defaultData);
            getLogger().info("Initialisation des données pour " + player.getName());
        } else {
            // Charger le cache à partir du stockage permanent si nécessaire
            dataManager.loadPlayerData(uuid);
        }
    }

    public PlayerDataManager getDataManager() {
        return dataManager;
    }

    public PlayerNexusAPI getAPI() {
        return api;
    }
}
