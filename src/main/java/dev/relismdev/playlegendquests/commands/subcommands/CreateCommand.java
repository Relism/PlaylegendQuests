package dev.relismdev.playlegendquests.commands.subcommands;

import dev.relismdev.playlegendquests.models.Quest;
import dev.relismdev.playlegendquests.commands.SubCommand;
import dev.relismdev.playlegendquests.utils.msg;
import org.bukkit.entity.Player;

/**
 * Subcommand to initiate the process of creating a quest in the game.
 * Extends {@link dev.relismdev.playlegendquests.commands.SubCommand}.
 */
public class CreateCommand extends SubCommand {

    /**
     * Retrieves the name of this subcommand.
     *
     * @return The name of the subcommand ("create").
     */
    @Override
    public String getName() {
        return "create";
    }

    /**
     * Retrieves the description of this subcommand.
     *
     * @return A brief description of the subcommand ("Create a Quest").
     */
    @Override
    public String getDescription() {
        return "Create a Quest";
    }

    /**
     * Retrieves the syntax of this subcommand.
     *
     * @return The syntax for using this subcommand.
     */
    @Override
    public String getSyntax() {
        return "/quests create <name> <description> <reward_coins> <reward item true/false>";
    }

    /**
     * Performs the action associated with this subcommand.
     * Initiates the quest creation process by prompting the player to input the quest name.
     *
     * @param player The player who initiated the subcommand.
     * @param args   Arguments passed along with the subcommand.
     */
    @Override
    public void perform(Player player, String[] args) {
        // Prompt the player to input the Quest name
        msg.sendLocale(player, "input_quest_name");

        // Create a new Quest object and store it for the player's quest creation
        Quest quest = new Quest(null, null, 0, null);
        CreateHandler.questCreationData.put(player, quest);
    }
}
