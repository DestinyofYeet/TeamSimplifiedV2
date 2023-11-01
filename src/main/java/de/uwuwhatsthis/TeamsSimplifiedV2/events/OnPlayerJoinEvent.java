package de.uwuwhatsthis.TeamsSimplifiedV2.events;

import de.uwuwhatsthis.TeamsSimplifiedV2.main.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnPlayerJoinEvent implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event){
        Main.getManager().updateDisplayName(event.getPlayer());
    }
}
