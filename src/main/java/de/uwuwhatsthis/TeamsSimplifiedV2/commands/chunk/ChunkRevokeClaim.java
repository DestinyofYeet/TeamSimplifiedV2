package de.uwuwhatsthis.TeamsSimplifiedV2.commands.chunk;

import de.uwuwhatsthis.TeamsSimplifiedV2.main.Main;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.Chunk;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.Team;
import de.uwuwhatsthis.TeamsSimplifiedV2.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChunkRevokeClaim {

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!Utils.isPlayer(sender)) return true;

        if (!sender.hasPermission("TeamsSimplifiedV2.chunk.removeclaim")){
            sender.sendMessage("§cInsufficient permissions!");
            return true;
        }

        Player player = (Player) sender;

        Chunk chunk = new Chunk(player.getLocation().getChunk());

        Team team = Main.getManager().chunkIsClaimedBy(chunk);

        if (team == null){
            player.sendMessage("§cThat chunk is not claimed!");
            return true;
        }

        Main.getManager().unclaimChunk(team, chunk);
        player.sendMessage("§aSuccessfully unclaimed the chunk!");
        return true;
    }
}
