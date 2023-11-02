package de.uwuwhatsthis.TeamsSimplifiedV2.commands.team;

import de.uwuwhatsthis.TeamsSimplifiedV2.main.Main;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.Team;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.TeamRank;
import de.uwuwhatsthis.TeamsSimplifiedV2.utils.Errors;
import de.uwuwhatsthis.TeamsSimplifiedV2.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamLeaveCommand {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args){
        if (!Utils.isPlayer(commandSender)) return true;

        Player player = (Player) commandSender;

        if (!player.hasPermission("TeamsSimplifiedV2.team.leave")){
            player.sendMessage("§cInsufficient permissions!");
            return true;
        }

        Team team = Main.getManager().getTeamByPlayer(player);

        if (team == null){
            player.sendMessage("§cYou are not in a team!");
            return true;
        }

        TeamRank rank = team.getTeamRank(player);

        if (rank == TeamRank.OWNER){
            player.sendMessage("§cYou cannot leave the team, because you are the owner!");
            return true;
        }

        Errors status = team.removePlayer(player);

        if (status == Errors.SUCCESS){
            player.sendMessage("§aSuccessfully left the team!");
        } else {
            player.sendMessage(status.getValue());
        }

        return true;
    }
}
