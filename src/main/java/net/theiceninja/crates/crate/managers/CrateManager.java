package net.theiceninja.crates.crate.managers;

import lombok.Getter;
import net.theiceninja.crates.CratesPlugin;
import net.theiceninja.crates.api.crate.CrateType;
import net.theiceninja.crates.api.crate.ICrate;
import net.theiceninja.crates.api.crate.managers.ICrateManager;
import net.theiceninja.crates.crate.Crate;
import net.theiceninja.crates.crate.listeners.BlockBreakListener;
import net.theiceninja.crates.crate.listeners.BlockPlaceListener;
import net.theiceninja.crates.crate.listeners.CrateClickListener;
import net.theiceninja.crates.crate.listeners.InventoryClickListener;
import net.theiceninja.crates.crate.setup.CrateSetupHandler;
import net.theiceninja.utilitys.spigot.config.ConfigurationFile;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
        plugin.getServer().getPluginManager().registerEvents(new InventoryClickListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new BlockBreakListener(this), plugin);
        plugin.getServer().getPluginManager().registerEvents(new BlockPlaceListener(), plugin);
    }

    public Crate getCrateFromBlock(@NotNull Block block) {
        return crateList.stream().filter(crate -> crate.getLocation().getBlock().equals(block)).iterator().next();
    }

    public Optional<Crate> findCrateById(int id) {
        if (crateList.isEmpty()) return Optional.empty();

        return crateList.stream().filter(existing -> existing.getId() == id).findAny();
    }

    @Override
    public boolean isCrate(@NotNull Block block) {
        if (crateList.isEmpty()) return false;

        return crateList.stream().anyMatch(crate -> crate.getLocation().getBlock().equals(block));
    }

    @Override
    public void saveCrate(ICrate iCrate) {
        Crate crate = (Crate) iCrate;
        cratesFile.get().set("crates." + crate.getId() + ".id", crate.getId());
        cratesFile.get().set("crates." + crate.getId() + ".type", crate.getType().toString());
        cratesFile.setLocation("crates." + crate.getId() + ".location", crate.getLocation());

        crateList.add(crate);
        cratesFile.save();
        crate.reload();
    }

    @Override
    public void deleteCrate(ICrate iCrate) {
        Crate crate = (Crate) iCrate;
        crate.delete();
        crateList.removeIf(existing -> existing.equals(crate));
        cratesFile.get().set("chests." + crate.getId(), null);

        cratesFile.save();
    }

    @Override
    public void loadCrates() {
        if (cratesFile.get().getConfigurationSection("crates") == null) return;

        for (String crateID : cratesFile.get().getConfigurationSection("crates").getKeys(false)) {
            final ConfigurationSection crateSection = cratesFile.get().getConfigurationSection("crates." + crateID);
            if (crateSection == null) break;

            final Set<ItemStack> items = new HashSet<>();
            if (crateSection.getConfigurationSection("items") != null)
                crateSection.getConfigurationSection("items").getKeys(false).forEach(item -> {
                    ItemStack itemStack = crateSection.getItemStack("items." + item);

                    items.add(itemStack);
                });

            Crate crate = new Crate(
                    crateSection.getInt("id"),
                    CrateType.valueOf(crateSection.getString("type")),
                    cratesFile.readLocation("crates." + crateID + ".location"),
                    items,
                    this
            );
            crateList.add(crate);

            plugin.getLogger().info(
                    "Loading crate: " + crate.getId() + " " +
                            "Rarity: " + crate.getType().name().toUpperCase() + " " +
                            "Items: " + Arrays.toString(crate.getItems().toArray())
            );
        }
    }
}
