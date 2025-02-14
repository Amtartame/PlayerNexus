package akia.net.playerNexus.api;

import akia.net.playerNexus.PlayerNexus;
import akia.net.playerNexus.PlayerDataManager;
import org.bukkit.entity.Player;

import java.util.Map;

public class PlayerNexusAPI {

    private static PlayerNexusAPI instance;
    private final PlayerDataManager dataManager;

    public PlayerNexusAPI(PlayerNexus plugin) {
        this.dataManager = plugin.getDataManager();
        instance = this;
    }

    public static PlayerNexusAPI getInstance() {
        return instance;
    }

    /**
     * Vérifie si des données existent pour un joueur.
     *
     * @param player Le joueur concerné.
     * @return true si des données existent, sinon false.
     */
    public boolean hasData(Player player) {
        return dataManager.playerDataExists(player.getUniqueId());
    }

    /**
     * Récupère toutes les données d'un joueur.
     *
     * @param player Le joueur concerné.
     * @return Une map contenant les clés et valeurs associées.
     */
    public Map<String, String> getAllData(Player player) {
        return dataManager.loadPlayerData(player.getUniqueId());
    }

    /**
     * Récupère une valeur spécifique pour un joueur.
     *
     * @param player Le joueur concerné.
     * @param key    La clé de la donnée.
     * @return La valeur associée à la clé, ou null si inexistante.
     */
    public String getValue(Player player, String key) {
        return dataManager.getValue(player.getUniqueId(), key);
    }

    /**
     * Définit une valeur spécifique pour un joueur.
     *
     * @param player Le joueur concerné.
     * @param key    La clé de la donnée.
     * @param value  La valeur à enregistrer.
     */
    public void setValue(Player player, String key, String value) {
        dataManager.setValue(player.getUniqueId(), key, value);
    }

    /**
     * Ferme proprement le gestionnaire de données lorsqu'il n'est plus nécessaire.
     */
    public void close() {
        dataManager.close();
    }
}
