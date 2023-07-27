package net.theiceninja.crates.commands.subcommands;

import net.theiceninja.crates.api.crate.CrateType;
import net.theiceninja.utils.Messages;
import net.theiceninja.utils.java.NumberUtils;
import net.theiceninja.utils.spigot.ItemBuilder;
import net.theiceninja.utils.spigot.color.ColorUtils;
import net.theiceninja.utils.spigot.color.TextColor;
import net.theiceninja.utils.spigot.handlers.command.SubCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class GiveKeySubCommand implements SubCommand {

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage(ColorUtils.colorString("&#E81E33אתה צריך להקליד את הסוג של המפתח ו-כמות."));
            return;
        }

        if (!containsValue(args[1])) {
            player.sendMessage(ColorUtils.colorString("&#E81E33זה לא חלק מ-enum!"));
            return;
        }

        if (!NumberUtils.isNumeric(args[2])) {
            player.sendMessage(Messages.NOT_NUMBER);
            return;
        }

        CrateType crateType = CrateType.valueOf(args[1].toUpperCase());
        player.getInventory().addItem(
                new ItemBuilder(Material.TRIPWIRE_HOOK)
                        .setDisplayName("&#F3D813מפתח " + crateType.getPrefix())
                        .setAmount(Integer.parseInt(args[2]))
                        .build()
        );

        player.sendMessage(ColorUtils.colorString(TextColor.SUCCESS + "נוסף לך מפתח אחד לאינבנטורי שלך!"));
    }

    @Override
    public String getName() {
        return "givekey";
    }

    @Override
    public String getDescription() {
        return "מביא לך מפתח";
    }

    private boolean containsValue(String name) {
        for (int i = 0; i < CrateType.values().length; i++) {
            if (Arrays.stream(CrateType.values()).toList().get(i).name().equals(name.toUpperCase())) return true;
        }

        return false;
    }
}
