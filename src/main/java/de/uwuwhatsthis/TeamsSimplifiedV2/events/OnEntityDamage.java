package de.uwuwhatsthis.TeamsSimplifiedV2.events;

import de.uwuwhatsthis.TeamsSimplifiedV2.main.Main;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.Chunk;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.Team;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Arrays;

public class OnEntityDamage implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        Chunk chunk = new Chunk(event.getEntity().getLocation().getChunk());

        Team team = Main.getManager().chunkIsClaimedBy(chunk);

        if (team == null) return;

        Chunk chunkWithData = team.getChunkData(chunk);

        if (!Arrays.asList(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, EntityDamageEvent.DamageCause.BLOCK_EXPLOSION).contains(event.getCause())) return;

        if (chunkWithData.isExplosionEnabledSet()){
            if (!chunkWithData.isExplosionEnabled()){
                event.setCancelled(true);
            }

            return;
        }

        if (!team.isExplosionsEnabled()){
            event.setCancelled(true);
        }
    }
}
