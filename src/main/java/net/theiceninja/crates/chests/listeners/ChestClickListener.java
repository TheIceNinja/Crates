package net.theiceninja.crates.chests.listeners;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.chests.Chest;
import net.theiceninja.crates.chests.managers.ChestManager;
import net.theiceninja.ninjaapi.ColorUtils;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

@RequiredArgsConstructor
public class ChestClickListener implements Listener {

    private final ChestManager chestManager;

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;

        Block block = event.getClickedBlock();
        if (!chestManager.isChest(block)) return;

        Chest chest = chestManager.getChest(block);
        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            event.getPlayer().openInventory(chest.getInventory());
        } else if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            event.setUseInteractedBlock(Event.Result.DENY);
            if (!event.hasItem()) {
                chest.cancel(event.getPlayer(), "&#E81E33אתה צריך מפתח לתיבה הזאת!");
                return;
            }
            if (event.getItem() == null) return;
            if (!event.getItem().hasItemMeta()) return;

            String itemName = event.getItem().getItemMeta().getDisplayName();
            if (itemName.contains(ColorUtils.color(chest.getChestType().getPrefix()))) {
                event.setUseInteractedBlock(Event.Result.DENY);
                chest.openChest(event.getPlayer());
            } else {
                event.setUseInteractedBlock(Event.Result.DENY);
                chest.cancel(event.getPlayer(), "&#E81E33אתה צריך את המפתח התקין לתיבה הזאת");
            }
        }
    }
}
