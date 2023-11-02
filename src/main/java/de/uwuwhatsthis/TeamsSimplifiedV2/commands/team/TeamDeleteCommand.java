package de.uwuwhatsthis.TeamsSimplifiedV2.commands.team;

import de.uwuwhatsthis.TeamsSimplifiedV2.main.Main;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.Team;
import de.uwuwhatsthis.TeamsSimplifiedV2.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamDeleteCommand {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args){
        if (!commandSender.hasPermission("TeamsSimplifiedV2.team.delete.own")){
            commandSender.sendMessage("§cInsufficient permissions!");
            return true;
        }

        Team team;

        if (args.length < 2){
            commandSender.sendMessage("§cYou need to provide a team to delete!");
            return true;
        }

        team = Main.getManager().getTeamByName(args[1]);

        if (team == null){
            commandSender.sendMessage("§cThe team '§6" + args[1] + "§c' does not exist!");
            return true;
        }

        if (commandSender.hasPermission("TeamsSimplifiedV2.team.delete.others")){
            commandSender.sendMessage("§cPlease run §6/team confirmdelete " + team.getName() + "§c to delete your team!");
            return true;
        }

        if (!Utils.isPlayer(commandSender)) return true;

        Player player = (Player) commandSender;

        if (!team.isOwner(player)){
            player.sendMessage("§cYou are not the owner of that team, so you can't delete it!");
            return true;
        }

        player.sendMessage("§cPlease run §6/team confirmdelete " + team.getName() + "§c to delete your team!");

        return true;
    }
}
