package de.uwuwhatsthis.TeamsSimplifiedV2.commands.team;

import de.uwuwhatsthis.TeamsSimplifiedV2.main.Main;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.Team;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class TeamListCommand {
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args){
        ArrayList<String> openTeams = new ArrayList<>(){{
            for (Team team: Main.getManager().getAllTeams()){
                if (team.isOpen()){
                    add(team.getName());
                }
            }
        }};

        if (!openTeams.isEmpty()){
            commandSender.sendMessage("§6Open teams to join: §a" + openTeams.toString().replace("[", "").replace("]", ""));
        } else {
            commandSender.sendMessage("§cThere are no open teams to join!");
        }

        return true;
    }
}
