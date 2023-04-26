package net.theiceninja.crates.commands;

import net.theiceninja.crates.chests.managers.ChestManager;
import net.theiceninja.crates.commands.subcommands.*;
import net.theiceninja.ninjaapi.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CommandInfo(name = "crates", permission = "crates.admin", usage = "addchest|deletechest|editchest")
public class CratesCommand extends PluginCommand implements TabCompleter {

    private final List<SubCommand> subCommands = new ArrayList<>();

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
        Optional<SubCommand> optionalSubCommand = subCommands.stream().filter(subCommand -> subCommand.getName().equalsIgnoreCase(args[0])).findFirst();
        if (optionalSubCommand.isEmpty()) {
            player.sendMessage(ColorUtils.color("&cעקוב אחרי מה אתה צריך " + getCommandInfo().usage()));
            return;
        }

        optionalSubCommand.get().execute(player, args);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> complete = new ArrayList<>();
        if (args.length == 1) {
            subCommands.forEach(subCommand -> complete.add(subCommand.getName()));
        }

        List<String> result = new ArrayList<>();
        if (args.length != 1) return null;

        complete.forEach(completion -> {
            if (completion.toLowerCase().startsWith(args[0].toLowerCase()))
                result.add(completion);
        });

        return result;
    }
}
