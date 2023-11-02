package de.uwuwhatsthis.TeamsSimplifiedV2.commands.team;

import de.uwuwhatsthis.TeamsSimplifiedV2.main.Main;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.Team;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.TeamRank;
import de.uwuwhatsthis.TeamsSimplifiedV2.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class TeamKickCommand {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args){
        if (Utils.isPlayer(commandSender)) return true;

        Player player = (Player) commandSender;

        if (!Utils.isInTeam(player)) return true;

        if (args.length < 2 ){
            player.sendMessage("§cYou need to provide a player to kick!");
            return true;
        }

        String targetPlayerName = args[1];

        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);

        if (targetPlayer == null){
            player.sendMessage("§cThat player does not exist!");
            return true;
        }

        Team team = Main.getManager().getTeamByPlayer(player);

        TeamRank rank = team.getTeamRank(player);

        if (!Arrays.asList(TeamRank.OWNER, TeamRank.MODERATOR).contains(rank)){
            player.sendMessage("§cYou need to be a moderator or the owner to kick players!");
            return true;
        }

        TeamRank targetPlayerRank = team.getTeamRank(targetPlayer);

        if (targetPlayerRank == rank){
            player.sendMessage("§cYou both have the same rank!");
            return true;
        }

        if (targetPlayerRank == TeamRank.OWNER){
            player.sendMessage("§cYou cannot kick the owner!");
            return true;
        }

        team.removePlayer(targetPlayer);

        player.sendMessage("§aSuccessfully kicked '§6" + targetPlayerName + "§a'!");
        targetPlayer.sendMessage("§cYou got kicked from the team '§6" + team.getName() + "'§c!");
        return true;
    }
}
