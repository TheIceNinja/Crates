package net.theiceninja.crates.chests.listeners;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.chests.managers.CrateManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

@RequiredArgsConstructor
public class BlockBreakListener implements Listener {

    private final CrateManager crateManager;

    @EventHandler
    private void onBreak(BlockBreakEvent event) {
        if (crateManager.isCrate(event.getBlock()))
            event.setCancelled(true);
    }
}
