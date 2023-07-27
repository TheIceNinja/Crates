package net.theiceninja.crates.crate.setup.listeners;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.crate.Crate;
import net.theiceninja.crates.crate.setup.CrateSetupHandler;
import net.theiceninja.utils.spigot.color.ColorUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

@RequiredArgsConstructor
public class InteractListener implements Listener {

    private final CrateSetupHandler crateSetupHandler;

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (!crateSetupHandler.inSetup(event.getPlayer())) return;
        if (!event.hasItem()) return;
        if (event.getItem() == null) return;
        if (!event.getItem().hasItemMeta()) return;
        if (event.getItem().getItemMeta() == null) return;

        String itemName = event.getItem().getItemMeta().getDisplayName();
        if (itemName.equals(ColorUtils.colorString("&cיציאה"))) {
            crateSetupHandler.getCrateManager().getCratesFile().reloadConfig();
            crateSetupHandler.removeFromSetup(event.getPlayer());
        } else if (itemName.equals(ColorUtils.colorString("&aאישור"))) {
            Crate crate = crateSetupHandler.getSetup().get(event.getPlayer().getUniqueId());
            if (crate.getLocation() == null) {
                event.getPlayer().sendMessage(ColorUtils.colorString("&cלא שמת לו מיקום."));
                return;
            }

            crateSetupHandler.getCrateManager().saveCrate(crate);
            crateSetupHandler.removeFromSetup(event.getPlayer());
        }
    }
}
