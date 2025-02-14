package akia.net.playerNexus.command;

import akia.net.playerNexus.PlayerDataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class PlayerDataCommand implements CommandExecutor {

    private final PlayerDataManager dataManager;

    public PlayerDataCommand(PlayerDataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Seuls les joueurs peuvent utiliser cette commande.");
            return true;
        }
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        if (args.length == 0) {
            Map<String, String> data = dataManager.loadPlayerData(uuid);
            player.sendMessage("§6Vos données:");
            data.forEach((key, value) -> player.sendMessage("§e" + key + " : " + value));
        } else if (args.length == 1) {
            String key = args[0];
            String value = dataManager.getValue(uuid, key);
            if (value != null) {
                player.sendMessage("§e" + key + " : " + value);
            } else {
                player.sendMessage("§cLa clé '" + key + "' n'existe pas dans le schéma.");
            }
        } else if (args.length == 2) {
            String key = args[0];
            String value = args[1];
            if (dataManager.getValue(uuid, key) != null) {
                dataManager.setValue(uuid, key, value);
                player.sendMessage("§aMise à jour de " + key + " à " + value);
            } else {
                player.sendMessage("§cLa clé '" + key + "' n'existe pas dans le schéma.");
            }
        } else {
            player.sendMessage("§cUtilisation: /pdata [clé] [valeur]");
        }
        return true;
    }
}
