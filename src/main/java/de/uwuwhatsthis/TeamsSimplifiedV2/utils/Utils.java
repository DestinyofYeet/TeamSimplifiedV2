package de.uwuwhatsthis.TeamsSimplifiedV2.utils;

import de.uwuwhatsthis.TeamsSimplifiedV2.main.Main;
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

    public static boolean isInTeam(Player player){
        if (Main.getManager().getTeamByPlayer(player) == null){
            player.sendMessage("§cYou need to be in a team to run that command!");
            return false;
        }

        return true;
    }

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
