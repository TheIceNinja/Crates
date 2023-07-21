package net.theiceninja.crates.crate.listeners;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.crate.managers.CrateManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

@RequiredArgsConstructor
public class EntityExplodeListener implements Listener {

    private final CrateManager crateManager;

    @EventHandler
    private void onEntityExplode(EntityExplodeEvent event) {
        event.blockList().forEach(block -> {
            if (!crateManager.isCrate(block)) return;

            event.setCancelled(true);
        });
    }

}
