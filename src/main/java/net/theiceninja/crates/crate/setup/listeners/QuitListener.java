package net.theiceninja.crates.crate.setup.listeners;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.crate.setup.CrateSetupHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class QuitListener implements Listener {

    private final CrateSetupHandler crateSetupHandler;

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        if (!crateSetupHandler.inSetup(event.getPlayer())) return;

        crateSetupHandler.removeFromSetup(event.getPlayer());
    }
}
