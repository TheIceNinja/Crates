package net.theiceninja.crates.crate;

import lombok.Getter;
import lombok.Setter;
import net.theiceninja.crates.api.crate.CrateType;
import net.theiceninja.crates.api.crate.ICrate;
import net.theiceninja.crates.crate.managers.CrateManager;
import net.theiceninja.crates.crate.tasks.GambleTask;
import net.theiceninja.utilitys.java.NumberUtils;
import net.theiceninja.utilitys.spigot.color.ColorUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter
public class Crate implements ICrate {

    private final int id;
    private final CrateType type;
    private boolean open = false;

    private ArmorStand
            displayItemArmorStand,
            displayNameArmorStand;
    private GambleTask gambleTask;

    @Setter private Location location;
    private final Set<ItemStack> items;

    private final CrateManager crateManager;

    // loading from configuration file
    public Crate(int id, CrateType crateType, Location chestLocation, Set<ItemStack> items, CrateManager crateManager) {
        this.id = id;
        this.type = crateType;
        this.items = items;
        this.location = chestLocation;
        this.crateManager = crateManager;

        setupArmorStands();
    }

    // creating the chest
    public Crate(int id, CrateType chestType, CrateManager crateManager) {
        this.id = id;
        this.type = chestType;
        this.crateManager = crateManager;
        this.items = new HashSet<>();
    }

    public Inventory getInventory() {
        Inventory itemsMenu = crateManager.getPlugin().getServer().createInventory(
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
        if (key.isEmpty())
            key = UUID.randomUUID().toString();

        crateManager.getCratesFile().get().set("chests." + id + ".items." + key, item);
        crateManager.getCratesFile().save();

        items.add(item);
    }

    @Override
    public void removeItem(int index) {
        ItemStack item = items.stream().toList().get(index);
        items.removeIf(existing -> existing.equals(item));
        if (item.getItemMeta() == null) return;

        crateManager.getCratesFile().get().set(
                "chests." + id + ".items." + item.getItemMeta().getDisplayName(),
                null
        );
        crateManager.getCratesFile().save();
    }

    private void setupArmorStands() {
        if (location.getWorld() == null) return;

        this.displayNameArmorStand = applyArmorStandProperties(
                location.add(0.43, -0.9, 0.5),
                "&#F3ED13⭐ " + type.getPrefix() + " &#F3ED13⭐"
        );
        this.displayNameArmorStand.setCustomNameVisible(true);

        this.displayItemArmorStand = applyArmorStandProperties(
                location.add(0.1, 1.8, 0),
                "&r"
        );
        this.displayItemArmorStand.setCustomNameVisible(false);
        this.displayItemArmorStand.setSmall(true);
        this.displayItemArmorStand.setRotation(-80, 0);
    }

    private ArmorStand applyArmorStandProperties(@NotNull Location spawnLocation, @NotNull String displayName) {
        ArmorStand armorStand = (ArmorStand) crateManager
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
        location.getBlock().setType(Material.AIR);
        items.clear();
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

        setOpen(true);
        this.displayItemArmorStand.setCustomNameVisible(true);

        ItemStack item = player.getInventory().getItemInMainHand();
        item.setAmount(item.getAmount() - 1);
        gamble(player);
        player.sendMessage(ColorUtils.colorString("&aפותח לך את התיבה!"));
    }

    @Override
    public void cancelClick(@NotNull Player player, @NotNull String reason) {
        player.setVelocity(player.getLocation().getDirection().multiply(-0.5));
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1);
        player.sendMessage(ColorUtils.colorString(reason));
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
        this.gambleTask.runTaskTimer(crateManager.getPlugin(), 0, 16);
    }

    @Override
    public void reset() {
        this.displayItemArmorStand.setCustomNameVisible(false);
        if (this.displayItemArmorStand.getEquipment() == null) return;

        this.displayItemArmorStand.getEquipment().setHelmet(null);
        setOpen(false);
    }
}