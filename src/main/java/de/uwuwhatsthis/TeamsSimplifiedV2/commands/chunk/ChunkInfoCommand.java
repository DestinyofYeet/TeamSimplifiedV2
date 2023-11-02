package de.uwuwhatsthis.TeamsSimplifiedV2.commands.chunk;

import de.uwuwhatsthis.TeamsSimplifiedV2.main.Main;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.Team;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.TeamManager;
import de.uwuwhatsthis.TeamsSimplifiedV2.utils.Utils;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChunkInfoCommand {

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!Utils.isPlayer(sender)){
            return true;
        }

        Player player = (Player) sender;

        TeamManager manager = Main.getManager();

        Team playerTeam = manager.getTeamByPlayer(player);


        if (!player.hasPermission("TeamsSimplifiedV2.chunk.info")){
            player.sendMessage("§cInsufficient permissions!");
            return true;
        }

        Chunk chunk = player.getLocation().getChunk();

        de.uwuwhatsthis.TeamsSimplifiedV2.teams.Chunk wantedChunk = new de.uwuwhatsthis.TeamsSimplifiedV2.teams.Chunk(chunk.getX(), chunk.getZ(), chunk.getWorld().getUID());

        Team team = Main.getManager().chunkIsClaimedBy(wantedChunk);

        StringBuilder builder = new StringBuilder();

        builder.append("§6Chunk coordinates: §a").append(chunk.getX()).append(" ").append(chunk.getZ()).append("§r\n")
                .append("§6Chunk claimed: §a").append(team == null ? "No" : "Yes: " + team.getName()).append("§r");

        if (playerTeam != null){
            if (playerTeam.compareTo(team) == 0){

                de.uwuwhatsthis.TeamsSimplifiedV2.teams.Chunk chunkWithData = team.getChunkData(wantedChunk);

                builder.append("\n§6Explosions enabled: §a").append(chunkWithData.isExplosionEnabled() ? "Yes": "No");
                builder.append("\n§6Chunkloaded: §a").append(chunkWithData.isChunkLoaded() ? "Yes": "No");
            }
        }

        player.sendMessage(builder.toString());
        return true;
    }
}
