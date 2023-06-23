package net.theiceninja.crates.chests.setup.listeners;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.chests.setup.CrateSetupHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

@RequiredArgsConstructor
public class DropItemsListener implements Listener {

    private final CrateSetupHandler crateSetupHandler;

    @EventHandler
    private void onDrop(PlayerDropItemEvent event) {
        if (!crateSetupHandler.inSetup(event.getPlayer())) return;

        event.setCancelled(true);
    }

}
