package net.theiceninja.crates.commands.subcommands;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.crate.Crate;
import net.theiceninja.crates.crate.managers.CrateManager;
import net.theiceninja.utilitys.java.NumberUtils;
import net.theiceninja.utilitys.spigot.color.ColorUtils;
import net.theiceninja.utilitys.spigot.color.TextColor;
import net.theiceninja.utilitys.spigot.handlers.command.SubCommand;
import org.bukkit.entity.Player;

import java.util.Optional;

@RequiredArgsConstructor
public class RemoveItemSubCommand implements SubCommand {

    private final CrateManager crateManager;

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage(ColorUtils.colorString("&#E81E33אתה צריך להקליד את האיידי של התיבה + המספר של האייטם בתיבה"));
            return;
        }

        if (!NumberUtils.isNumeric(args[1]) || !NumberUtils.isNumeric(args[2])) {
            player.sendMessage(ColorUtils.colorChat(
                    TextColor.ERROR,
                    "ארגיומנט זה לא מספר, אנא כתוב משהו שהוא מספר."
            ));
            return;
        }

        Optional<Crate> optionalCrate = crateManager.findCrateById(Integer.parseInt(args[1]));
        if (optionalCrate.isEmpty()) {
            player.sendMessage(ColorUtils.colorString("&#E81E33התיבה לא נמצאה, נסה עם איידי שונה."));
            return;
        }

        optionalCrate.get().removeItem(Integer.parseInt(args[2]));
        player.sendMessage(ColorUtils.colorChat(TextColor.SUCCESS, "נמחק האייטם!"));
    }

    @Override
    public String getName() {
        return "removeitem";
    }

    @Override
    public String getDescription() {
        return "מוריד אייטם מהרשימה";
    }
}
