package de.uwuwhatsthis.TeamsSimplifiedV2.commands.team;

import de.uwuwhatsthis.TeamsSimplifiedV2.main.Main;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.Team;
import de.uwuwhatsthis.TeamsSimplifiedV2.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class TeamInfoCommand {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args){
        Team team;

        if (args.length < 2){
            if (!Utils.isPlayer(commandSender)){
                return true;
            }

            Player player = (Player) commandSender;

            team = Main.getManager().getTeamByPlayer(player);

            if (team == null){
                player.sendMessage("§cYou are not in a team!");
                return true;
            }

        } else {
            team = Main.getManager().getTeamByName(args[2]);

            if (team == null){
                commandSender.sendMessage("§cThe team '" + args[2] + "' doesn't exist!");
                return true;
            }
        }



        StringBuilder builder = new StringBuilder();

        ArrayList<String> players = new ArrayList<>(team.getPlayers().size()){{
            for (UUID player: team.getPlayers()){
                add(Utils.getNameOfPlayer(player));
            }
        }};

        ArrayList<String> moderators = new ArrayList<>(team.getModerators().size()){{
            for (UUID player: team.getModerators()){
                add(Utils.getNameOfPlayer(player));
            }
        }};

        String realColor = team.getColor().replace("&", "§");

        builder.append("§6Team name: ").append(realColor).append(" ").append(team.getName()).append("§r\n")
                .append("§6Team tag: ").append(realColor).append(team.getTag()).append("§r\n")
                .append("§6Team Owner: §a").append(Utils.getNameOfPlayer(team.getOwner())).append("§r\n")
                .append("§6Team moderators: §a").append(String.join(", ", moderators)).append("§r\n")
                .append("§6Team members: §a").append(String.join(", ", players)).append("§r\n")
                .append("§6Chunks claimed: §a").append(team.getClaimedChunks().size());

        commandSender.sendMessage(builder.toString());


        return true;
    }
}
