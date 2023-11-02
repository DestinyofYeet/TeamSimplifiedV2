package de.uwuwhatsthis.TeamsSimplifiedV2.commands.chunk;

import de.uwuwhatsthis.TeamsSimplifiedV2.main.Main;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.Chunk;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.Team;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.TeamManager;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.TeamRank;
import de.uwuwhatsthis.TeamsSimplifiedV2.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.C;

import java.util.concurrent.CopyOnWriteArrayList;

public class ChunkExplosionsCommand {

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args){
        if (!Utils.isPlayer(sender)){
            return true;
        }

        Player player = (Player) sender;

        TeamManager manager = Main.getManager();

        Chunk chunk = new Chunk(player.getLocation().getChunk());

        Team team = manager.chunkIsClaimedBy(chunk);

        if (team == null){
            player.sendMessage("§cThis chunk is not claimed by anyone!");
            return true;
        }

        if (!team.getCompleteListOfPlayers().contains(player.getUniqueId())){
            player.sendMessage("§cYou are not in the team that claimed the chunk!");
            return true;
        }

        if (args.length < 2){
            player.sendMessage("§cYou have to provide a new option to set the explosion to!");
            return true;
        }

        TeamRank rank = team.getTeamRank(player);

        if (rank == TeamRank.PLAYER){
            player.sendMessage("§cYou need to be a moderator or the owner to enable or disable explosions!");
            return true;
        }

        Chunk chunkWithData = team.getChunkData(chunk);

        String option = args[1];

        if (option.equalsIgnoreCase("enable")){
            if (chunkWithData.isExplosionEnabled()){
                player.sendMessage("§cExplosions are already enabled in this chunk!");

            } else {
                chunkWithData.setExplosionEnabled(true);
                player.sendMessage("§aSuccessfully enabled explosions in this chunk!");
            }

            return true;

        } else if (option.equalsIgnoreCase("disable")){
            if (!chunkWithData.isExplosionEnabled()){
                player.sendMessage("§cExplosions are disabled in this chunk!");
            } else {
                chunkWithData.setExplosionEnabled(false);
                player.sendMessage("§aSuccessfully disabled explosions in this chunk!");
            }

            return true;
        } else {
            player.sendMessage("§cInvalid option!");
        }

        return true;
    }
}
