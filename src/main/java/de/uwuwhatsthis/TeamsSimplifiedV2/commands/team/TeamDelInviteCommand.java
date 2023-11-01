package de.uwuwhatsthis.TeamsSimplifiedV2.commands.team;

import de.uwuwhatsthis.TeamsSimplifiedV2.main.Main;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.Team;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.TeamRank;
import de.uwuwhatsthis.TeamsSimplifiedV2.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamDelInviteCommand {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args){
        if (!Utils.isPlayer(commandSender)) return true;

        Player player = (Player) commandSender;

        if (args.length < 2){
            player.sendMessage("§cYou need to specify a player to revoke the invite from!");
            return true;
        }

        Team team = Main.getManager().getTeamByPlayer(player);

        if (team == null){
            player.sendMessage("§cYou need to be in a team to revoke an invite!");
            return true;
        }

        TeamRank rank = team.getTeamRank(player);

        if (rank == TeamRank.PLAYER){
            player.sendMessage("§cSorry, you have to be a moderator or the owner to revoke invites!");
            return true;
        }

        String targetPlayerName = args[1];

        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);

        if (targetPlayer == null){
            player.sendMessage("§cThe player with the name '§6" + targetPlayerName + "§c' does not exist!");
            return true;
        }

        if (!team.getInvitedPlayers().contains(targetPlayer.getUniqueId())){
            player.sendMessage("§cThe player '§6" + targetPlayerName + "§c' does not have an invite!");
            return true;
        }

        player.sendMessage("§aSuccessfully revoked the invite for '§6" + targetPlayerName + "§a'!");

        targetPlayer.sendMessage("§aYour invite for the team '§6" + team.getName() + "§a' got revoked from the player '§6" + player.getName() + "§a'!");
        team.getInvitedPlayers().remove(targetPlayer.getUniqueId());
        Main.getManager().saveTeams();
        return true;
    }
}
