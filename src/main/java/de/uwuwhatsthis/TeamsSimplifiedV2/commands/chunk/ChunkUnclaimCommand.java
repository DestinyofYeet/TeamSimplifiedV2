package de.uwuwhatsthis.TeamsSimplifiedV2.commands.chunk;

import de.uwuwhatsthis.TeamsSimplifiedV2.main.Main;
import de.uwuwhatsthis.TeamsSimplifiedV2.utils.Errors;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.Team;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.TeamManager;
import de.uwuwhatsthis.TeamsSimplifiedV2.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ChunkUnclaimCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!Utils.isPlayer(sender)){
            return true;
        }

        Player player = (Player) sender;

        TeamManager manager = Main.getManager();

        Team playerTeam = manager.getTeamByPlayer(player);


        if (!player.hasPermission("TeamsSimplifiedV2.chunk.unclaim")){
            player.sendMessage("§cInsufficient permissions!");
            return true;
        }

        if (playerTeam == null){
            player.sendMessage("§cYou aren't in a team!");
            return true;
        }

        Main.getPlugin().getLogger().info(Arrays.toString(args));

        if (args.length > 0){
            if (args[0].equals("all")){
                Errors result = playerTeam.unclaimAllChunks(player);

                if (result == Errors.SUCCESS){
                    player.sendMessage("§aSuccessfully unclaimed all chunks");
                } else {
                    player.sendMessage(result.getValue());
                }

                return true;
            }
        }

        Errors result = playerTeam.unclaimChunk(player.getLocation().getChunk(), player);

        if (result == Errors.SUCCESS){
            player.sendMessage("§aSuccessfully unclaimed the current chunk!");
        } else {
            player.sendMessage(result.getValue());
        }

        return true;
    }
}
