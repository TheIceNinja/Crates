package net.theiceninja.crates.api.crate.managers;

import net.theiceninja.crates.api.crate.ICrate;
import org.bukkit.block.Block;

public interface ICrateManager {

    boolean isCrate(Block block);

    void saveCrate(ICrate iCrate);
    void deleteCrate(ICrate iCrate);
    void loadCrates();

}
