package net.theiceninja.crates.commands.subcommands;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.chests.Chest;
import net.theiceninja.crates.chests.managers.ChestManager;
import net.theiceninja.utilitys.spigot.color.ColorUtils;
import net.theiceninja.utilitys.spigot.commands.SubCommand;
import org.bukkit.entity.Player;

import java.util.Optional;

@RequiredArgsConstructor
public class DeleteChestSubCommand implements SubCommand {

    private final ChestManager chestManager;

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ColorUtils.colorString("&#E81E33אתה צריך לציין שם לארנה שאתה רוצה למחוק."));
            return;
        }

        int chestIndex;
        try {
            chestIndex = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            player.sendMessage(ColorUtils.colorString("&#E81E33האיידי לא נמצא"));
            return;
        }

        Optional<Chest> optionalChest = chestManager.findChest(chestIndex);
        if (optionalChest.isEmpty()) {
            player.sendMessage(ColorUtils.colorString("&#E81E33התיבה לא נמצאה, נסה עם איידי שונה."));
            return;
        }

        chestManager.deleteChest(optionalChest.get());
        player.sendMessage(ColorUtils.colorString("&#E81E33התיבה הוסרה בהצלחה!"));
    }

    @Override
    public String getName() {
        return "deletechest";
    }

    @Override
    public String getDescription() {
        return "מוחק תיבה";
    }
}
