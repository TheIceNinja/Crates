package net.theiceninja.crates.crate.tasks;

import net.theiceninja.crates.crate.Crate;
import net.theiceninja.utilitys.spigot.color.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class CrateChooseItemTask extends BukkitRunnable {

    private final int randomNumber;
    private int timeItemLength;
    private int firstRoundTimeLength;

    private final Crate crate;
    private final Player player;

    private boolean isFirstRound = true;

    public CrateChooseItemTask(UUID uuid, int randomNumber, int length, Crate chest) {
        this.randomNumber = randomNumber;
        this.timeItemLength = length;
        this.crate = chest;

        this.player = Bukkit.getPlayer(uuid);
        this.firstRoundTimeLength = chest.getItems().size();
    }

    @Override
    public void run() {
        if (!isFirstRound) timeItemLength--;
        else firstRoundTimeLength--;

        if (player == null) {
            cancel();
            crate.reset();
            return;
        }

        if (firstRoundTimeLength < 0 && isFirstRound) {
            isFirstRound = false;
            firstRoundTimeLength = 0;
        }

        if (timeItemLength == randomNumber && !isFirstRound) {
            cancel();
            if (crate.getDisplayItemArmorStand() == null) return;

            ItemStack item = crate.getItems().stream().toList().get(timeItemLength);
            if (crate.getDisplayItemArmorStand().getEquipment() == null) return;

            crate.getDisplayItemArmorStand().setCustomName(ColorUtils.colorString(
                    "&#13F338זכית ב &#F3C313" +
                            (item.getItemMeta() == null ? item.getType().name().toUpperCase() : item.getItemMeta().getDisplayName()) +
                            " &#13B3F3" + item.getAmount()
            ));

            crate.getDisplayItemArmorStand().getEquipment().setHelmet(item);
            player.getInventory().addItem(item);
            player.playSound(player, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1, 1);
            player.sendMessage(ColorUtils.colorString(
                    "&r\n" +
                            "&#13F338זכית ב &#F3C313" +
                            (item.getItemMeta() == null ? item.getType().name().toUpperCase() : item.getItemMeta().getDisplayName()) +
                            " &#13B3F3" + item.getAmount()
                    )
            );

            Plugin plugin = crate.getCrateManager().getPlugin();
            AtomicInteger timeLeftToSpin = new AtomicInteger(16);
            AtomicInteger yaw = new AtomicInteger();

            crate.getCrateManager().getPlugin().getServer().getScheduler().runTaskTimer(plugin, task -> {
                if (crate.getDisplayItemArmorStand().getEquipment() == null) task.cancel();

                timeLeftToSpin.getAndDecrement();
                yaw.addAndGet(30);
                if (timeLeftToSpin.get() <= 0) {
                    task.cancel();
                    crate.reset();
                    return;
                }

                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                crate.getDisplayItemArmorStand().setRotation(yaw.get(), 0);

            }, 0, 12);

            return;
        }
        if (crate.getDisplayItemArmorStand() == null) return;
        if (crate.getDisplayItemArmorStand().getEquipment() == null) return;

        player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_BREAK, 3, 1);

        ItemStack item = crate.getItems().stream().toList().get(isFirstRound ? firstRoundTimeLength : timeItemLength);
        crate.getDisplayItemArmorStand().setCustomName(ColorUtils.colorString(
                "&#F3C713" +
                        (item.getItemMeta() == null ? item.getType().name().toUpperCase() : item.getItemMeta().getDisplayName()) +
                        " &#F31353" + item.getAmount()
        ));

        crate.getDisplayItemArmorStand().getEquipment().setHelmet(item);
    }
}
