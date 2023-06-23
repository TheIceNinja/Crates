package net.theiceninja.crates.commands.subcommands;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.chests.Crate;
import net.theiceninja.crates.chests.managers.CrateManager;
import net.theiceninja.utilitys.java.NumberUtils;
import net.theiceninja.utilitys.spigot.color.ColorUtils;
import net.theiceninja.utilitys.spigot.color.TextColor;
import net.theiceninja.utilitys.spigot.commands.SubCommand;
import org.bukkit.entity.Player;

import java.util.Optional;

@RequiredArgsConstructor
public class RemoveItemSubCommand implements SubCommand {

    private final CrateManager crateManager;

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
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

        Optional<Crate> optionalChest = crateManager.findCrate(Integer.parseInt(args[1]));
        if (optionalChest.isEmpty()) {
            player.sendMessage(ColorUtils.colorString("&#E81E33התיבה לא נמצאה, נסה עם איידי שונה."));
            return;
        }

        optionalChest.get().removeItem(Integer.parseInt(args[2]));
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
