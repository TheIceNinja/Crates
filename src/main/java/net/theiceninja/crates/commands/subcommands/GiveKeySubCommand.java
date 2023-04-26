package net.theiceninja.crates.commands.subcommands;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.CratesPlugin;
import net.theiceninja.crates.api.chest.ChestType;
import net.theiceninja.crates.chests.managers.ChestManager;
import net.theiceninja.ninjaapi.ColorUtils;
import net.theiceninja.ninjaapi.ItemBuilder;
import net.theiceninja.ninjaapi.SubCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;

@RequiredArgsConstructor
public class GiveKeySubCommand implements SubCommand {

    private final CratesPlugin plugin;

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ColorUtils.color("&#E81E33אתה צריך להקליד את הסוג של המפתח"));
            return;
        }

        if (!containsValue(args[1])) {
            player.sendMessage(ColorUtils.color("&#E81E33זה לא חלק מ-enum!"));
            return;
        }

        ChestType chestType = ChestType.valueOf(args[1].toUpperCase());
        player.getInventory().addItem(
                new ItemBuilder(Material.TRIPWIRE_HOOK)
                        .setDisplayName("&#F3D813מפתח " + chestType.getPrefix())
                        .setCustomModelData(plugin.getConfig().getInt("item.keydata"))
                        .build()
        );

        player.sendMessage(ColorUtils.color("&aנוסף לך מפתח אחד לאינבנטורי שלך!"));
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
        for (int i = 0; i < ChestType.values().length; i++) {
            if (Arrays.stream(ChestType.values()).toList().get(i).name().equals(name.toUpperCase())) return true;
        }

        return false;
    }
}
