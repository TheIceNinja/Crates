package net.theiceninja.crates.api.chest;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface IChest {

    void reload();
    void addItem(@NotNull ItemStack item);
    void removeItem(int index);
    void destroyArmorStand();
    void delete();

    void open(@NotNull Player player);
    void cancelClick(@NotNull Player player, @NotNull String errorMessage);
    boolean isOpen();
    void setOpen(boolean open);

    void gamble(@NotNull Player player);
    void reset();

}
