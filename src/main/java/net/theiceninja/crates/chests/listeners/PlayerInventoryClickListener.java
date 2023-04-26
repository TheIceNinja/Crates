package net.theiceninja.crates.chests.listeners;

import net.theiceninja.ninjaapi.ColorUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PlayerInventoryClickListener implements Listener {

    @EventHandler
    private void onInteract(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ColorUtils.color("דברים שאתה יכול לקבל")))
            event.setCancelled(true);
    }
}
