package net.theiceninja.crates.commands.subcommands;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.chests.Chest;
import net.theiceninja.crates.chests.managers.ChestManager;
import net.theiceninja.ninjaapi.ColorUtils;
import net.theiceninja.ninjaapi.SubCommand;
import org.bukkit.entity.Player;

import java.util.Optional;

@RequiredArgsConstructor
public class RemoveItemSubCommand implements SubCommand {

    private final ChestManager chestManager;

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ColorUtils.color("&#E81E33אתה צריך להקליד את האיידי של התיבה + המספר של האייטם בתיבה"));
            return;
        }

        int chestIndex, itemIndex;
        try {
            chestIndex = Integer.parseInt(args[1]);
            itemIndex = Integer.parseInt(args[2]);
        } catch (NumberFormatException ex) {
            player.sendMessage(ColorUtils.color("&#E81E33האיידי לא נמצא"));
            return;
        }

        Optional<Chest> optionalChest = chestManager.findChest(chestIndex);
        if (optionalChest.isEmpty()) {
            player.sendMessage(ColorUtils.color("&#E81E33התיבה לא נמצאה, נסה עם איידי שונה."));
            return;
        }

        optionalChest.get().removeItem(itemIndex);
        player.sendMessage(ColorUtils.color("&#E81E33נמחק האייטם!"));
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
