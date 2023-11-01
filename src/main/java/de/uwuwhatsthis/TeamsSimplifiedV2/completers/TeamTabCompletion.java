package de.uwuwhatsthis.TeamsSimplifiedV2.completers;

import de.uwuwhatsthis.TeamsSimplifiedV2.main.Main;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.Team;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeamTabCompletion implements TabCompleter {

    private List<String> returnNothing(){
        return new ArrayList<>() {{
            add("");
        }};
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("team")){
            if (args.length == 1){
                return new ArrayList<>() {{
                    add("create"); // re-implemented
                    add("delete");
                    add("edit");
                    add("join");
                    add("leave");
                    add("invite");
                    add("delinvite");
                    add("list");
                    add("confirmdelete");
                    add("info");
                    add("promote");
                    add("demote");
                    add("kick");
                }};

            } else if (args.length == 2 && (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("confirmdelete") || args[0].equalsIgnoreCase("edit"))){
                if (sender instanceof ConsoleCommandSender ){
                    return Main.getManager().getAllTeamNames();
                } else if(sender instanceof Player player){
                    if (player.hasPermission("TeamsSimplifiedV2.team.delete.others")){
                        return Main.getManager().getAllTeamNames();
                    } else {
                        return Collections.singletonList(Main.getManager().getTeamByPlayer(player).getName());
                    }
                }

            } else if (args.length == 2 && args[0].equalsIgnoreCase("edit")){
                if (sender.hasPermission("TeamsSimplifiedV2.team.modify.others") || !(sender instanceof Player player)){
                    return Main.getManager().getAllTeamNames();
                }

                return Collections.singletonList(Main.getManager().getTeamByPlayer(player).getName());


            } else if (args.length == 3 && args[0].equalsIgnoreCase("edit")) {
                return new ArrayList<String>() {{
                    add("name");
                    add("tag");
                    add("color");
                    add("accessibility");
                }};

            } else if (args.length == 4 && args[0].equalsIgnoreCase("edit") && args[2].equalsIgnoreCase("accessibility")) {
                return new ArrayList<>() {{
                    add("open");
                    add("closed");
                }};

            } else if (args.length == 4  && args[0].equalsIgnoreCase("edit") && args[2].equalsIgnoreCase("color")) {
                return returnNothing();

            } else if (args.length == 4  && args[0].equalsIgnoreCase("edit") && args[2].equalsIgnoreCase("tag")) {
                return returnNothing();

            } else if (args.length == 4  && args[0].equalsIgnoreCase("edit") && args[2].equalsIgnoreCase("name")) {
                return returnNothing();

            } else if (args.length == 2 && args[0].equalsIgnoreCase("join")){
                if (!(sender instanceof Player)){
                    return returnNothing();
                }
                List<String> availableTeamsToJoin = new ArrayList<>();
                Player player = (Player) sender;

                for (Team team: Main.getManager().getAllTeams()){
                    if (team.getInvitedPlayers().contains(player.getUniqueId())
                            || team.isOpen()
                            || player.hasPermission("TeamsSimplifiedV2.team.join.overridejoin")){

                        return availableTeamsToJoin;
                    }
                }
            }
        }
        return null;
    }
}
