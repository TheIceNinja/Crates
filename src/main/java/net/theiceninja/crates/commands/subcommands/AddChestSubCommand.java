package net.theiceninja.crates.commands.subcommands;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.api.chest.ChestType;
import net.theiceninja.crates.chests.Chest;
import net.theiceninja.crates.chests.managers.ChestManager;
import net.theiceninja.ninjaapi.ColorUtils;
import net.theiceninja.ninjaapi.SubCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;

@RequiredArgsConstructor
public class AddChestSubCommand implements SubCommand {

    private final ChestManager chestManager;

    @Override
    public void execute(Player player, String[] args) {
        if (args.length <= 2) {
            player.sendMessage(ColorUtils.color("&#E81E33אתה צריך לספק איידי וסוג."));
            return;
        }

        try {
            if (chestManager.findChest(Integer.parseInt(args[1])).isPresent()) {
                player.sendMessage(ColorUtils.color("&#E81E33יש כבר איידי כזה!"));
                return;
            }

            if (!containsValue(args[2])) {
                player.sendMessage(ColorUtils.color("&#E81E33זה לא חלק מ-enum!"));
                return;
            }

            Chest chest = new Chest(
                    Integer.parseInt(args[1]),
                    ChestType.valueOf(args[2].toUpperCase()),
                    chestManager
            );

            chestManager.getChestSetupHandler().addToSetup(player, chest);
            player.sendMessage(ColorUtils.color(
                    "&bיצרת עכשיו את התיבה "
                            + args[1] + " &bעם הסוג "
                            + ChestType.valueOf(args[2].toUpperCase()).getPrefix())
            );
        } catch (NumberFormatException ex) {
            player.sendMessage(ColorUtils.color("&#E81E33אתה צריך להקליד איידי של מספר!"));
        }
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
            if (Arrays.stream(ChestType.values()).toList().get(i).name().equals(name.toUpperCase())) return true;
        }

        return false;
    }
}
