package net.theiceninja.crates.chests.setup.listeners;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.chests.Chest;
import net.theiceninja.crates.chests.setup.ChestSetupHandler;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

@RequiredArgsConstructor
public class BlockPlaceListener implements Listener {

    private final ChestSetupHandler chestSetupHandler;

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        if (!chestSetupHandler.inSetup(event.getPlayer())) return;
        if (event.getBlock().getType() != Material.CHEST) {
            event.setBuild(false);
            return;
        }

        Chest chest = chestSetupHandler.getSetup().get(event.getPlayer().getUniqueId());
        chest.setChestLocation(event.getBlock().getLocation());
    }
}
