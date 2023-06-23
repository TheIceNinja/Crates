package net.theiceninja.crates.commands.subcommands;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.api.chest.ChestType;
import net.theiceninja.crates.chests.Chest;
import net.theiceninja.crates.chests.managers.ChestManager;
import net.theiceninja.utilitys.java.NumberUtils;
import net.theiceninja.utilitys.spigot.color.ColorUtils;
import net.theiceninja.utilitys.spigot.color.TextColor;
import net.theiceninja.utilitys.spigot.commands.SubCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;

@RequiredArgsConstructor
public class AddChestSubCommand implements SubCommand {

    private final ChestManager chestManager;

    @Override
    public void execute(Player player, String[] args) {
        if (args.length <= 2) {
            player.sendMessage(ColorUtils.colorString("&#E81E33אתה צריך לספק איידי וסוג."));
            return;
        }

        if (!NumberUtils.isNumeric(args[1])) {
            player.sendMessage(ColorUtils.colorChat(
                    TextColor.ERROR,
                    "ארגיומנט זה לא מספר, אנא כתוב משהו שהוא מספר."
            ));
            return;
        }

        if (chestManager.findChest(Integer.parseInt(args[1])).isPresent()) {
            player.sendMessage(ColorUtils.colorString("&#E81E33יש כבר איידי כזה!"));
            return;
        }

        if (!containsValue(args[2])) {
            player.sendMessage(ColorUtils.colorString("&#E81E33זה לא חלק מ-enum!"));
            return;
        }

        Chest chest = new Chest(
                Integer.parseInt(args[1]),
                ChestType.valueOf(args[2].toUpperCase()),
                chestManager
        );

        chestManager.getChestSetupHandler().addToSetup(player, chest);
        player.sendMessage(ColorUtils.colorString(
                "&bיצרת עכשיו את התיבה "
                        + args[1] + " &bעם הסוג "
                        + ChestType.valueOf(args[2].toUpperCase()).getPrefix())
        );
    }

    @Override
    public String getName() {
        return "addchest";
    }

    @Override
    public String getDescription() {
        return "שם תיבה על פי סוג";
    }

    private boolean containsValue(String name) {
        for (int i = 0; i < ChestType.values().length; i++) {
            if (Arrays.stream(ChestType.values()).toList().get(i).name().equals(name.toUpperCase()))
                return true;
        }

        return false;
    }
}
