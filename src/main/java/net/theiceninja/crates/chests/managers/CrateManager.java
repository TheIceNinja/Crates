package net.theiceninja.crates.chests.managers;

import lombok.Getter;
import net.theiceninja.crates.CratesPlugin;
import net.theiceninja.crates.api.chest.CrateType;
import net.theiceninja.crates.api.chest.ICrate;
import net.theiceninja.crates.api.chest.managers.ICrateManager;
import net.theiceninja.crates.chests.Crate;
import net.theiceninja.crates.chests.listeners.BlockBreakListener;
import net.theiceninja.crates.chests.listeners.BlockPlaceListener;
import net.theiceninja.crates.chests.listeners.CrateClickListener;
import net.theiceninja.crates.chests.listeners.PlayerInventoryClickListener;
import net.theiceninja.crates.chests.setup.CrateSetupHandler;
import net.theiceninja.utilitys.spigot.LocationUtility;
import net.theiceninja.utilitys.spigot.config.ConfigurationFile;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter
public class CrateManager implements ICrateManager {

    private final Set<Crate> crateList = new HashSet<>();

    private final ConfigurationFile cratesFile;

    private final CrateSetupHandler crateSetupHandler;
    private final CratesPlugin plugin;

    public CrateManager(CratesPlugin plugin) {
        this.plugin = plugin;
        this.cratesFile = new ConfigurationFile(plugin, "crates");
        this.crateSetupHandler = new CrateSetupHandler(this);
        loadCrates();

        plugin.getServer().getPluginManager().registerEvents(new CrateClickListener(this), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerInventoryClickListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new BlockBreakListener(this), plugin);
        plugin.getServer().getPluginManager().registerEvents(new BlockPlaceListener(), plugin);
    }

    public Crate getCrate(@NotNull Block block) {
        return crateList.stream().filter(chest -> chest.getChestLocation().getBlock().equals(block)).iterator().next();
    }

    public Optional<Crate> findCrate(int id) {
        if (crateList.isEmpty()) return Optional.empty();

        return crateList.stream().filter(existing -> existing.getId() == id).findAny();
    }

    @Override
    public boolean isCrate(@NotNull Block block) {
        if (crateList.isEmpty()) return false;
        return crateList.stream().anyMatch(chest -> chest.getChestLocation().getBlock().equals(block));
    }

    @Override
    public void saveCrate(ICrate iChest) {
        Crate chest = (Crate) iChest;
        cratesFile.get().set("chests." + chest.getId() + ".id", chest.getId());
        cratesFile.get().set("chests." + chest.getId() + ".type", chest.getType().toString());
        LocationUtility.setLocation(
                chest.getChestLocation(),
                cratesFile.get().createSection("chests." + chest.getId() + ".location")
        );

        crateList.add(chest);
        cratesFile.save();
    }

    @Override
    public void deleteCrate(ICrate iChest) {
        Crate chest = (Crate) iChest;
        chest.delete();
        crateList.removeIf(existing -> existing.equals(chest));
        cratesFile.get().set("chests." + chest.getId(), null);

        cratesFile.save();
    }

    @Override
    public void loadCrates() {
        if (cratesFile.get().getConfigurationSection("chests") == null) return;

        for (String chestID : cratesFile.get().getConfigurationSection("chests").getKeys(false)) {
            final ConfigurationSection chestSection = cratesFile.get().getConfigurationSection("chests." + chestID);
            if (chestSection == null) break;

            final Set<ItemStack> items = new HashSet<>();
            if (chestSection.getConfigurationSection("items") != null)
                chestSection.getConfigurationSection("items").getKeys(false).forEach(item -> {
                    ItemStack itemStack = chestSection.getItemStack("items." + item);

                    items.add(itemStack);
                });

            Crate chest = new Crate(
                    chestSection.getInt("id"),
                    CrateType.valueOf(chestSection.getString("type")),
                    LocationUtility.readLocation(chestSection.getConfigurationSection("location")),
                    items,
                    this
            );
            crateList.add(chest);

            plugin.getLogger().info(
                    "Loading chest: " + chest.getId() + " " +
                            "Rarity: " + chest.getType() + " " +
                            "Items: " + Arrays.toString(chest.getItems().toArray())
            );
        }
    }
}
