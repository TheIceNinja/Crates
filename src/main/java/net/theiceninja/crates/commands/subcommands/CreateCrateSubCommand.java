package net.theiceninja.crates.commands.subcommands;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.api.crate.CrateType;
import net.theiceninja.crates.crate.Crate;
import net.theiceninja.crates.crate.managers.CrateManager;
import net.theiceninja.utils.Messages;
import net.theiceninja.utils.java.NumberUtils;
import net.theiceninja.utils.spigot.color.ColorUtils;
import net.theiceninja.utils.spigot.handlers.command.SubCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;

@RequiredArgsConstructor
public class CreateCrateSubCommand implements SubCommand {

    private final CrateManager crateManager;

    @Override
    public void execute(Player player, String[] args) {
        if (args.length <= 2) {
            player.sendMessage(ColorUtils.colorString("&#E81E33אתה צריך לספק איידי וסוג."));
            return;
        }

        if (!NumberUtils.isNumeric(args[1])) {
            player.sendMessage(Messages.NOT_NUMBER);
            return;
        }

        if (crateManager.findCrateById(Integer.parseInt(args[1])).isPresent()) {
            player.sendMessage(ColorUtils.colorString("&#E81E33יש כבר איידי כזה!"));
            return;
        }

        if (!containsValue(args[2])) {
            player.sendMessage(ColorUtils.colorString("&#E81E33זה לא חלק מ-enum!"));
            return;
        }

        Crate crate = new Crate(
                Integer.parseInt(args[1]),
                CrateType.valueOf(args[2].toUpperCase()),
                crateManager
        );

        crateManager.getCrateSetupHandler().addToSetup(player, crate);
        player.sendMessage(ColorUtils.colorString(
                "&bיצרת עכשיו את התיבה " +
                        args[1] + " &bעם הסוג " +
                        CrateType.valueOf(args[2].toUpperCase()).getPrefix())
        );
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getDescription() {
        return "שם תיבה על פי סוג";
    }

    private boolean containsValue(String name) {
        for (int i = 0; i < CrateType.values().length; i++) {
            if (Arrays.stream(CrateType.values()).toList().get(i).name().equals(name.toUpperCase()))
                return true;
        }

        return false;
    }
}
