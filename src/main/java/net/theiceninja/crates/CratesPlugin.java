package net.theiceninja.crates;

import net.theiceninja.crates.chests.Chest;
import net.theiceninja.crates.chests.managers.ChestManager;
import net.theiceninja.crates.commands.CratesCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class CratesPlugin extends JavaPlugin {

    private ChestManager chestManager;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(false);
        saveDefaultConfig();

        this.chestManager = new ChestManager(this);
        getCommand("crates").setExecutor(new CratesCommand(chestManager));
        getCommand("crates").setTabCompleter(new CratesCommand(chestManager));
    }

    @Override
    public void onDisable() {
        if (chestManager.getChestList().isEmpty()) return;

        this.chestManager.getChestList().forEach(Chest::destroyArmorStand);
    }
}
