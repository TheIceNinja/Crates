package net.theiceninja.crates.api.chest.managers;

import net.theiceninja.crates.api.chest.ICrate;
import org.bukkit.block.Block;

public interface ICrateManager {

    boolean isChest(Block block);

    void saveChest(ICrate iCrate);
    void deleteChest(ICrate iCrate);
    void loadChests();

}
