package dev.relismdev.playlegendquests.utils;

import dev.relismdev.playlegendquests.models.User;
import dev.relismdev.playlegendquests.Playlegendquests;
import dev.relismdev.playlegendquests.storage.DatabaseInterface;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

/**
 * Utility class for handling messaging functionalities within the plugin.
 */
public class msg {

    private static Playlegendquests main = Playlegendquests.getPlugin();

    /**
     * Initializes a new msg instance.
     */
    public msg(){}

    /**
     * Regular expression pattern to match hexadecimal color codes in the format #XXXXXX.
     */
    public static final Pattern HEX_PATTERN = Pattern.compile("&(#[A-Fa-f0-9]{6})");

    /**
     * Represents the color character used in Minecraft chat formatting.
     */
    public static final char COLOR_CHAR = ChatColor.COLOR_CHAR;

    /**
     * Sends a color-translated and placeholder-parsed message to a specific player.
     *
     * @param p       The player to whom the message will be sent.
     * @param message The message to be sent.
     */
    public static void send(Player p, String message) {
        p.sendMessage(translateColorCodes(PlaceholderAPI.setPlaceholders(p, message)));
    }

    /**
     * Sends a message to a player based on their locale.
     *
     * @param player     The player to send the message to.
     * @param messageKey The key for the message to be retrieved.
     */
    public static void sendLocale(Player player, String messageKey) {
        // Retrieve user information from the database
        User user = DatabaseInterface.getUser(player);
        // Determine the appropriate locale based on user information or default if unavailable
        String locale;
        if (user != null) {
            locale = user.getLocale();
        } else {
            locale = LocaleManager.getDefaultLocale();
        }
        // Retrieve the message based on the locale and message key, then send it to the player
        send(player, LocaleManager.getMessage(locale, messageKey));
    }


    /**
     * Sends a color-translated message to all players on the server.
     *
     * @param message The message to be broadcasted.
     */
    public static void broadcast(String message) {
        Bukkit.broadcastMessage(translateColorCodes(message));
    }

    /**
     * Sends a color-translated debug message to the console if the debug configuration is enabled.
     *
     * @param message The debug message to be sent.
     */
    public static void debug(String message){
        if(main.getConfig().getBoolean("debug")){
            log(message);
        }
    }

    /**
     * Sends a color-translated message to the console.
     *
     * @param message The message to be logged in the console.
     */
    public static void log(String message) {
        Bukkit.getConsoleSender().sendMessage(translateColorCodes(message));
    }

    /**
     * Translates color codes in the provided text using the vanilla minecraft color code identifier.
     * It also supports hexadecimal color codes (#XXXXXX format).
     *
     * @param text The text containing color codes to be translated.
     * @return The text with translated color codes.
     */
    public static String translateColorCodes(String text) {
        final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";
        String[] texts = text.split(String.format(WITH_DELIMITER, "&"));
        StringBuilder finalText = new StringBuilder();
        for (int i = 0; i < texts.length; i++) {
            if (texts[i].equalsIgnoreCase("&")) {
                //get the next string
                i++;
                if (texts[i].charAt(0) == '#') {
                    finalText.append(ChatColor.of(texts[i].substring(0, 7)) + texts[i].substring(7));
                } else {
                    finalText.append(ChatColor.translateAlternateColorCodes('&', "&" + texts[i]));
                }
            } else {
                finalText.append(texts[i]);
            }
        }
        return finalText.toString();
    }
}
