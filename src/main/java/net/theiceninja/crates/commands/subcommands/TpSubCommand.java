package net.theiceninja.crates.commands.subcommands;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.crate.Crate;
import net.theiceninja.crates.crate.managers.CrateManager;
import net.theiceninja.utilitys.Messages;
import net.theiceninja.utilitys.java.NumberUtils;
import net.theiceninja.utilitys.spigot.color.ColorUtils;
import net.theiceninja.utilitys.spigot.color.TextColor;
import net.theiceninja.utilitys.spigot.handlers.command.SubCommand;
import org.bukkit.entity.Player;

import java.util.Optional;

@RequiredArgsConstructor
public class TpSubCommand implements SubCommand {

    private final CrateManager crateManager;

    @Override
    public void execute(Player player, String[] args) {
        if (args.length == 1) {
            player.sendMessage(ColorUtils.colorChat(
                    TextColor.WRONG_USAGE,
                    "אתה צריך לספק שם לתיבה שאתה רוצה להשתגר אלייה."
            ));
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

        player.teleport(optionalCrate.get().getLocation());
        player.sendMessage(ColorUtils.colorChat(
                TextColor.SUCCESS,
                "השתגרת לתיבה בהצלחה!"
        ));
    }

    @Override
    public String getName() {
        return "tp";
    }

    @Override
    public String getDescription() {
        return "משתגר לתיבה.";
    }
}
