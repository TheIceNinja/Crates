package net.theiceninja.crates.crate.setup.listeners;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.crate.Crate;
import net.theiceninja.crates.crate.setup.CrateSetupHandler;
import net.theiceninja.utils.spigot.color.ColorUtils;
import net.theiceninja.utils.spigot.color.TextColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

@RequiredArgsConstructor
public class BlockPlaceListener implements Listener {

    private final CrateSetupHandler crateSetupHandler;

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        if (!crateSetupHandler.inSetup(event.getPlayer())) return;
        if (event.getBlock().getType() != Material.CHEST) {
            event.setBuild(false);
            event.getPlayer().sendMessage(ColorUtils.colorChat(
                    TextColor.ERROR,
                    "אתה צריך להניח רק תיבות שניתנו לך."
            ));
            return;
        }

        Crate crate = crateSetupHandler.getSetup().get(event.getPlayer().getUniqueId());
        crate.setLocation(event.getBlock().getLocation());
    }
}
