package dev.relismdev.playlegendquests.storage;

import dev.relismdev.playlegendquests.Models.Economy;
import dev.relismdev.playlegendquests.Models.Quest;
import dev.relismdev.playlegendquests.Playlegendquests;
import dev.relismdev.playlegendquests.utils.msg;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static dev.relismdev.playlegendquests.utils.ItemUtils.deserializeItemStack;
import static dev.relismdev.playlegendquests.utils.ItemUtils.serializeItemStack;

public class DatabaseWrapper {

    private static Playlegendquests main = Playlegendquests.getPlugin();
    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;
    private static String DB_NAME; // Added field for the database name
    private static final int MAX_CONNECTIONS = 10;
    private static BlockingQueue<Connection> connectionPool;

    /**
     * Initializes the database connection settings, creates connection pool,
     * and sets up required database tables for quests and economy.
     * Retrieves database connection settings directly from the plugin configuration.
     */
    public static void init() {
        DB_URL = main.getConfig().getString("db_url");
        DB_USER = main.getConfig().getString("db_username");
        DB_PASSWORD = main.getConfig().getString("db_password");
        DB_NAME = main.getConfig().getString("db_name");
        initializeConnectionPool();
        createQuestsTable();
        createEconomyTable();
    }

    /**
     * Initializes the connection pool by creating database connections and adding them to the pool.
     * This method attempts to create the maximum allowed number of connections.
     * Any failed connection attempts will be logged.
     */
    public static void initializeConnectionPool() {
        boolean connectionEstablished = false; // Track if at least one connection was established
        connectionPool = new LinkedBlockingQueue<>(MAX_CONNECTIONS);
        for (int i = 0; i < MAX_CONNECTIONS; i++) {
            try {
                Connection connection = DriverManager.getConnection(getFullDBURL(), DB_USER, DB_PASSWORD);
                connectionPool.offer(connection);
                connectionEstablished = true;
            } catch (SQLException e) {
                msg.log("Failed to establish database connection: " + e.getMessage());
            }
        }

        if (connectionEstablished) {
            msg.log("Database connection established.");
        }
    }

    /**
     * Retrieves a database connection from the connection pool.
     * This method acquires an available database connection from the pool, if present.
     * If a connection is available, it is retrieved and logged as obtained from the pool.
     * If no connection is immediately available, this method waits until one becomes available.
     *
     * @implNote This method is responsible for fetching a database connection from the pool.
     *           It utilizes the connectionPool's blocking behavior to wait for an available connection.
     *           Upon obtaining a connection, it logs the successful retrieval from the pool.
     *           Any interruptions during the wait for a connection are logged, and null is returned.
     *
     * @return A database connection object retrieved from the pool if available; otherwise, null.
     */
    public static Connection getConnection() {
        try {
            Connection connection = connectionPool.take();
            msg.log("Database connection retrieved from pool.");
            return connection;
        } catch (InterruptedException e) {
            msg.log("Interrupted while getting a database connection: " + e.getMessage());
            return null;
        }
    }


    /**
     * Releases a database connection back to the connection pool for reusability.
     * If the provided connection object is valid, it returns it to the connection pool
     * for potential reuse by other parts of the application.
     *
     * @param connection The database connection object to be released.
     *                   If null, no action is taken.
     *                   If valid, it is returned to the connection pool for reuse.
     * @implNote This method aims to manage database connection resources effectively
     *           by returning connections to the pool when they're no longer needed,
     *           promoting resource efficiency and reuse within the application.
     */
    public static void releaseConnection(Connection connection) {
        if (connection != null) {
            try {
                connectionPool.offer(connection);
                msg.log("Database connection released back to pool.");
            } catch (Exception e) {
                msg.log("Failed to release database connection: " + e.getMessage());
            }
        }
    }


