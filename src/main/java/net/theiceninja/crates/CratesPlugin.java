package net.theiceninja.crates;

import net.theiceninja.crates.chests.Crate;
import net.theiceninja.crates.chests.managers.CrateManager;
import net.theiceninja.crates.commands.CratesCommand;
import org.bukkit.plugin.java.JavaPlugin;

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

        this.crateManager.getCrateList().forEach(Crate::destroyArmorStand);
    }
}
