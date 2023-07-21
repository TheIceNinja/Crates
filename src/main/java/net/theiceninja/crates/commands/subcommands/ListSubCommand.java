package net.theiceninja.crates.commands.subcommands;

import lombok.RequiredArgsConstructor;
import net.theiceninja.crates.crate.managers.CrateManager;
import net.theiceninja.utilitys.spigot.color.ColorUtils;
import net.theiceninja.utilitys.spigot.handlers.command.SubCommand;
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

        crateManager.getCrateList().forEach(crate -> player.sendMessage(ColorUtils.colorString(
                "&#F3D113" + crate.getId() +
                        "\n&#13BAF3" + crate.getLocation().getX() +
                        "&6, &#13BAF3" + crate.getLocation().getY() +
                        "&6, &#13BAF3" + crate.getLocation().getZ()
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
