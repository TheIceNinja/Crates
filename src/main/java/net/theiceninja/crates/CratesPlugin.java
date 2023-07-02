package net.theiceninja.crates;

import net.theiceninja.crates.chests.Crate;
import net.theiceninja.crates.chests.managers.CrateManager;
import net.theiceninja.crates.commands.CratesCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class CratesPlugin extends JavaPlugin {

    private CrateManager crateManager;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(false);
        saveDefaultConfig();

        this.crateManager = new CrateManager(this);
        getCommand("crates").setExecutor(new CratesCommand(crateManager));
        getCommand("crates").setTabCompleter(new CratesCommand(crateManager));
    }

    @Override
    public void onDisable() {
        if (crateManager.getCrateList().isEmpty()) return;

        this.crateManager.getCrateList()
                .stream()
                .filter(Objects::nonNull)
                .forEach(Crate::destroyArmorStand);
    }
}
