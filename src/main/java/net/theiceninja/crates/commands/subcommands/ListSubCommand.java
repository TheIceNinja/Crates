package net.theiceninja.crates.commands.subcommands;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.chests.managers.ChestManager;
import net.theiceninja.utilitys.spigot.color.ColorUtils;
import net.theiceninja.utilitys.spigot.commands.SubCommand;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class ListSubCommand implements SubCommand {

    private final ChestManager chestManager;

    @Override
    public void execute(Player player, String[] strings) {
        if (chestManager.getChestList().isEmpty()) {
            player.sendMessage(ColorUtils.colorString("&#E81E33לא נמצאו תיבות בשרת."));
            return;
        }

        chestManager.getChestList().forEach(chest -> player.sendMessage(ColorUtils.colorString(
                "&#F3D113" + chest.getId()
                  + "\n&#13BAF3" + chest.getChestLocation().getX() +
                    "&6, &#13BAF3" + chest.getChestLocation().getY() +
                    "&6, &#13BAF3" + chest.getChestLocation().getZ()
        )));
    }

    @Override
    public String getName() {
        return "chestList";
    }

    @Override
    public String getDescription() {
        return "מביא רשימה של התיבות המפוזרות במפה";
    }
}
