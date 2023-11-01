package de.uwuwhatsthis.TeamsSimplifiedV2.commands.chunks;

import de.uwuwhatsthis.TeamsSimplifiedV2.main.Main;
import de.uwuwhatsthis.TeamsSimplifiedV2.utils.Errors;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.Team;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.TeamManager;
import de.uwuwhatsthis.TeamsSimplifiedV2.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
        /claim

        claims the current chunk if the player is in a team
 */
public class ChunkClaimCommand {

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!Utils.isPlayer(sender)){
            return true;
        }

        Player player = (Player) sender;

        TeamManager manager = Main.getManager();

        Team playerTeam = manager.getTeamByPlayer(player);


        if (!player.hasPermission("TeamsSimplifiedV2.chunk.claim")){
            player.sendMessage("§cInsufficient permissions!");
            return true;
        }

        if (playerTeam == null){
            player.sendMessage("§cYou aren't in a team!");
            return true;
        }


        Errors result = playerTeam.claimChunk(player.getLocation().getChunk(), player);

        if (result == Errors.SUCCESS){
            player.sendMessage("§aSuccessfully claimed the current chunk!");
        } else {
            player.sendMessage(result.getValue());
        }

        return true;
    }
}
