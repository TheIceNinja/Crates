package net.theiceninja.crates.chests;

import lombok.Getter;
import lombok.Setter;
import net.theiceninja.crates.api.chest.ChestType;
import net.theiceninja.crates.api.chest.IChest;
import net.theiceninja.crates.chests.managers.ChestManager;
import net.theiceninja.crates.chests.tasks.GambleTask;
import net.theiceninja.ninjaapi.ColorUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter public class Chest implements IChest {

    private final int id;
    private final ChestType chestType;
    private boolean open = false;

    private ArmorStand displayItemArmorStand;
    private ArmorStand displayNameArmorStand;
    private GambleTask gambleTask;

    @Setter private Location chestLocation;
    private final List<ItemStack> items;

    private final ChestManager chestManager;

    public Chest(int id, ChestType chestType, Location chestLocation, List<ItemStack> items, ChestManager chestManager) {
        this.id = id;
        this.chestType = chestType;
        this.items = items;
        this.chestLocation = chestLocation;
        this.chestManager = chestManager;

        setupArmorStands();
    }

    public Chest(int id, ChestType chestType, ChestManager chestManager) {
        this.id = id;
        this.chestType = chestType;
        this.chestManager = chestManager;
        this.items = new ArrayList<>();
    }

    public Inventory getInventory() {
       Inventory itemsMenu = chestManager.getPlugin().getServer().createInventory(null, 27, "דברים שאתה יכול לקבל");
        for (int i = 0; i < items.size(); i++) {
            ItemStack item = items.get(i);
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
        ItemStack item = items.get(index);
        items.removeIf(existing -> existing.equals(item));
        if (item.getItemMeta() == null) return;

        chestManager.getChestFile().get().set("chests." + id + ".items." + item.getItemMeta().getDisplayName(), null);
        chestManager.getChestFile().save();
    }

    private void setupArmorStands() {
        if (chestLocation.getWorld() == null) return;

        this.displayNameArmorStand = (ArmorStand) chestManager.getPlugin().getServer().getWorld(chestLocation.getWorld().getName())
                .spawnEntity(
                        chestLocation.add(0.43, -0.9, 0.5),
                        EntityType.ARMOR_STAND
                );
        this.displayNameArmorStand.setGravity(false);
        this.displayNameArmorStand.setArms(false);
        this.displayNameArmorStand.setVisible(false);
        this.displayNameArmorStand.setInvulnerable(true);
        this.displayNameArmorStand.setCustomNameVisible(true);
        this.displayNameArmorStand.setCustomName(ColorUtils.color("&#F3ED13⭐ " + chestType.getPrefix() + " &#F3ED13⭐"));

        this.displayItemArmorStand = (ArmorStand) chestManager.getPlugin().getServer().getWorld(chestLocation.getWorld().getName())
                .spawnEntity(
                        chestLocation.add(0.1, 1.8, -0),
                        EntityType.ARMOR_STAND
                );
        this.displayItemArmorStand.setGravity(false);
        this.displayItemArmorStand.setArms(false);
        this.displayItemArmorStand.setVisible(false);
        this.displayItemArmorStand.setInvulnerable(true);
        this.displayItemArmorStand.setCustomNameVisible(false);
        this.displayItemArmorStand.setSmall(true);
        this.displayItemArmorStand.setCustomName(ColorUtils.color("&r"));
        this.displayItemArmorStand.setRotation(-80, 0);
    }

    @Override
    public void reload() {
        if (displayNameArmorStand != null) destroyArmorStand();
        if (displayItemArmorStand != null) this.displayItemArmorStand.remove();

        setupArmorStands();
    }

    @Override
    public void destroyArmorStand() {
        if (this.displayNameArmorStand != null) this.displayNameArmorStand.remove();
        if (this.displayItemArmorStand != null) this.displayItemArmorStand.remove();
    }

    @Override
    public void deleteChest() {
        destroyArmorStand();
    }

    @Override
    public void openChest(@NotNull Player player) {
        if (isOpen()) {
            cancel(player, "&#E81E33יש מישהו כרגע שפותח, אנא המתן.");
            return;
        }

        if (items.isEmpty()) {
            cancel(player, "&#E81E33אין דברים בתיבה הזאת.");
            return;
        }

        open = true;
        this.displayItemArmorStand.setCustomNameVisible(true);

        ItemStack item = player.getInventory().getItemInMainHand();
        item.setAmount(item.getAmount() - 1);
        gamble(player);
        player.sendMessage(ColorUtils.color("&aפותח לך את התיבה!"));
    }

    @Override
    public void cancel(@NotNull Player player, @NotNull String errorMessage) {
        player.setVelocity(player.getLocation().getDirection().multiply(-0.5));
        player.sendMessage(ColorUtils.color("&#E81E33אתה צריך מפתח כדי לפתוח את התיבה!"));
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
        int randomNumber = (int) randomizer(-1, items.size());
        if (this.gambleTask != null) this.gambleTask.cancel();

        this.gambleTask = new GambleTask(player.getUniqueId(), randomNumber, items.size() - 1, this);
        this.gambleTask.runTaskTimer(chestManager.getPlugin(), 0, 16);
    }

    private double randomizer(int a, int b) {
        return (double) b + (Math.random() * (a - b + 1));
    }

    public void resetDisplayItemArmorStand() {
        this.displayItemArmorStand.setCustomNameVisible(false);
        if (this.displayItemArmorStand.getEquipment() == null) return;

        this.displayItemArmorStand.getEquipment().setHelmet(null);
        open = false;
    }
}