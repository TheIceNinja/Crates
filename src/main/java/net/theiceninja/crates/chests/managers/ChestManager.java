package net.theiceninja.crates.chests.managers;

import lombok.Getter;
import net.theiceninja.crates.CratesPlugin;
import net.theiceninja.crates.api.chest.ChestType;
import net.theiceninja.crates.api.chest.IChest;
import net.theiceninja.crates.api.chest.managers.IChestManager;
import net.theiceninja.crates.chests.Chest;
import net.theiceninja.crates.chests.listeners.BlockBreakListener;
import net.theiceninja.crates.chests.listeners.ChestClickListener;
import net.theiceninja.crates.chests.listeners.PlayerInventoryClickListener;
import net.theiceninja.crates.chests.setup.ChestSetupHandler;
import net.theiceninja.crates.utils.LocationUtility;
import net.theiceninja.ninjaapi.ConfigurationFile;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter public class ChestManager implements IChestManager {

    private final List<Chest> chestList = new ArrayList<>();

    private final ConfigurationFile chestFile;

    private final ChestSetupHandler chestSetupHandler;
    private final CratesPlugin plugin;

    public ChestManager(CratesPlugin plugin) {
        this.plugin = plugin;
        this.chestFile = new ConfigurationFile(plugin, "chests");
        this.chestSetupHandler = new ChestSetupHandler(this);

        if (chestFile.get().getConfigurationSection("chests") == null) return;
        loadChests();

        plugin.getServer().getPluginManager().registerEvents(new ChestClickListener(this), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerInventoryClickListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new BlockBreakListener(this), plugin);
    }

    public Chest getChest(Block block) {
        return chestList.stream().filter(chest -> chest.getChestLocation().getBlock().equals(block)).iterator().next();
    }

    public Optional<Chest> findChest(int id) {
        if (chestList.isEmpty()) return Optional.empty();

        return chestList.stream().filter(existing -> existing.getId() == id).findAny();
    }

    @Override
    public boolean isChest(Block block) {
        return chestList.stream().anyMatch(chest -> chest.getChestLocation().getBlock().equals(block));
    }

    @Override
    public void saveChest(IChest iChest) {
        Chest chest = (Chest) iChest;
        chestFile.get().set("chests." + chest.getId() + ".id", chest.getId());
        chestFile.get().set("chests." + chest.getId() + ".type", chest.getChestType().toString());
        LocationUtility.saveLocation(
                chest.getChestLocation(),
                chestFile.get().createSection("chests." + chest.getId() + ".location")
        );

        chestList.add(chest);
        chestFile.save();
    }

    @Override
    public void deleteChest(IChest iChest) {
        Chest chest = (Chest) iChest;
        chest.deleteChest();
        chestList.removeIf(existing -> existing.equals(chest));
        chestFile.get().set("chests." + chest.getId(), null);

        chest.getChestLocation().getBlock().setType(Material.AIR);

        chestFile.save();
    }

    @Override
    public void loadChests() {
        for (String chestID : chestFile.get().getConfigurationSection("chests").getKeys(false)) {
            final ConfigurationSection chestSection = chestFile.get().getConfigurationSection("chests." + chestID);
            if (chestSection == null) break;

            final List<ItemStack> items = new ArrayList<>();
            if (chestSection.getConfigurationSection("items") != null)
                chestSection.getConfigurationSection("items").getKeys(false).forEach(item -> {
                    ItemStack itemStack = chestSection.getItemStack("items." + item);

                    items.add(itemStack);
                });

            Chest chest = new Chest(
                    chestSection.getInt("id"),
                    ChestType.valueOf(chestSection.getString("type")),
                    LocationUtility.readLocation(chestSection.getConfigurationSection("location")),
                    items,
                    this
            );

            System.out.println(chest.getChestLocation());

            chestList.add(chest);
        }
    }
}
