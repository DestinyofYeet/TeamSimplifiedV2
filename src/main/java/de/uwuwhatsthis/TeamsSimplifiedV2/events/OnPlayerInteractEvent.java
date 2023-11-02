package de.uwuwhatsthis.TeamsSimplifiedV2.events;

import de.uwuwhatsthis.TeamsSimplifiedV2.main.Main;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.Chunk;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.Team;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class OnPlayerInteractEvent implements Listener {

    private static final Set<Material> usableBlocks = new HashSet<Material>(Arrays.asList(
            Material.ENDER_CHEST,
            Material.CRAFTING_TABLE
    ));

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event){
        Player player = event.getPlayer();

        if (event.getClickedBlock() == null) return;

        Chunk chunk = new Chunk(event.getClickedBlock().getLocation().getChunk());

        if (usableBlocks.contains(event.getClickedBlock().getType())) return;

        Team claimedChunkTeam = Main.getManager().chunkIsClaimedBy(chunk);

        if (claimedChunkTeam == null) return; // chunk is not claimed

        if (claimedChunkTeam.isPlayerPartOfTeam(player)) return;

        if (player.hasPermission("TeamsSimplifiedV2.chunk.overrideaccess")){
            player.sendMessage("§aSuccessfully overrode chunk access!");
            return;
        }

        player.sendMessage("§cYou are not part of the team that claimed this chunk!");
        event.setCancelled(true);

    }
}
