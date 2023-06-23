package net.theiceninja.crates.api.chest.managers;

import net.theiceninja.crates.api.chest.ICrate;
import org.bukkit.block.Block;

public interface ICrateManager {

    boolean isCrate(Block block);

    void saveCrate(ICrate iCrate);
    void deleteCrate(ICrate iCrate);
    void loadCrates();

}
