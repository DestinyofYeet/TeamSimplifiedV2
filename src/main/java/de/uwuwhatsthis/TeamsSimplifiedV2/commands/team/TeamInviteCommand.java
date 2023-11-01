package de.uwuwhatsthis.TeamsSimplifiedV2.commands.team;

import de.uwuwhatsthis.TeamsSimplifiedV2.main.Main;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.Team;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.TeamRank;
import de.uwuwhatsthis.TeamsSimplifiedV2.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TeamInviteCommand {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args){
        if (!Utils.isPlayer(commandSender)) return true;

        Player player = (Player) commandSender;

        if (args.length < 2){
            player.sendMessage("§cYou need to specify a player to invite!");
            return true;
        }

        Team team = Main.getManager().getTeamByPlayer(player);

        if (team == null){
            player.sendMessage("§cYou need to be in a team to send out an invite!");
            return true;
        }

        TeamRank rank = team.getTeamRank(player);

        if (rank == TeamRank.PLAYER){
            player.sendMessage("§cSorry, you have to be a moderator or the owner to send out invites!");
            return true;
        }

        String targetPlayerName = args[1];

        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);

        if (targetPlayer == null){
            player.sendMessage("§cThe player with the name '§6" + targetPlayerName + "§c' does not exist!");
            return true;
        }

        player.sendMessage("§aSuccessfully invited '§6" + targetPlayerName + "§a'!");

        targetPlayer.sendMessage("§aYou received an invite for the team '§6" + team.getName() + "§a' from the player '§6" + player.getName() + "§a'! Do §6/team join " + team.getName() + "§a to accept the invite!");
        team.getInvitedPlayers().add(targetPlayer.getUniqueId());
        return true;
    }
}
