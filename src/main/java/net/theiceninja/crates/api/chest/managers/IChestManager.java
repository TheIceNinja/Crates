package net.theiceninja.crates.api.chest.managers;

import net.theiceninja.crates.api.chest.IChest;
import org.bukkit.block.Block;

public interface IChestManager {

    boolean isChest(Block block);

    void saveChest(IChest iChest);
    void deleteChest(IChest iChest);
    void loadChests();

}
