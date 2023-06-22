package net.theiceninja.crates.commands;

import net.theiceninja.crates.chests.managers.ChestManager;
import net.theiceninja.crates.commands.subcommands.*;
import net.theiceninja.utilitys.spigot.color.ColorUtils;
import net.theiceninja.utilitys.spigot.color.TextColor;
import net.theiceninja.utilitys.spigot.commands.CommandInfo;
import net.theiceninja.utilitys.spigot.commands.PluginCommand;
import net.theiceninja.utilitys.spigot.commands.SubCommand;
import net.theiceninja.utilitys.spigot.commands.subcommand.HelpSubCommand;
import org.bukkit.entity.Player;

import java.util.*;

@CommandInfo(name = "crates", permission = "crates.admin", subCommandNames = {"deletechest", "addchest", "chestlist", "additem", "givekey", "removeitem"})
public class CratesCommand extends PluginCommand {

    private final Set<SubCommand> subCommands = new HashSet<>();

    public CratesCommand(ChestManager chestManager) {
        subCommands.add(new GiveKeySubCommand(chestManager.getPlugin()));
        subCommands.add(new AddItemSubCommand(chestManager));
        subCommands.add(new DeleteChestSubCommand(chestManager));
        subCommands.add(new RemoveItemSubCommand(chestManager));
        subCommands.add(new ListSubCommand(chestManager));
        subCommands.add(new AddChestSubCommand(chestManager));
        subCommands.add(new HelpSubCommand(subCommands, getCommandInfo()));
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ColorUtils.colorChat(
                    TextColor.WRONG_USAGE,
                    "Usage: /crates <deletechest|addchest|listchest|additem|givekey|removeitem>"
            ));
            return;
        }

        Optional<SubCommand> optionalSubCommand = subCommands.stream().filter(subCommand -> subCommand.getName().equalsIgnoreCase(args[0])).findFirst();
        if (optionalSubCommand.isEmpty()) {
            player.sendMessage(ColorUtils.colorChat(
                    TextColor.WRONG_USAGE,
                    "Usage: /crates <deletechest|addchest|listchest|additem|givekey|removeitem>"
            ));
            return;
        }

        optionalSubCommand.get().execute(player, args);
    }
}
