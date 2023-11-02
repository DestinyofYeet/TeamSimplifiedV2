package de.uwuwhatsthis.TeamsSimplifiedV2.commands.team;

import de.uwuwhatsthis.TeamsSimplifiedV2.main.Main;
import de.uwuwhatsthis.TeamsSimplifiedV2.utils.Defaults;
import de.uwuwhatsthis.TeamsSimplifiedV2.utils.Errors;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.Team;
import de.uwuwhatsthis.TeamsSimplifiedV2.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamEditCommand {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args){
        if (!commandSender.hasPermission("TeamsSimplifiedV2.team.modify.modify")){
            commandSender.sendMessage("§cInsufficient permissions");
            return true;
        }

        if (args.length < 2){
            commandSender.sendMessage("§cYou need to provide a team name to edit!");
            return true;
        }

        String teamName = args[1];

        Team teamToEdit = Main.getManager().getTeamByName(teamName);

        if (teamToEdit == null) {
            commandSender.sendMessage("§cThe team '" + teamName + "' does not exist!");
            return true;
        }



        if (!commandSender.hasPermission("TeamsSimplifiedV2.team.modify.others")){
            if (!Utils.isPlayer(commandSender)){
                return true;
            }

            Player player = (Player) commandSender;

            if (!teamToEdit.isOwner(player)){
                player.sendMessage("§cYou are not the owner of the team '" + teamName + "' so you can't edit it.");
                return true;
            }
        }

        if (args.length < 3){
            commandSender.sendMessage("§cYou need to provide an argument on what to edit!");
            return true;
        }

        if (args.length < 4){
            commandSender.sendMessage("§cYou need to provide a new value!");
            return true;
        }

        String newValue = args[3];

        boolean updateMembers = false;

        switch (args[2]){
            case "name":
                if (newValue.length() > Main.getPlugin().getConfig().getInt(Defaults.CONFIG_MAX_TEAM_NAME_LENGTH.getValue())){
                    commandSender.sendMessage("§cThat team name is too long!");
                    return true;
                }

                for (Team team: Main.getManager().getAllTeams()){
                    if (team.getName().equals(newValue)){
                        commandSender.sendMessage(Errors.TEAM_NAME_ALREADY_USED.getValue());
                        return true;
                    }
                }

                teamToEdit.setName(newValue);
                updateMembers = true;
                commandSender.sendMessage("§aSuccessfully renamed the team to '§6" + newValue + "§a'!");
                break;

            case "color":
                if (!newValue.startsWith("&")){
                    commandSender.sendMessage("§cYour color has to start with a '&'. Set it to '&a' to get green for example. Lookup 'bukkit colorcodes' for more colors and formatting options!");
                }

                teamToEdit.setColor(newValue);
                updateMembers = true;
                commandSender.sendMessage("§aSuccessfully changed color to " + teamToEdit.getColor().replace("&", "§") + "this color§a!");
                break;

            case "tag":
                if (newValue.length() > Main.getPlugin().getConfig().getInt(Defaults.CONFIG_MAX_TEAM_TAG_LENGTH.getValue())){
                    commandSender.sendMessage("§cThat tag is too long!");
                    return true;
                }

                for (Team team: Main.getManager().getAllTeams()){
                    if (team.getTag().equals(newValue)){
                        commandSender.sendMessage(Errors.TEAM_TAG_ALREADY_USED.getValue());
                        return true;
                    }
                }

                teamToEdit.setTag(newValue);
                updateMembers = true;
                commandSender.sendMessage("§aSuccessfully updated the tag to '§6" + newValue + "§a'!");
                break;

            case "accessibility":
                if (newValue.equalsIgnoreCase("closed")){
                    if (teamToEdit.isOpen()){
                        teamToEdit.setOpen(false);
                        commandSender.sendMessage("§aThe team is now closed!");
                    } else {
                        commandSender.sendMessage("§cThe team is already closed!");
                    }
                } else if (newValue.equalsIgnoreCase("open")){
                    if (!teamToEdit.isOpen()){
                        teamToEdit.setOpen(true);
                        commandSender.sendMessage("§aThe team is now open!");
                    } else {
                        commandSender.sendMessage("§cThe team is already open!");
                    }
                } else {
                    commandSender.sendMessage("§cInvalid option!");
                }

            case "explosions":
                if (newValue.equalsIgnoreCase("enable")){
                    if (!teamToEdit.isExplosionsEnabled()){
                        teamToEdit.setExplosionsEnabled(true);
                        commandSender.sendMessage("§aExplosions are now enabled!");
                    } else {
                        commandSender.sendMessage("§cExplosions are already enabled!");
                    }

                } else if (newValue.equalsIgnoreCase("disable")){
                    if (teamToEdit.isExplosionsEnabled()){
                        teamToEdit.setExplosionsEnabled(false);
                        commandSender.sendMessage("§aExplosions are now disabled!");
                    } else {
                        commandSender.sendMessage("§cExplosions are already disabled!");
                    }
                }
        }

        Main.getManager().saveTeams();
        if (updateMembers) teamToEdit.updateDisplayNameOfMembers();

        return true;
    }
}
