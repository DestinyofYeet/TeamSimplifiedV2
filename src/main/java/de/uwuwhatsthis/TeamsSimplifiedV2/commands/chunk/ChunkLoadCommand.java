package de.uwuwhatsthis.TeamsSimplifiedV2.commands.chunk;

import de.uwuwhatsthis.TeamsSimplifiedV2.main.Main;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.Chunk;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.Team;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.TeamRank;
import de.uwuwhatsthis.TeamsSimplifiedV2.utils.Defaults;
import de.uwuwhatsthis.TeamsSimplifiedV2.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChunkLoadCommand {

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args){
        if (!Utils.isPlayer(sender)) return true;

        Player player = (Player) sender;

        if (!player.hasPermission("TeamsSimplifiedV2.chunk.load")){
            player.sendMessage("§cInsufficient permissions!");
            return true;
        }

        Team team = Main.getManager().getTeamByPlayer(player);

        if (team == null){
            player.sendMessage("§cYou are not in a team!");
            return true;
        }

        TeamRank rank = team.getTeamRank(player);

        if (rank == TeamRank.PLAYER){
            player.sendMessage("§cYou have to be a moderator or the owner to load chunks!");
            return true;
        }

        if (args.length < 2){
            player.sendMessage("§cYou need to specify if you want to load or unload the chunk!");
            return true;
        }

        String newValue = args[1];

        Chunk chunk = new Chunk(player.getLocation().getChunk());

        Chunk chunkWithData = team.getChunkData(chunk);

        if (chunkWithData == null){
            player.sendMessage("§cThe chunk is not claimed!");
            return true;
        }

        if (team.getLoadedChunks().size() == Main.getPlugin().getConfig().getInt(Defaults.CONFIG_MAX_LOADED_CHUNKS_PER_TEAM.getValue())){
            player.sendMessage("§cYour team already has the maximum amount of loaded chunks!");
            return true;
        }

        switch (newValue) {
            case "enable" -> {
                if (!chunkWithData.isChunkLoaded()){
                    player.sendMessage("§aSuccessfully marked the chunk to be loaded at all times!");
                    chunkWithData.setChunkLoaded(true);
                } else {
                    player.sendMessage("§cThat chunk is already marked to load at all times!");
                }
            }

            case "disable" -> {
                if (chunkWithData.isChunkLoaded()){
                    player.sendMessage("§aSuccessfully disabled loading of the chunk!");
                    chunkWithData.setChunkLoaded(false);
                } else {
                    player.sendMessage("§cThat chunk is already disabled to load!");
                }
            }

            default -> {
                player.sendMessage("§cInvalid option!");
            }
        }

        return true;
    }
}