    /**
     * Closes all the database connections currently present in the connection pool.
     * It iterates through the connection pool, attempts to close each connection,
     * and logs the outcome of the closure operation.
     *
     * @implNote This method ensures that all active database connections in the pool are closed.
     *           It iterates through the connection pool, attempting to close each connection,
     *           and logs successful closures or any encountered SQLExceptions.
     */
    public static void closeConnections() {
        for (Connection connection : connectionPool) {
            try {
                connection.close();
                msg.log("Database connection closed.");
            } catch (SQLException e) {
                msg.log("Failed to close database connection: " + e.getMessage());
            }
        }
    }


    /**
     * Creates the 'quests' table in the connected database if it doesn't already exist.
     * The table includes columns for quest details: ID, name, description, reward coins, and reward item.
     * If the table creation fails or an SQL exception occurs during execution, it logs an error.
     *
     * @throws SQLException If an SQL exception occurs during table creation or execution.
     */
    private static void createQuestsTable() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            String initializeQuestsTableQuery = "CREATE TABLE IF NOT EXISTS quests (id VARCHAR(255) PRIMARY KEY, name VARCHAR(255), description TEXT, reward_coins BIGINT, reward_item TEXT)";
            statement.execute(initializeQuestsTableQuery);

            releaseConnection(connection);
        } catch (SQLException e) {
            // Log error if table creation fails
            msg.log("Failed to create 'quests' table: " + e.getMessage());
        }
    }

    /**
     * Creates the 'economy' table in the connected database if it doesn't already exist.
     * The table includes columns for player UUID as primary key and balance.
     * If the table creation fails or an SQL exception occurs during execution, it logs an error.
     *
     * @throws SQLException If an SQL exception occurs during table creation or execution.
     */
    private static void createEconomyTable() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            String initializeEconomyTableQuery = "CREATE TABLE IF NOT EXISTS economy (uuid VARCHAR(36) PRIMARY KEY, balance BIGINT)";
            statement.execute(initializeEconomyTableQuery);

            releaseConnection(connection);
        } catch (SQLException e) {
            // Log error if table creation fails
            msg.log("Failed to create 'economy' table: " + e.getMessage());
        }
    }

    /**
     * Retrieves a Quest object from the database based on the provided ID.
     * This method executes a database query to fetch quest details associated with the given ID.
     * If found, it constructs and returns a Quest object encapsulating the retrieved data.
     * If not found or if an error occurs during the database query, it returns null.
     *
     * @param id The unique identifier of the quest to retrieve.
     * @return A Quest object containing the details of the requested quest if found; otherwise, null.
     */
    public static Quest getQuest(String id) throws SQLException {
        String query = "SELECT * FROM quests WHERE id = ?";
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, id);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String questId = resultSet.getString("id");
            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            int rewardCoins = resultSet.getInt("reward_coins");
            String serializedItem = resultSet.getString("reward_item"); // Assuming reward_item is stored as a serialized string

            ItemStack rewardItem = deserializeItemStack(serializedItem);

            return new Quest(questId, name, description, rewardCoins, rewardItem);
        }
        releaseConnection(connection);
        return null;
    }

    /**
     * Inserts a new Quest object into the quests database table.
     *
     * This method constructs an INSERT query and executes it to add a new Quest object into the 'quests' table
     * of the connected database. It serializes the reward_item ItemStack to store it as a base64 encoded string in the database.
     *
     * @param quest The Quest object to be inserted into the database.
     * @return True if the insertion is successful; otherwise, false.
     * @throws NullPointerException If the provided Quest object is null.
     * @throws IllegalStateException If there's a SQL exception during the insertion process.
     */
    public static boolean createQuest(Quest quest) throws SQLException {
        String query = "INSERT INTO quests (id, name, description, reward_coins, reward_item) VALUES (?, ?, ?, ?, ?)";
        Connection connection = null;
        PreparedStatement statement = null;

        connection = getConnection();
        statement = connection.prepareStatement(query);

        statement.setString(1, quest.getId());
        statement.setString(2, quest.getName());
        statement.setString(3, quest.getDescription());
        statement.setInt(4, quest.getReward_coins());
        statement.setString(5, serializeItemStack(quest.getReward_item()));

        int rowsInserted = statement.executeUpdate();

        releaseConnection(connection);
        return rowsInserted > 0;
    }

    /**
     * Updates an existing quest in the quests table.
     *
     * @param quest The modified Quest object to be updated in the database.
     * @return True if the update is successful; otherwise, false.
     * @throws NullPointerException     If the provided Quest object is null.
     * @throws IllegalStateException    If there's a SQL exception during the update process.
     */
    public static boolean updateQuest(Quest quest) throws SQLException {
        if (quest == null) {
            throw new NullPointerException("Provided Quest object is null.");
        }

        Connection connection = null;
        PreparedStatement statement = null;

        connection = getConnection();
        statement = connection.prepareStatement("UPDATE quests SET name = ?, description = ?, reward_coins = ?, reward_item = ? WHERE id = ?");

        statement.setString(1, quest.getName());
        statement.setString(2, quest.getDescription());
        statement.setInt(3, quest.getReward_coins());
        statement.setString(4, serializeItemStack(quest.getReward_item()));
        statement.setString(5, quest.getId());

        int rowsAffected = statement.executeUpdate();

        releaseConnection(connection);
        return rowsAffected > 0; // Returns true if at least one row was affected (updated)
    }

    /**
     * Deletes a quest entry from the 'quests' table based on the provided quest ID.
     *
     * @param id The unique identifier of the quest to be deleted from the 'quests' table.
     * @return True if the quest deletion is successful (at least one row affected); otherwise, false.
     * @throws SQLException If an SQL exception occurs during the deletion process.
     */
    public static boolean deleteQuest(String id) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("DELETE FROM quests WHERE id = ?");
        statement.setString(1, id);

        int rowsAffected = statement.executeUpdate();
        releaseConnection(connection);

        return rowsAffected > 0;
    }

    /**
     * Retrieves a list of all quests available from the 'quests' table in the database.
     * Executes an SQL SELECT query to obtain all quest IDs, then fetches each individual quest
     * using the 'getQuest' method and constructs a list of Quest objects.
     *
     * @return A List containing all available quests from the 'quests' table; an empty list if no quests are found.
     * @throws SQLException If an SQL exception occurs during the retrieval process.
     */
    public static List<Quest> getQuests() throws SQLException {
        List<Quest> questList = new ArrayList<>();
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT id FROM quests");

        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            String questId = resultSet.getString("id");
            Quest quest = getQuest(questId);
            if (quest != null) {
                questList.add(quest);
            } else {
                // Handling might be necessary here for cases where a quest is not retrieved
                // or for any exceptional situations.
            }
        }
        releaseConnection(connection);
        return questList;
    }

    /**
     * Retrieves the economy details for the provided player from the 'economy' table in the database.
     * Constructs an Economy object encapsulating the player's economy details if found.
     *
     * @param player The OfflinePlayer object for whom the economy details are to be retrieved.
     * @return An Economy object containing the economy details of the specified player if found; otherwise, null.
     * @throws SQLException If an SQL exception occurs during the retrieval process.
     */
    public static Economy getEconomy(OfflinePlayer player) throws SQLException {
        String query = "SELECT * FROM economy WHERE uuid = ?";
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, player.getUniqueId().toString());
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            int balance = resultSet.getInt("balance");
            return new Economy(player, balance);
        }
        releaseConnection(connection);
        return null;
    }


    /**
     * Generates and retrieves the complete database URL for establishing a connection.
     * Constructs the database URL based on the configured database URL and database name,
     * ensuring that SSL usage is explicitly set to 'false'.
     *
     * @return The complete JDBC URL for the database connection with SSL disabled.
     */
    private static String getFullDBURL() {
        return "jdbc:mysql://" + DB_URL + "/" + DB_NAME + "?useSSL=false"; // Adjust useSSL as needed
    }


    /**
     * Disables the database wrapper by closing all active connections.
     * This method ensures the graceful shutdown of database connections
     * when the plugin is disabled or needs to halt its database interaction.
     * It calls the 'closeConnections' method to release all active database connections.
     * It's recommended to invoke this method during plugin shutdown or when the
     * database interaction is no longer required to prevent resource leaks.
     */
    public static void disable() {
        closeConnections();
    }
}


