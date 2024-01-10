package dev.relismdev.playlegendquests.models;

import org.bukkit.inventory.ItemStack;

/**
 * Represents a quest in the game, encapsulating its unique identifier, name, description,
 * reward in coins, and an item rewarded upon completion.
 */
public class Quest {

    private String name; // Name of the quest
    private String description; // Description of the quest
    private int reward_coins; // Coins rewarded upon completion
    private ItemStack reward_item; // Item rewarded upon completion

    /**
     * Constructs a Quest object with the specified details.
     *
     * @param name           The name of the quest.
     * @param description    The description of the quest.
     * @param reward_coins   The amount of coins rewarded for completing the quest.
     * @param reward_item    The ItemStack representing the item rewarded for completing the quest.
     */
    public Quest(String name, String description, int reward_coins, ItemStack reward_item) {
        this.name = name;
        this.description = description;
        this.reward_coins = reward_coins;
        this.reward_item = reward_item;
    }

    /**
     * Retrieves the name of the quest.
     *
     * @return The name of the quest.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the quest.
     *
     * @param name The new name of the quest.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the description of the quest.
     *
     * @return The description of the quest.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the quest.
     *
     * @param description The new description of the quest.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retrieves the amount of coins rewarded for completing the quest.
     *
     * @return The amount of coins rewarded for completing the quest.
     */
    public int getReward_coins() {
        return reward_coins;
    }

    /**
     * Sets the amount of coins rewarded for completing the quest.
     *
     * @param reward_coins The new amount of coins rewarded for completing the quest.
     */
    public void setReward_coins(int reward_coins) {
        this.reward_coins = reward_coins;
    }

    /**
     * Retrieves the item rewarded for completing the quest.
     *
     * @return The ItemStack representing the item rewarded for completing the quest.
     */
    public ItemStack getReward_item() {
        return reward_item;
    }

    /**
     * Sets the item rewarded for completing the quest.
     *
     * @param reward_item The new ItemStack representing the item rewarded for completing the quest.
     */
    public void setReward_item(ItemStack reward_item) {
        this.reward_item = reward_item;
    }
}
