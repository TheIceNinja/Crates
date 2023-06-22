package net.theiceninja.crates.chests.listeners;

import net.theiceninja.utilitys.spigot.color.ColorUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PlayerInventoryClickListener implements Listener {

    @EventHandler
    private void onInteract(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ColorUtils.colorString("דברים שאתה יכול לקבל")))
            event.setCancelled(true);
    }
}
