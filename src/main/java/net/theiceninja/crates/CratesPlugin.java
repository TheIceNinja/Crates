package net.theiceninja.crates;

import net.theiceninja.crates.crate.Crate;
import net.theiceninja.crates.crate.managers.CrateManager;
import net.theiceninja.crates.commands.CratesCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class CratesPlugin extends JavaPlugin {

    private CrateManager crateManager;

    @Override
    public void onEnable() {
        this.crateManager = new CrateManager(this);

        CratesCommand cratesCommand = new CratesCommand(crateManager);
        getCommand("crates").setExecutor(cratesCommand);
        getCommand("crates").setTabCompleter(cratesCommand);
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
