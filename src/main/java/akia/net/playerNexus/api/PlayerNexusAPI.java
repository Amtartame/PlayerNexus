package akia.net.playerNexus.api;

import akia.net.playerNexus.PlayerNexus;
import org.bukkit.entity.Player;

public class PlayerNexusAPI {

    private static PlayerNexusAPI instance;
    private final PlayerNexus plugin;

    public PlayerNexusAPI(PlayerNexus plugin) {
        this.plugin = plugin;
        instance = this;
    }

    public static PlayerNexusAPI getInstance() {
        return instance;
    }

    public String getValue(Player player, String key) {
        return plugin.getConfig().getString(player.getUniqueId() + "." + key);
    }

}
