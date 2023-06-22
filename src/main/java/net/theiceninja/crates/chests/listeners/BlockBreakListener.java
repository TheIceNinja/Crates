package net.theiceninja.crates.chests.listeners;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.chests.managers.ChestManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

@RequiredArgsConstructor
public class BlockBreakListener implements Listener {

    private final ChestManager chestManager;

    @EventHandler
    private void onBreak(BlockBreakEvent event) {
        if (chestManager.isChest(event.getBlock()))
            event.setCancelled(true);
    }
}
