package dev.relismdev.playlegendquests.Models;

import org.bukkit.OfflinePlayer;

/**
 * Represents an economy profile associated with a player, containing information about their balance.
 */
public class Economy {

    private static OfflinePlayer player; // The associated player
    private static int balance; // The player's balance

    /**
     * Constructs an Economy object associated with the specified player and balance.
     *
     * @param p        The OfflinePlayer associated with this economy profile.
     * @param balance  The balance associated with the player's economy.
     */
    public Economy(OfflinePlayer p, int balance) {
        this.player = p;
        this.balance = balance;
    }

    /**
     * Retrieves the OfflinePlayer associated with this economy profile.
     *
     * @return The OfflinePlayer associated with this economy profile.
     */
    public static OfflinePlayer getPlayer() {
        return player;
    }

    /**
     * Retrieves the balance associated with this player's economy.
     *
     * @return The balance associated with this player's economy.
     */
    public static int getBalance() {
        return balance;
    }

    /**
     * Sets the balance associated with this player's economy.
     *
     * @param balance The new balance to set for this player's economy.
     */
    public static void setBalance(int balance) {
        Economy.balance = balance;
    }
}

