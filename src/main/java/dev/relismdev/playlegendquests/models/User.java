package dev.relismdev.playlegendquests.models;

import org.bukkit.OfflinePlayer;

/**
 * Represents an User profile associated with a player, containing information about them.
 */
public class User {

    private static OfflinePlayer player; // The associated player
    private static String uuid;
    private static int balance; // The player's balance
    private static String locale;

    /**
     * Constructs an User object associated with the specified player and balance.
     *
     * @param player   The OfflinePlayer associated with this User model profile.
     * @param balance  The balance associated with the player's User model.
     * @param balance  The locale associated with the player's User model.
     */
    public User(OfflinePlayer player, int balance, String locale) {
        this.player = player;
        this.uuid = String.valueOf(player.getUniqueId());
        this.balance = balance;
        this.locale = locale;
    }

    /**
     * Retrieves the OfflinePlayer associated with this User model profile.
     *
     * @return The OfflinePlayer associated with this User model profile.
     */
    public static OfflinePlayer getPlayer() {
        return player;
    }

    /**
     * Retrieves the balance associated with this player's User model.
     *
     * @return The balance associated with this player's User model.
     */
    public static int getBalance() {
        return balance;
    }

    /**
     * Sets the balance associated with this player's User model.
     *
     * @param balance The new balance to set for this player's User model.
     */
    public static void setBalance(int balance) {
        User.balance = balance;
    }

    /**
     * Retrieves the preferred locale (language) associated with this player's User model.
     *
     * @return The balance associated with this player's User model.
     */
    public static String getLocale() {
        return locale;
    }

    /**
     * Retrieves the uuid associated with this player's User model.
     *
     * @return The balance associated with this player's User model.
     */
    public static String getUuid() {
        return uuid;
    }

    /**
     * sets the preferred locale (language) associated with this player's User model.
     *
     * @param locale The locale to set for this player's User model.
     */
    public static void setLocale(String locale) {
        User.locale = locale;
    }
}

