package de.uwuwhatsthis.TeamsSimplifiedV2.commands.team;

import de.uwuwhatsthis.TeamsSimplifiedV2.main.Main;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.Team;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.TeamRank;
import de.uwuwhatsthis.TeamsSimplifiedV2.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.N;

public class TeamPromoteCommand {
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args){
        if (!Utils.isPlayer(commandSender)) return true;

        Player player = (Player) commandSender;

        Team team = Main.getManager().getTeamByPlayer(player);

        if (team == null){
            player.sendMessage("§cYou are not in a team!");
            return true;
        }

        if (args.length < 2){
            player.sendMessage("§cYou need to provide a player to promote!");
            return true;
        }

        String targetPlayerName = args[1];

        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);

        if (targetPlayer == null){
            player.sendMessage("§cThat player doesn't exist or isn't online!");
            return true;
        }

        TeamRank rank = team.getTeamRank(player);

        if (rank != TeamRank.OWNER){
            player.sendMessage("§cSorry, only the owner can promote players!");
            return true;
        }

        TeamRank targetPlayerRank = team.getTeamRank(targetPlayer);

        if (targetPlayerRank != TeamRank.PLAYER){
            player.sendMessage("§cThat player already is a moderator!");
            return true;
        }

        team.promotePlayer(targetPlayer);

        player.sendMessage("§aSuccessfully promoted '§6" + targetPlayerName + "§a' to moderator!");

        targetPlayer.sendMessage("§aYou got promoted to moderator by '§6" + player.getName() + "§a'!");

        return true;
    }
}
