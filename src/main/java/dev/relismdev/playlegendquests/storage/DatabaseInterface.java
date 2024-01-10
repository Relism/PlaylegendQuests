package dev.relismdev.playlegendquests.storage;

import dev.relismdev.playlegendquests.models.Quest;
import dev.relismdev.playlegendquests.models.User;
import dev.relismdev.playlegendquests.utils.msg;
import org.bukkit.OfflinePlayer;

import java.sql.SQLException;
import java.util.List;

public class DatabaseInterface {

    /**
     * Attempts to create a new quest in the database.
     *
     * @param quest The Quest object to be created in the database.
     * @return True if the creation is successful; otherwise, false.
     */
    public static boolean createQuest(Quest quest){
        try {
            return DatabaseWrapper.createQuest(quest);
        } catch (SQLException e) {
            errorHandle(e);
            return false;
        }
    }

    /**
     * Retrieves a Quest object from the database based on the provided ID.
     *
     * @param name The name of the quest to retrieve.
     * @return A Quest object containing the details of the requested quest if found; otherwise, null.
     */
    public static Quest getQuest(String name){
        try {
            return DatabaseWrapper.getQuest(name);
        } catch (SQLException e) {
            errorHandle(e);
            return null;
        }
    }

    /**
     * Updates an existing quest in the database.
     *
     * @param quest The modified Quest object to be updated in the database.
     * @return True if the update is successful; otherwise, false.
     */
    public static boolean updateQuest(Quest quest){
        try {
            return DatabaseWrapper.updateQuest(quest);
        } catch (SQLException e) {
            errorHandle(e);
            return false;
        }
    }

    /**
     * Deletes a quest from the database based on the provided ID.
     *
     * @param id The unique identifier of the quest to be deleted.
     * @return True if the deletion is successful; otherwise, false.
     */
    public static boolean deleteQuest(String id){
        try {
            return DatabaseWrapper.deleteQuest(id);
        } catch (SQLException e) {
            errorHandle(e);
            return false;
        }
    }

    /**
     * Retrieves a list of Quest objects from the database.
     *
     * @return A List containing Quest objects; returns null in case of an error.
     */
    public static List<Quest> getQuests(){
        try {
            return DatabaseWrapper.getQuests();
        } catch (SQLException e) {
            errorHandle(e);
            return null;
        }
    }

    /**
     * Attempts to create a new user in the database.
     *
     * @param user The Quest object to be created in the database.
     * @return True if the creation is successful; otherwise, false.
     */
    public static boolean createUser(User user){
        try {
            return DatabaseWrapper.createUser(user);
        } catch (SQLException e) {
            errorHandle(e);
            return false;
        }
    }

    /**
     * Retrieves an User object for the specified OfflinePlayer from the database.
     *
     * @param player The OfflinePlayer to retrieve the User object for.
     * @return The User object associated with the player; null if not found or an error occurs.
     */
    public static User getUser(OfflinePlayer player){
        try {
            return DatabaseWrapper.getUser(player);
        } catch (SQLException e){
            errorHandle(e);
            return null;
        }
    }

    /**
     * Updates an existing quest in the database.
     *
     * @param user The modified User object to be updated in the database.
     * @return True if the update is successful; otherwise, false.
     */
    public static boolean updateUser(User user){
        try {
            return DatabaseWrapper.updateUser(user);
        } catch (SQLException e) {
            errorHandle(e);
            return false;
        }
    }

    /**
     * Handles SQL Exceptions by logging them.
     *
     * @param e The SQLException to handle.
     */
    private static void errorHandle(SQLException e){
        msg.log("&#ff6961SQL Exception: &#fdfd96" + e.getMessage());
    }
}
