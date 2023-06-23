package net.theiceninja.crates.chests;

import lombok.Getter;
import lombok.Setter;
import net.theiceninja.crates.api.chest.ChestType;
import net.theiceninja.crates.api.chest.IChest;
import net.theiceninja.crates.chests.managers.ChestManager;
import net.theiceninja.crates.chests.tasks.GambleTask;
import net.theiceninja.utilitys.java.NumberUtils;
import net.theiceninja.utilitys.spigot.color.ColorUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter
public class Chest implements IChest {

    private final int id;
    private final ChestType type;
    private boolean open = false;

    private ArmorStand
            displayItemArmorStand,
            displayNameArmorStand;
    private GambleTask gambleTask;

    @Setter private Location chestLocation;
    private final Set<ItemStack> items;

    private final ChestManager chestManager;

    // loading from configuration file
    public Chest(int id, ChestType chestType, Location chestLocation, Set<ItemStack> items, ChestManager chestManager) {
        this.id = id;
        this.type = chestType;
        this.items = items;
        this.chestLocation = chestLocation;
        this.chestManager = chestManager;

        setupArmorStands();
    }

    // creating the chest
    public Chest(int id, ChestType chestType, ChestManager chestManager) {
        this.id = id;
        this.type = chestType;
        this.chestManager = chestManager;
        this.items = new HashSet<>();
    }

    public Inventory getInventory() {
        Inventory itemsMenu = chestManager.getPlugin().getServer().createInventory(
                null,
                27,
                "דברים שאתה יכול לקבל"
        );
        for (int i = 0; i < items.size(); i++) {
            ItemStack item = items.stream().toList().get(i);
            itemsMenu.addItem(item);
        }

        return itemsMenu;
    }

    @Override
    public void addItem(@NotNull ItemStack item) {
        if (item.getItemMeta() == null) return;

        String key = item.getItemMeta().getDisplayName();
        if (key.isEmpty()) {
            key = UUID.randomUUID().toString();
        }

        chestManager.getChestFile().get().set("chests." + id + ".items." + key, item);
        chestManager.getChestFile().save();

        items.add(item);
    }

    @Override
    public void removeItem(int index) {
        ItemStack item = items.stream().toList().get(index);
        items.removeIf(existing -> existing.equals(item));
        if (item.getItemMeta() == null) return;

        chestManager.getChestFile().get().set(
                "chests." + id + ".items." + item.getItemMeta().getDisplayName()
                , null
        );
        chestManager.getChestFile().save();
    }

    private void setupArmorStands() {
        if (chestLocation.getWorld() == null) return;

        this.displayNameArmorStand = applyArmorStandProperties(
                chestLocation.add(0.43, -0.9, 0.5),
                "&#F3ED13⭐ " + type.getPrefix() + " &#F3ED13⭐"
        );
        this.displayNameArmorStand.setCustomNameVisible(true);

        this.displayItemArmorStand = applyArmorStandProperties(
                chestLocation.add(0.1, 1.8, 0),
                "&r"
        );
        this.displayItemArmorStand.setCustomNameVisible(false);
        this.displayItemArmorStand.setSmall(true);
        this.displayItemArmorStand.setRotation(-80, 0);
    }

    private ArmorStand applyArmorStandProperties(@NotNull Location spawnLocation, @NotNull String displayName) {
        ArmorStand armorStand = (ArmorStand) chestManager
                .getPlugin()
                .getServer()
                .getWorld(spawnLocation.getWorld().getName())
                .spawnEntity(
                        spawnLocation,
                        EntityType.ARMOR_STAND
                );

        armorStand.setGravity(false);
        armorStand.setArms(false);
        armorStand.setVisible(false);
        armorStand.setInvulnerable(true);
        armorStand.setCustomName(ColorUtils.colorString(displayName));
        return armorStand;
    }

    @Override
    public void reload() {
        destroyArmorStand();
        setupArmorStands();
    }

    @Override
    public void destroyArmorStand() {
        if (this.displayNameArmorStand != null) this.displayNameArmorStand.remove();
        if (this.displayItemArmorStand != null) this.displayItemArmorStand.remove();
    }

    @Override
    public void delete() {
        destroyArmorStand();
    }

    @Override
    public void open(@NotNull Player player) {
        if (isOpen()) {
            cancelClick(player, "&#E81E33יש מישהו כרגע שפותח, אנא המתן.");
            return;
        }

        if (items.isEmpty()) {
            cancelClick(player, "&#E81E33אין דברים בתיבה הזאת.");
            return;
        }

        open = true;
        this.displayItemArmorStand.setCustomNameVisible(true);

        ItemStack item = player.getInventory().getItemInMainHand();
        item.setAmount(item.getAmount() - 1);
        gamble(player);
        player.sendMessage(ColorUtils.colorString("&aפותח לך את התיבה!"));
    }

    @Override
    public void cancelClick(@NotNull Player player, @NotNull String errorMessage) {
        player.setVelocity(player.getLocation().getDirection().multiply(-0.5));
        player.sendMessage(ColorUtils.colorString(errorMessage));
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1);
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public void setOpen(boolean open) {
        this.open = open;
    }

    @Override
    public void gamble(@NotNull Player player) {
        int randomNumber = (int) NumberUtils.randomizer(0, items.size() - 1);
        if (this.gambleTask != null) this.gambleTask.cancel();

        this.gambleTask = new GambleTask(
                player.getUniqueId(),
                randomNumber,
                items.size() - 1,
                this
        );
        this.gambleTask.runTaskTimer(chestManager.getPlugin(), 0, 16);
    }

    @Override
    public void reset() {
        this.displayItemArmorStand.setCustomNameVisible(false);
        if (this.displayItemArmorStand.getEquipment() == null) return;

        this.displayItemArmorStand.getEquipment().setHelmet(null);
        open = false;
    }
}