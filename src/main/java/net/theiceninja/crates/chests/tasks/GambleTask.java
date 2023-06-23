package net.theiceninja.crates.chests.tasks;

import net.theiceninja.crates.chests.Chest;
import net.theiceninja.utilitys.spigot.color.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class GambleTask extends BukkitRunnable {

    private final int randomNumber;
    private int timeItemLength;
    private int firstRoundTimeLength;

    private final Chest chest;
    private final Player player;

    private boolean isFirstRound = true;

    public GambleTask(UUID uuid, int randomNumber, int length, Chest chest) {
        this.randomNumber = randomNumber;
        this.timeItemLength = length;
        this.chest = chest;

        this.player = Bukkit.getPlayer(uuid);
        this.firstRoundTimeLength = chest.getItems().size();
    }

    @Override
    public void run() {
        if (!isFirstRound) timeItemLength--;
        else firstRoundTimeLength--;

        if (player == null) {
            cancel();
            chest.reset();
            return;
        }

        if (firstRoundTimeLength < 0 && isFirstRound) {
            isFirstRound = false;
            firstRoundTimeLength = 0;
        }

        if (timeItemLength == randomNumber && !isFirstRound) {
            cancel();
            if (chest.getDisplayItemArmorStand() == null) return;

            ItemStack item = chest.getItems().stream().toList().get(timeItemLength);
            if (chest.getDisplayItemArmorStand().getEquipment() == null) return;

            chest.getDisplayItemArmorStand().setCustomName(ColorUtils.colorString(
                    "&#13F338זכית ב &#F3C313" +
                            (item.getItemMeta() == null ? item.getType().name().toUpperCase() : item.getItemMeta().getDisplayName())
                            + " &#13B3F3" + item.getAmount()
            ));

            chest.getDisplayItemArmorStand().getEquipment().setHelmet(item);
            player.getInventory().addItem(item);
            player.playSound(player, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1, 1);
            player.sendMessage(ColorUtils.colorString(
                    "&r\n" +
                            "&#13F338זכית ב &#F3C313" +
                            (item.getItemMeta() == null ? item.getType().name().toUpperCase() : item.getItemMeta().getDisplayName())
                            + " &#13B3F3" + item.getAmount())
                    + "\n&r"
            );

            Plugin plugin = chest.getChestManager().getPlugin();
            AtomicInteger timeLeftToSpin = new AtomicInteger(16);
            AtomicInteger yaw = new AtomicInteger();

            plugin.getServer().getScheduler().runTaskTimer(plugin, task -> {
                if (chest.getDisplayItemArmorStand().getEquipment() == null) task.cancel();

                timeLeftToSpin.getAndDecrement();
                yaw.addAndGet(30);
                if (timeLeftToSpin.get() <= 0) {
                    task.cancel();
                    chest.reset();
                    return;
                }

                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                chest.getDisplayItemArmorStand().setRotation(yaw.get(), 0);

            }, 0, 12);

            return;
        }
        if (chest.getDisplayItemArmorStand() == null) return;
        if (chest.getDisplayItemArmorStand().getEquipment() == null) return;

        player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_BREAK, 3, 1);

        ItemStack item = chest.getItems().stream().toList().get(isFirstRound ? firstRoundTimeLength : timeItemLength);
        chest.getDisplayItemArmorStand().setCustomName(ColorUtils.colorString(
                "&#F3C713" +
                        (item.getItemMeta() == null ? item.getType().name().toUpperCase() : item.getItemMeta().getDisplayName())
                        + " &#F31353" + item.getAmount()
        ));

        chest.getDisplayItemArmorStand().getEquipment().setHelmet(item);
    }
}
