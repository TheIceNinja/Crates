package net.theiceninja.crates.commands.subcommands;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.chests.Crate;
import net.theiceninja.crates.chests.managers.CrateManager;
import net.theiceninja.utilitys.java.NumberUtils;
import net.theiceninja.utilitys.spigot.color.ColorUtils;
import net.theiceninja.utilitys.spigot.color.TextColor;
import net.theiceninja.utilitys.spigot.commands.SubCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Optional;

@RequiredArgsConstructor
public class AddItemSubCommand implements SubCommand {

    private final CrateManager crateManager;

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ColorUtils.colorString("&#E81E33אתה צריך לציין את האיידי של התיבה"));
            return;
        }

        if (!NumberUtils.isNumeric(args[1])) {
            player.sendMessage(ColorUtils.colorChat(
                    TextColor.ERROR,
                    "ארגיומנט זה לא מספר, אנא כתוב משהו שהוא מספר."
            ));
            return;
        }

        Optional<Crate> optionalChest = crateManager.findCrate(Integer.parseInt(args[1]));
        if (optionalChest.isEmpty()) {
            player.sendMessage(ColorUtils.colorString("&#E81E33התיבה לא נמצאה, נסה עם איידי שונה."));
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            player.sendMessage(ColorUtils.colorString("&#E81E33אתה צריך להחזיק משהו!"));
            return;
        }

        if (item.getItemMeta() == null) {
            ItemMeta meta = item.getItemMeta();
            item.setItemMeta(meta);
        }

        optionalChest.get().addItem(item);
        player.sendMessage(ColorUtils.colorString("&#E81E33הוספת בהצלחה!"));
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
