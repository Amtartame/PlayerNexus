package akia.net.playerNexus.api;

import akia.net.playerNexus.PlayerNexus;
import akia.net.playerNexus.PlayerDataManager;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class PlayerNexusAPI {

    private static PlayerNexusAPI instance;
    private final PlayerDataManager dataManager; // Utilisation de PlayerDataManager

    public PlayerNexusAPI(PlayerNexus plugin) {
        this.dataManager = plugin.getDataManager(); // Récupération du gestionnaire de données
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
     * Sauvegarde toutes les données d'un joueur.
     *
     * @param player Le joueur concerné.
     * @param data   Les données à sauvegarder.
     */
    public void saveAllData(Player player, Map<String, String> data) {
        dataManager.savePlayerData(player.getUniqueId(), data);
    }

    /**
     * Ferme proprement le gestionnaire de données lorsqu'il n'est plus nécessaire.
     */
    public void close() {
        dataManager.close();
    }
}
