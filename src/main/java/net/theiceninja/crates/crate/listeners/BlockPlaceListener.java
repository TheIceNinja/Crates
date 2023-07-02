package net.theiceninja.crates.crate.listeners;

import net.theiceninja.utilitys.spigot.color.ColorUtils;
import net.theiceninja.utilitys.spigot.color.TextColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

    @EventHandler
    private void onPlace(BlockPlaceEvent event) {
        if (event.getItemInHand().getType() == Material.AIR) return;
        if (!event.getItemInHand().hasItemMeta()) return;
        if (!event.getItemInHand().getItemMeta().hasDisplayName()) return;

        if (event.getItemInHand().getItemMeta().getDisplayName().contains("מפתח")) {
            event.setBuild(false);
            event.getPlayer().sendMessage(ColorUtils.colorChat(
                    TextColor.AQUA,
                    "אתה לא יכול לשים מפתח על הרצפה!"
            ));
        }
    }

}
