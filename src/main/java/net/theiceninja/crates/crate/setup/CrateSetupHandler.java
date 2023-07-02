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
import net.theiceninja.utilitys.spigot.ItemBuilder;
import net.theiceninja.utilitys.spigot.PlayerRollback;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CrateSetupHandler implements ISetupHandler {

    @Getter private final Map<UUID, Crate> setup;

    @Getter private final CrateManager crateManager;
    private final PlayerRollback rollbackManager;

    public CrateSetupHandler(CrateManager chestManager) {
        this.crateManager = chestManager;
        this.setup = new HashMap<>();
        this.rollbackManager = new PlayerRollback();

        chestManager.getPlugin().getServer().getPluginManager().registerEvents(new BlockPlaceListener(this), chestManager.getPlugin());
        chestManager.getPlugin().getServer().getPluginManager().registerEvents(new InteractListener(this), chestManager.getPlugin());
        chestManager.getPlugin().getServer().getPluginManager().registerEvents(new DropItemsListener(this), chestManager.getPlugin());
        chestManager.getPlugin().getServer().getPluginManager().registerEvents(new QuitListener(this), chestManager.getPlugin());
    }

    @Override
    public void addToSetup(@NotNull Player player, @NotNull ICrate iChest) {
        Crate chest = (Crate) iChest;
        setup.put(player.getUniqueId(), chest);
        player.getInventory().clear();
        rollbackManager.save(player);

        player.setGameMode(GameMode.CREATIVE);
        player.getInventory().setItem(
                0,
                new ItemBuilder(Material.CHEST)
                        .setCustomModelData(0)
                        .setDisplayName(chest.getType().getPrefix())
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
        player.getInventory().clear();
        rollbackManager.restore(player);
    }

    @Override
    public boolean inSetup(final @NotNull Player player) {
        return setup.containsKey(player.getUniqueId());
    }
}
