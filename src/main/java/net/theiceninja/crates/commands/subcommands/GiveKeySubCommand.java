package net.theiceninja.crates.commands.subcommands;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.CratesPlugin;
import net.theiceninja.crates.api.chest.CrateType;
import net.theiceninja.utilitys.spigot.ItemBuilder;
import net.theiceninja.utilitys.spigot.color.ColorUtils;
import net.theiceninja.utilitys.spigot.commands.SubCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;

@RequiredArgsConstructor
public class GiveKeySubCommand implements SubCommand {

    private final CratesPlugin plugin;

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ColorUtils.colorString("&#E81E33אתה צריך להקליד את הסוג של המפתח"));
            return;
        }

        if (!containsValue(args[1])) {
            player.sendMessage(ColorUtils.colorString("&#E81E33זה לא חלק מ-enum!"));
            return;
        }

        CrateType chestType = CrateType.valueOf(args[1].toUpperCase());
        player.getInventory().addItem(
                new ItemBuilder(Material.TRIPWIRE_HOOK)
                        .setDisplayName("&#F3D813מפתח " + chestType.getPrefix())
                        .setCustomModelData(plugin.getConfig().getInt("item.keydata"))
                        .build()
        );

        player.sendMessage(ColorUtils.colorString("&aנוסף לך מפתח אחד לאינבנטורי שלך!"));
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
