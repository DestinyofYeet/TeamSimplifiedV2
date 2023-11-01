package de.uwuwhatsthis.TeamsSimplifiedV2.commands.team;

import de.uwuwhatsthis.TeamsSimplifiedV2.main.Main;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.Team;
import de.uwuwhatsthis.TeamsSimplifiedV2.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamJoinCommand {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args){
        if (!Utils.isPlayer(commandSender)) return true;

        Player player = (Player) commandSender;

        if (!player.hasPermission("TeamsSimplifiedV2.team.join.join")){
            player.sendMessage("§cInsufficient permissions!");
            return true;
        }

        if (args.length < 2){
            player.sendMessage("§cYou need to provide a team to join!");
            return true;
        }

        String teamName = args[1];

        Team team = Main.getManager().getTeamByName(teamName);

        if (team == null){
            player.sendMessage("§cThat team doesn't exist!");
            return true;
        }

        if (team.isOpen()){
            team.joinTeam(player);
            player.sendMessage("§aSuccessfully joined the team '§6" + teamName + "§a'!");

        } else {
            boolean playerCanOverride = player.hasPermission("TeamsSimplifiedV2.team.join.joinanyways");

            if (!team.isInvited(player)){
                if (!playerCanOverride){
                    player.sendMessage("§cThat team is not open and you are not invited!");
                } else {
                    player.sendMessage("§aSuccessfully joined the team '§6" + teamName + "§a' using op permissions!");
                    team.joinTeam(player);
                }
                return true;
            }

            team.joinTeam(player);


            player.sendMessage("§aSuccessfully joined the team '§6" + teamName + "§a' using the invite!");
        }

        return true;
    }
}
