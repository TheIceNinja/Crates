package net.theiceninja.crates.chests.setup.listeners;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.chests.Crate;
import net.theiceninja.crates.chests.setup.CrateSetupHandler;
import net.theiceninja.utilitys.spigot.color.ColorUtils;
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
            Crate chest = crateSetupHandler.getSetup().get(event.getPlayer().getUniqueId());
            if (chest.getChestLocation() == null) {
                event.getPlayer().sendMessage(ColorUtils.colorString("&cלא שמת לו מיקום."));
                return;
            }

            crateSetupHandler.getCrateManager().saveChest(chest);
            chest.reload();

            crateSetupHandler.removeFromSetup(event.getPlayer());
        }
    }
}
