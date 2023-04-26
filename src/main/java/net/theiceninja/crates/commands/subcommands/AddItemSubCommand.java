package net.theiceninja.crates.commands.subcommands;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.chests.Chest;
import net.theiceninja.crates.chests.managers.ChestManager;
import net.theiceninja.ninjaapi.ColorUtils;
import net.theiceninja.ninjaapi.SubCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Optional;

@RequiredArgsConstructor
public class AddItemSubCommand implements SubCommand {

    private final ChestManager chestManager;

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ColorUtils.color("&#E81E33אתה צריך לציין את האיידי של התיבה"));
            return;
        }

        int chestIndex;
        try {
            chestIndex = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            player.sendMessage(ColorUtils.color("&#E81E33האיידי לא נמצא"));
            return;
        }

        Optional<Chest> optionalChest = chestManager.findChest(chestIndex);
        if (optionalChest.isEmpty()) {
            player.sendMessage(ColorUtils.color("&#E81E33התיבה לא נמצאה, נסה עם איידי שונה."));
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            player.sendMessage(ColorUtils.color("&#E81E33אתה צריך להחזיק משהו!"));
            return;
        }

        if (item.getItemMeta() == null) {
            ItemMeta meta = item.getItemMeta();
            item.setItemMeta(meta);
        }

        optionalChest.get().addItem(item);
        player.sendMessage(ColorUtils.color("&#E81E33הוספת בהצלחה!"));
    }

    @Override
    public String getName() {
        return "additem";
    }

    @Override
    public String getDescription() {
        return "להוסיף אייטם לתיבה";
    }
}
