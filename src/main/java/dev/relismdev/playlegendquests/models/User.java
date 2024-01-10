package dev.relismdev.playlegendquests.models;

import org.bukkit.OfflinePlayer;

/**
 * Represents an economy profile associated with a player, containing information about their balance.
 */
public class User {

    private static OfflinePlayer player; // The associated player
    private static String uuid;
    private static int balance; // The player's balance
    private static String locale;

    /**
     * Constructs an Economy object associated with the specified player and balance.
     *
     * @param p        The OfflinePlayer associated with this User model profile.
     * @param balance  The balance associated with the player's User model.
     */
    public User(OfflinePlayer p, int balance, String locale) {
        this.player = p;
        this.uuid = String.valueOf(p.getUniqueId());
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
     * @return The balance associated with this player's economy.
     */
    public static String getLocale() {
        return locale;
    }

    /**
     * Retrieves the uuid associated with this player's User model.
     *
     * @return The balance associated with this player's economy.
     */
    public static String getUuid() {
        return uuid;
    }

    /**
     * sets the preferred locale (language) associated with this player's User model.
     *
     * @return The balance associated with this player's User model.
     */
    public static void setLocale(String locale) {
        User.locale = locale;
    }
}

