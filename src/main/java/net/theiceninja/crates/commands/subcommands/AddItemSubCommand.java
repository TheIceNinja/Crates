package net.theiceninja.crates.commands.subcommands;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.crate.Crate;
import net.theiceninja.crates.crate.managers.CrateManager;
import net.theiceninja.utils.Messages;
import net.theiceninja.utils.java.NumberUtils;
import net.theiceninja.utils.spigot.color.ColorUtils;
import net.theiceninja.utils.spigot.color.TextColor;
import net.theiceninja.utils.spigot.handlers.command.SubCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
            player.sendMessage(Messages.NOT_NUMBER);
            return;
        }

        Optional<Crate> optionalCrate = crateManager.findCrateById(Integer.parseInt(args[1]));
        if (optionalCrate.isEmpty()) {
            player.sendMessage(ColorUtils.colorString("&#E81E33התיבה לא נמצאה, נסה עם איידי שונה."));
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            player.sendMessage(ColorUtils.colorString("&#E81E33אתה צריך להחזיק משהו!"));
            return;
        }

        optionalCrate.get().addItem(item);
        player.sendMessage(ColorUtils.colorString(TextColor.SUCCESS + "הוספת בהצלחה!"));
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
