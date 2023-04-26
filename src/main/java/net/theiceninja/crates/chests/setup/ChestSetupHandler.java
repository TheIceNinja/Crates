package net.theiceninja.crates.chests.setup;

import lombok.Getter;
import net.theiceninja.crates.api.chest.IChest;
import net.theiceninja.crates.api.chest.ISetupHandler;
import net.theiceninja.crates.chests.Chest;
import net.theiceninja.crates.chests.managers.ChestManager;
import net.theiceninja.crates.chests.setup.listeners.BlockPlaceListener;
import net.theiceninja.crates.chests.setup.listeners.DropItemsListener;
import net.theiceninja.crates.chests.setup.listeners.InteractListener;
import net.theiceninja.crates.chests.setup.listeners.QuitListener;
import net.theiceninja.ninjaapi.ItemBuilder;
import net.theiceninja.ninjaapi.PlayerRollbackManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChestSetupHandler implements ISetupHandler {

    @Getter private final Map<UUID, Chest> setup;
    @Getter private final ChestManager chestManager;
    private final PlayerRollbackManager rollbackManager;

    public ChestSetupHandler(ChestManager chestManager) {
        this.chestManager = chestManager;
        this.setup = new HashMap<>();
        this.rollbackManager = new PlayerRollbackManager();

        chestManager.getPlugin().getServer().getPluginManager().registerEvents(new BlockPlaceListener(this), chestManager.getPlugin());
        chestManager.getPlugin().getServer().getPluginManager().registerEvents(new InteractListener(this), chestManager.getPlugin());
        chestManager.getPlugin().getServer().getPluginManager().registerEvents(new DropItemsListener(this), chestManager.getPlugin());
        chestManager.getPlugin().getServer().getPluginManager().registerEvents(new QuitListener(this), chestManager.getPlugin());
    }

    @Override
    public void addToSetup(@NotNull Player player, @NotNull IChest iChest) {
        Chest chest = (Chest) iChest;
        setup.put(player.getUniqueId(), chest);
        rollbackManager.save(player);

        player.setGameMode(GameMode.CREATIVE);
        player.getInventory().setItem(
                0,
                new ItemBuilder(Material.CHEST)
                        .setCustomModelData(0)
                        .setDisplayName(chest.getChestType().getPrefix())
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
        rollbackManager.restore(player);
    }

    @Override
    public boolean inSetup(final @NotNull Player player) {
        return setup.containsKey(player.getUniqueId());
    }
}
