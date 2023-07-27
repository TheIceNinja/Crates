package net.theiceninja.crates.crate.tasks;

import net.theiceninja.crates.crate.Crate;
import net.theiceninja.utils.spigot.color.ColorUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class CrateChooseItemTask extends BukkitRunnable {

    private final Player player;
    private final Crate crate;

    private final int randomNumber;
    private int timeItemLength;
    private int firstRoundTimeLength;

    private boolean isFirstRound = true;

    public CrateChooseItemTask(UUID uuid, int randomNumber, int length, Crate crate) {
        this.randomNumber = randomNumber;
        this.timeItemLength = length;
        this.crate = crate;

        this.player = crate.getCrateManager().getPlugin().getServer().getPlayer(uuid);
        this.firstRoundTimeLength = crate.getItems().size();
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

        // get the item (choose the item by the random number) and ending the animation
        if (timeItemLength == randomNumber && !isFirstRound) {
            cancel();
            if (crate.getDisplayItemArmorStand() == null) return;
            if (crate.getDisplayItemArmorStand().getEquipment() == null) return;

            ItemStack reward = (ItemStack) crate.getItems().toArray()[timeItemLength];
            if (reward.getItemMeta() == null) return;

            crate.getDisplayItemArmorStand().setCustomName(ColorUtils.colorString(
                    "&#13F338זכית ב &#F3C313" +
                            reward.getItemMeta().getDisplayName() +
                            " &#13B3F3" + reward.getAmount()
            ));

            crate.getDisplayItemArmorStand().getEquipment().setHelmet(reward);
            player.getInventory().addItem(reward);
            player.playSound(player, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1, 1);
            player.sendMessage(ColorUtils.colorString(
                            "&r\n" +
                                    "&#13F338זכית ב &#F3C313" +
                                    reward.getItemMeta().getDisplayName() +
                                    " &#13B3F3" + reward.getAmount() + "\n&r"
                    )
            );

            Plugin plugin = crate.getCrateManager().getPlugin();
            AtomicInteger timeLeftToSpin = new AtomicInteger(16);
            AtomicInteger yaw = new AtomicInteger();

            plugin.getServer().getScheduler().runTaskTimer(plugin, task -> {
                if (crate.getDisplayItemArmorStand().getEquipment() == null) {
                    task.cancel();
                    return;
                }

                timeLeftToSpin.getAndDecrement();
                yaw.addAndGet(30);
                if (timeLeftToSpin.get() <= 0) {
                    task.cancel();
                    crate.reset();
                    player.playSound(player, Sound.ENTITY_EXPERIENCE_BOTTLE_THROW, 1, 1);
                    return;
                }

                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                crate.getDisplayItemArmorStand().setRotation(yaw.get(), 0);
            }, 0, 12);

            return;
        }
        if (crate.getDisplayItemArmorStand() == null) return;
        if (crate.getDisplayItemArmorStand().getEquipment() == null) return;

        // showing players the items, after that choosing the item
        player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_BREAK, 3, 1);

        ItemStack possibleItemToGet = (ItemStack) crate.getItems().toArray()[isFirstRound ? firstRoundTimeLength : timeItemLength];
        if (possibleItemToGet.getItemMeta() == null) return;

        crate.getDisplayItemArmorStand().setCustomName(ColorUtils.colorString(
                "&#F3C713" + possibleItemToGet.getItemMeta().getDisplayName() + " &#F31353" + possibleItemToGet.getAmount()
        ));

        crate.getDisplayItemArmorStand().getEquipment().setHelmet(possibleItemToGet);
    }
}
