package de.uwuwhatsthis.TeamsSimplifiedV2.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Utils {

    public static boolean isPlayer(CommandSender sender){
        if (!(sender instanceof Player)){
            sender.sendMessage("§cSorry, you have to be a player to use this command!");
            return false;
        }

        return true;
    };

    public static String getNameOfPlayer(UUID uuid){
        String name;

        Player player = Bukkit.getPlayer(uuid);
        if (player == null){
            name = Bukkit.getOfflinePlayer(uuid).getName();
        } else {
            name = player.getName();
        }

        return name;
    }
}
