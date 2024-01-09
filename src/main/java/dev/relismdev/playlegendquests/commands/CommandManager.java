package dev.relismdev.playlegendquests.commands;

import dev.relismdev.playlegendquests.commands.subcommands.CreateCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Manages and executes subcommands for a Bukkit plugin.
 * Implements {@link org.bukkit.command.CommandExecutor} to handle commands.
 */
public class CommandManager implements CommandExecutor {

    // Store subcommands in an ArrayList
    private final ArrayList<SubCommand> subcommands = new ArrayList<>();

    // Constructor to initialize the CommandManager
    public CommandManager() {
        subcommands.add(new CreateCommand());
    }

    /**
     * Executes the command.
     *
     * @param sender  The sender of the command.
     * @param command The command that was executed.
     * @param label   The alias of the command used.
     * @param args    The arguments provided with the command.
     * @return True if the command was handled, false otherwise.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if the sender is a player
        if (!(sender instanceof Player)) {
            return true; // If the sender is not a player, return true
        }

        Player player = (Player) sender; // Cast sender to Player

        if (args.length > 0) {
            // Iterate through subcommands to find a match
            for (SubCommand subCommand : subcommands) {
                if (args[0].equalsIgnoreCase(subCommand.getName())) {
                    subCommand.perform(player, args); // Execute the matching subcommand
                    return true; // Return after executing the command
                }
            }
        } else {
            // Display available subcommands and their descriptions
            player.sendMessage("--------------------------------");
            for (SubCommand subCommand : subcommands) {
                player.sendMessage(subCommand.getSyntax() + " - " + subCommand.getDescription());
            }
            player.sendMessage("--------------------------------");
        }
        return true;
    }

    /**
     * Retrieves the list of registered subcommands.
     *
     * @return The list of subcommands.
     */
    public ArrayList<SubCommand> getSubcommands() {
        return subcommands;
    }
}
