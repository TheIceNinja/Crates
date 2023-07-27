package net.theiceninja.crates.crate.listeners;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.crate.Crate;
import net.theiceninja.crates.crate.managers.CrateManager;
import net.theiceninja.utils.spigot.color.ColorUtils;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

@RequiredArgsConstructor
public class CrateClickListener implements Listener {

    private final CrateManager crateManager;

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (!crateManager.isCrate(event.getClickedBlock())) return;

        Crate crate = crateManager.getCrateFromBlock(event.getClickedBlock());
        switch (event.getAction()) {
            case LEFT_CLICK_BLOCK -> event.getPlayer().openInventory(crate.getInventory());
            case RIGHT_CLICK_BLOCK -> {
                event.setUseInteractedBlock(Event.Result.DENY);
                if (!event.hasItem()) {
                    crate.cancelClick(event.getPlayer(), "&#E81E33אתה צריך מפתח לתיבה הזאת!");
                    return;
                }

                if (event.getItem() == null) return;
                if (!event.getItem().hasItemMeta()) {
                    crate.cancelClick(
                            event.getPlayer(),
                            "&#E81E33אתה צריך קודם שיהיה לך מפתח לפני שאתה מנסה לפתוח את התיבה!"
                    );
                    return;
                }
                if (event.getItem().getItemMeta() == null) return;

                String itemName = event.getItem().getItemMeta().getDisplayName();
                if (itemName.contains(ColorUtils.colorString(crate.getType().getPrefix())))
                    crate.open(event.getPlayer());
                else
                    crate.cancelClick(
                            event.getPlayer(),
                            "&#E81E33אתה צריך את המפתח התקין לתיבה הזאת"
                    );
            }
        }
    }
}
