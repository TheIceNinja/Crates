package net.theiceninja.crates.crate.setup;

import lombok.Getter;
import net.theiceninja.crates.api.crate.ICrate;
import net.theiceninja.crates.api.crate.ISetupHandler;
import net.theiceninja.crates.crate.Crate;
import net.theiceninja.crates.crate.managers.CrateManager;
import net.theiceninja.crates.crate.setup.listeners.BlockPlaceListener;
import net.theiceninja.crates.crate.setup.listeners.DropItemsListener;
import net.theiceninja.crates.crate.setup.listeners.InteractListener;
import net.theiceninja.crates.crate.setup.listeners.QuitListener;
import net.theiceninja.utils.spigot.ItemBuilder;
import net.theiceninja.utils.spigot.handlers.PlayerRollbackHandler;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CrateSetupHandler implements ISetupHandler {

    @Getter private final Map<UUID, Crate> setup = new HashMap<>();

    @Getter private final CrateManager crateManager;
    private final PlayerRollbackHandler rollbackHandler;

    public CrateSetupHandler(CrateManager crateManager) {
        this.crateManager = crateManager;
        this.rollbackHandler = new PlayerRollbackHandler();

        Plugin plugin = crateManager.getPlugin();
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvents(new BlockPlaceListener(this), plugin);
        pluginManager.registerEvents(new InteractListener(this), plugin);
        pluginManager.registerEvents(new DropItemsListener(this), plugin);
        pluginManager.registerEvents(new QuitListener(this), plugin);
    }

    @Override
    public void addToSetup(@NotNull Player player, @NotNull ICrate iCrate) {
        Crate crate = (Crate) iCrate;
        setup.put(player.getUniqueId(), crate);
        rollbackHandler.save(player);

        player.setGameMode(GameMode.CREATIVE);
        player.getInventory().setItem(
                0,
                new ItemBuilder(Material.CHEST)
                        .setCustomModelData(0)
                        .setDisplayName(crate.getType().getPrefix())
                        .setGlow(true)
                        .setLore("תשים את זה פשוט וזהו")
                        .build()
        );

        player.getInventory().setItem(
                8,
                new ItemBuilder(Material.RED_DYE)
                        .setDisplayName("&cיציאה")
                        .setLore("פשוט תצא לול")
                        .build()
        );
        player.getInventory().setItem(
                7,
                new ItemBuilder(Material.GREEN_DYE)
                        .setDisplayName("&aאישור")
                        .setLore("סיימת הכל")
                        .build()
        );
    }

    @Override
    public void removeFromSetup(final @NotNull Player player) {
        setup.remove(player.getUniqueId());
        rollbackHandler.restore(player);
    }

    @Override
    public boolean inSetup(final @NotNull Player player) {
        return setup.containsKey(player.getUniqueId());
    }
}
