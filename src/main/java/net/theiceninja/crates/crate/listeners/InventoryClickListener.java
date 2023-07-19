package net.theiceninja.crates.crate.listeners;

import net.theiceninja.utilitys.spigot.color.ColorUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    @EventHandler
    private void onInteract(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ColorUtils.colorString("דברים שאתה יכול לקבל")))
            event.setCancelled(true);
    }
}
