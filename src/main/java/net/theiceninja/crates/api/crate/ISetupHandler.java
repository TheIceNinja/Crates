package net.theiceninja.crates.api.crate;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface ISetupHandler {

    void addToSetup(@NotNull Player player, final @NotNull ICrate iCrate);
    void removeFromSetup(@NotNull Player player);
    boolean inSetup(@NotNull Player player);

}
