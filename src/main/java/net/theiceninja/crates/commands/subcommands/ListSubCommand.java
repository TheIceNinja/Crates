package net.theiceninja.crates.commands.subcommands;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.chests.managers.CrateManager;
import net.theiceninja.utilitys.spigot.color.ColorUtils;
import net.theiceninja.utilitys.spigot.commands.SubCommand;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class ListSubCommand implements SubCommand {

    private final CrateManager crateManager;

    @Override
    public void execute(Player player, String[] strings) {
        if (crateManager.getCrateList().isEmpty()) {
            player.sendMessage(ColorUtils.colorString("&#E81E33לא נמצאו תיבות בשרת."));
            return;
        }

        crateManager.getCrateList().forEach(chest -> player.sendMessage(ColorUtils.colorString(
                "&#F3D113" + chest.getId()
                  + "\n&#13BAF3" + chest.getLocation().getX() +
                    "&6, &#13BAF3" + chest.getLocation().getY() +
                    "&6, &#13BAF3" + chest.getLocation().getZ()
        )));
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "מביא רשימה של התיבות המפוזרות במפה";
    }
}
