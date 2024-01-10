package dev.relismdev.playlegendquests.commands.subcommands;

import dev.relismdev.playlegendquests.models.Quest;
import dev.relismdev.playlegendquests.storage.DatabaseInterface;
import dev.relismdev.playlegendquests.utils.msg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * Listens to player chat events to handle quest creation process through chat messages.
 * Uses {@link org.bukkit.event.player.AsyncPlayerChatEvent} for interaction.
 */
public class CreateHandler implements Listener {

    // HashMaps to store data temporarily for quest creation
    public static HashMap<Player, Quest> questCreationData = new HashMap<>();
    public static HashMap<Player, Integer> questCreationStep = new HashMap<>();

    /**
     * Listens for chat messages from players engaged in quest creation.
     * Handles the step-by-step process of creating a quest based on the chat interaction.
     *
     * @param e The AsyncPlayerChatEvent triggered by the player's chat message.
     */
    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        // Check if the player is in the process of creating a quest
        if (questCreationData.containsKey(player)) {
            e.setCancelled(true); // Cancel the chat message

            // Retrieve the current step of quest creation
            int step = questCreationStep.getOrDefault(player, 1);
            Quest quest = questCreationData.get(player);

            switch (step) {
                case 1:
                    // Set the quest name and proceed to the next step
                    quest.setName(e.getMessage());
                    questCreationStep.put(player, 2);
                    questCreationData.put(player, quest);
                    msg.sendLocale(player, "input_quest_description");
                    break;

                case 2:
                    // Set the quest description and proceed
                    quest.setDescription(e.getMessage());
                    questCreationStep.put(player, 3);
                    questCreationData.put(player, quest);
                    msg.sendLocale(player, "input_quest_coins");
                    break;

                case 3:
                    // Set the quest reward coins and proceed
                    quest.setReward_coins(Integer.parseInt(e.getMessage()));
                    questCreationStep.put(player, 4);
                    questCreationData.put(player, quest);
                    msg.sendLocale(player, "input_quest_item");
                    break;

                case 4:
                    // Determine if the held item should be the reward
                    Boolean isRewardItem = Boolean.valueOf(e.getMessage());
                    if (isRewardItem) {
                        ItemStack itemStack = player.getItemInHand();
                        if (!itemStack.isEmpty() && !itemStack.getType().isAir()) {
                            quest.setReward_item(itemStack);
                        }
                    }
                    // Cleanup data, create quest in database, and inform the player
                    questCreationStep.remove(player);
                    questCreationData.remove(player);
                    DatabaseInterface.createQuest(quest);
                    msg.sendLocale(player, "quest_creation_success");
                    break;
            }
        }
    }
}
