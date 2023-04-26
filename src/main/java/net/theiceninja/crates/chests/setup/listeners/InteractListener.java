package net.theiceninja.crates.chests.setup.listeners;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.chests.Chest;
import net.theiceninja.crates.chests.setup.ChestSetupHandler;
import net.theiceninja.ninjaapi.ColorUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

@RequiredArgsConstructor
public class InteractListener implements Listener {

    private final ChestSetupHandler chestSetupHandler;

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (!chestSetupHandler.inSetup(event.getPlayer())) return;
        if (!event.hasItem()) return;
        if (event.getItem() == null) return;
        if (!event.getItem().hasItemMeta()) return;

        String itemName = event.getItem().getItemMeta().getDisplayName();
        if (itemName.equals(ColorUtils.color("&cיציאה"))) {
            chestSetupHandler.getChestManager().getChestFile().reloadConfig();
            chestSetupHandler.removeFromSetup(event.getPlayer());
        } else if (itemName.equals(ColorUtils.color("&aאישור"))) {
            Chest chest = chestSetupHandler.getSetup().get(event.getPlayer().getUniqueId());
            if (chest.getChestLocation() == null) {
                event.getPlayer().sendMessage(ColorUtils.color("&cלא שמת לו מיקום."));
                return;
            }

            chestSetupHandler.getChestManager().saveChest(chest);
            chest.reload();

            chestSetupHandler.removeFromSetup(event.getPlayer());
        }
    }

}
