package de.uwuwhatsthis.TeamsSimplifiedV2.commands;

import de.uwuwhatsthis.TeamsSimplifiedV2.commands.team.*;
import de.uwuwhatsthis.TeamsSimplifiedV2.main.Main;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.TeamManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TeamCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!commandSender.hasPermission("TeamsSimplifiedV2.team.team")){
            commandSender.sendMessage("§cInsufficient permissions!");
            return true;
        }



        if (args.length < 1){
            commandSender.sendMessage("§cYou need to provide an argument!");
            return true;
        }

        switch (args[0]){
            case "create":
                return new TeamCreateCommand().onCommand(commandSender, command, s, args);

            case "info":
                return new TeamInfoCommand().onCommand(commandSender, command, s, args);

            case "edit":
                return new TeamEditCommand().onCommand(commandSender, command, s, args);

            case "delete":
                return new TeamDeleteCommand().onCommand(commandSender, command, s, args);

            case "confirmdelete":
                return new TeamConfirmDeleteCommand().onCommand(commandSender, command, s, args);

            case "leave":
                return new TeamLeaveCommand().onCommand(commandSender, command, s, args);

            case "join":
                return new TeamJoinCommand().onCommand(commandSender, command, s, args);

            case "invite":
                return new TeamInviteCommand().onCommand(commandSender, command, s, args);

            case "delinvite":
                return new TeamDelInviteCommand().onCommand(commandSender, command, s, args);

            case "promote":
                return new TeamPromoteCommand().onCommand(commandSender, command, s, args);

            case "demote":
                return new TeamDemoteCommand().onCommand(commandSender, command, s, args);

            case "kick":
                return new TeamKickCommand().onCommand(commandSender, command, s, args);

            case "list":
                return new TeamListCommand().onCommand(commandSender, command, s, args);

            case "force-reload-data":
                if (!commandSender.isOp()){
                    commandSender.sendMessage("§cInsufficient permissions!");
                    return true;
                }
                Main.setManager(TeamManager.loadTeams());
                commandSender.sendMessage("§aSuccessfully reloaded all teams!");
                return true;
        }

        return false;
    }
}
