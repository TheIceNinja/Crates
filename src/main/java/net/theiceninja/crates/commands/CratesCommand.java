package net.theiceninja.crates.commands;

import net.theiceninja.crates.crate.managers.CrateManager;
import net.theiceninja.crates.commands.subcommands.*;
import net.theiceninja.utilitys.spigot.color.ColorUtils;
import net.theiceninja.utilitys.spigot.color.TextColor;
import net.theiceninja.utilitys.spigot.handlers.command.CommandInfo;
import net.theiceninja.utilitys.spigot.handlers.command.PluginCommand;
import net.theiceninja.utilitys.spigot.handlers.command.SubCommand;
import net.theiceninja.utilitys.spigot.handlers.command.subcommand.HelpSubCommand;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@CommandInfo(
        name = "crates",
        permission = "crates.admin",
        subCommandsNames = {"delete", "add", "list", "additem", "givekey", "removeitem", "help"}
)
public class CratesCommand extends PluginCommand {

    private final Set<SubCommand> subCommands = new HashSet<>();

    public CratesCommand(CrateManager crateManager) {
        subCommands.add(new GiveKeySubCommand(crateManager.getPlugin()));
        subCommands.add(new AddItemSubCommand(crateManager));
        subCommands.add(new DeleteCrateSubCommand(crateManager));
        subCommands.add(new RemoveItemSubCommand(crateManager));
        subCommands.add(new ListSubCommand(crateManager));
        subCommands.add(new AddCrateSubCommand(crateManager));
        subCommands.add(new HelpSubCommand(subCommands, getCommandInfo()));
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ColorUtils.colorChat(
                    TextColor.WRONG_USAGE,
                    "Usage: /crates <delete|add|list|additem|givekey|removeitem>"
            ));
            return;
        }

        Optional<SubCommand> optionalSubCommand = subCommands.stream()
                .filter(subCommand -> subCommand.getName().equalsIgnoreCase(args[0]))
                .findFirst();
        if (optionalSubCommand.isEmpty()) {
            player.sendMessage(ColorUtils.colorChat(
                    TextColor.WRONG_USAGE,
                    "Usage: /crates <delete|add|list|additem|givekey|removeitem>"
            ));
            return;
        }

        optionalSubCommand.get().execute(player, args);
    }
}
