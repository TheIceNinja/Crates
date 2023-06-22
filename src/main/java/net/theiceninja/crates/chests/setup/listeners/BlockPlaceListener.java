package net.theiceninja.crates.chests.setup.listeners;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.chests.Chest;
import net.theiceninja.crates.chests.setup.ChestSetupHandler;
import net.theiceninja.utilitys.spigot.color.ColorUtils;
import net.theiceninja.utilitys.spigot.color.TextColor;
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
            event.getPlayer().sendMessage(ColorUtils.colorChat(
                    TextColor.ERROR,
                    "אתה צריך להניח רק תיבות שניתנו לך."
            ));
            return;
        }

        Chest chest = chestSetupHandler.getSetup().get(event.getPlayer().getUniqueId());
        chest.setChestLocation(event.getBlock().getLocation());
    }
}
