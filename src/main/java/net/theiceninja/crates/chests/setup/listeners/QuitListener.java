package net.theiceninja.crates.chests.setup.listeners;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.chests.setup.ChestSetupHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class QuitListener implements Listener {

    private final ChestSetupHandler chestSetupHandler;

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        if (!chestSetupHandler.inSetup(event.getPlayer())) return;

        chestSetupHandler.removeFromSetup(event.getPlayer());
    }
}
