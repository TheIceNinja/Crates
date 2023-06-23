package net.theiceninja.crates.chests.listeners;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.chests.Crate;
import net.theiceninja.crates.chests.managers.CrateManager;
import net.theiceninja.utilitys.spigot.color.ColorUtils;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

@RequiredArgsConstructor
public class CrateClickListener implements Listener {

    private final CrateManager crateManager;

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;

        Block block = event.getClickedBlock();
        if (!crateManager.isChest(block)) return;

        Crate chest = crateManager.getChest(block);
        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            event.getPlayer().openInventory(chest.getInventory());
        } else if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            event.setUseInteractedBlock(Event.Result.DENY);
            if (!event.hasItem()) {
                chest.cancelClick(event.getPlayer(), "&#E81E33אתה צריך מפתח לתיבה הזאת!");
                return;
            }

            if (event.getItem() == null) return;
            if (!event.getItem().hasItemMeta()) return;
            if (event.getItem().getItemMeta() == null) return;

            String itemName = event.getItem().getItemMeta().getDisplayName();
            if (itemName.contains(ColorUtils.colorString(chest.getType().getPrefix()))) {
                event.setUseInteractedBlock(Event.Result.DENY);
                chest.open(event.getPlayer());
            } else {
                event.setUseInteractedBlock(Event.Result.DENY);
                chest.cancelClick(
                        event.getPlayer(),
                        "&#E81E33אתה צריך את המפתח התקין לתיבה הזאת"
                );
            }
        }
    }
}
