package de.uwuwhatsthis.TeamsSimplifiedV2.commands.team;

import de.uwuwhatsthis.TeamsSimplifiedV2.main.Main;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.Team;
import de.uwuwhatsthis.TeamsSimplifiedV2.utils.Defaults;
import de.uwuwhatsthis.TeamsSimplifiedV2.utils.Errors;
import de.uwuwhatsthis.TeamsSimplifiedV2.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class TeamCreateCommand {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args){
        if (!Utils.isPlayer(commandSender)) return true;

        Player player = (Player) commandSender;

        if (!player.hasPermission("TeamsSimplifiedV2.team.create")){
            player.sendMessage("§cInsufficient permissions!");
            return true;
        }

        if (args.length < 2){
            player.sendMessage("§cYou need to provide a name for your team!");
            return true;
        }

        if (args.length < 3){
            player.sendMessage("§cYou need to provide a tag for your team!");
            return true;
        }


        String teamName = args[1];
        String teamTag = args[2];
        String color;

        if (args.length < 4){
            color = "";
        } else {
            color = args[3];
        }

        FileConfiguration config = Main.getPlugin().getConfig();

        int maxTeamNameLength = config.getInt(Defaults.CONFIG_MAX_TEAM_NAME_LENGTH.getValue());
        int maxTeamTagLength = config.getInt(Defaults.CONFIG_MAX_TEAM_TAG_LENGTH.getValue());

        if (teamName.length() > maxTeamNameLength){
            player.sendMessage("§cThe maximum length of a team name is " + maxTeamNameLength);
            return true;
        }

        if (teamTag.length() > maxTeamTagLength){
            player.sendMessage("§cThe maximum length of a team tag is " + maxTeamTagLength);
            return true;
        }

        Errors status = Main.getManager().createTeam(teamName, teamTag, color, player);

        if (status == Errors.SUCCESS){
            Team team = Main.getManager().getTeamByPlayer(player);
            player.sendMessage("§aSuccessfully created team " + team.getColor().replace("&", "§") + teamName + "§a!");
        } else {
            player.sendMessage(status.getValue());
        }

        return true;
    }
}
