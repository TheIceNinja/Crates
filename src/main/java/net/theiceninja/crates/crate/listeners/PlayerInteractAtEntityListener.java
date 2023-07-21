package net.theiceninja.crates.crate.listeners;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.crate.managers.CrateManager;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class PlayerInteractAtEntityListener implements Listener {

    private final CrateManager crateManager;

    @EventHandler
    private void onInteractAtEntity(PlayerInteractAtEntityEvent event) {
        if (!(event.getRightClicked() instanceof ArmorStand armorStand)) return;
        if (!isCrateArmorStand(armorStand)) return;

        event.setCancelled(true);
    }

    private boolean isCrateArmorStand(@NotNull ArmorStand armorStand) {
        return crateManager.getCrateList()
                .stream()
                .anyMatch(crate -> crate.getDisplayItemArmorStand() == armorStand || crate.getDisplayNameArmorStand() == armorStand);
    }

}
