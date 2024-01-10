package dev.relismdev.playlegendquests.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to manage locale file configurations and their relative messages within the plugin.
 */
public class LocaleManager {

    private static final Map<String, YamlConfiguration> locales = new HashMap<>();
    private static String defaultLocale = "en_US";

    /**
     * Initializes the LocaleManager by loading the default locale and all available locales.
     *
     * @param plugin The JavaPlugin instance.
     */
    public static void init(JavaPlugin plugin) {
        loadDefaultLocale(plugin);
        loadLocales(plugin);
    }

    /**
     * Loads the default locale YAML file from a URL if it doesn't exist locally,
     * and saves it in the plugin's data folder.
     *
     * @param plugin The JavaPlugin instance.
     */
    private static void loadDefaultLocale(JavaPlugin plugin) {
        File defaultLocaleFile = new File(plugin.getDataFolder(), "locale/" + defaultLocale + ".yml");

        if (!defaultLocaleFile.exists()) {
            try {
                // Create the directories if they don't exist
                defaultLocaleFile.getParentFile().mkdirs();

                // URL for the default locale YAML file
                String defaultLocaleUrl = "https://github.dev/Relism/PlaylegendQuests/master/assets/locales/" + defaultLocale + ".yml";
                URL url = new URL(defaultLocaleUrl);
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

                // Write the content to the default locale file
                FileWriter fileWriter = new FileWriter(defaultLocaleFile);
                BufferedWriter out = new BufferedWriter(fileWriter);
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    out.write(inputLine);
                    out.newLine();
                }

                // Close resources
                in.close();
                out.close();

                // Log success message
                msg.log("Downloaded and saved " + defaultLocale + ".yml from GitHub raw URL.");
            } catch (IOException e) {
                // Log error if there's an issue downloading or saving the file
                msg.log("Error downloading or saving " + defaultLocale + ".yml: " + e.getMessage());
            }
        } else {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(defaultLocaleFile);
            locales.put(defaultLocale, defaultConfig);
        }
    }

    /**
     * Loads all available locales from the plugin's data folder.
     *
     * @param plugin The JavaPlugin instance.
     */
    private static void loadLocales(JavaPlugin plugin) {
        File localeFolder = new File(plugin.getDataFolder(), "locale");

        if (!localeFolder.exists()) {
            localeFolder.mkdirs();
        }

        File[] localeFiles = localeFolder.listFiles((dir, name) -> name.endsWith(".yml"));

        if (localeFiles != null) {
            for (File file : localeFiles) {
                String localeName = file.getName().replace(".yml", "");
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                locales.put(localeName, config);
            }
        }
    }

    /**
     * Retrieves a message from the specified locale using the given key.
     * If the message is not found in the specified locale, it tries to retrieve it from the default locale.
     * If not found in the default locale, returns an error message.
     *
     * @param locale The locale for the message.
     * @param key    The key to retrieve the message.
     * @return The message corresponding to the locale and key, or a default error message if not found.
     */
    public static String getMessage(String locale, String key) {
        YamlConfiguration config = locales.get(locale);
        if (config != null && config.contains(key)) {
            return config.getString(key);
        } else {
            YamlConfiguration defaultConfig = locales.get(defaultLocale);
            if (defaultConfig != null && defaultConfig.contains(key)) {
                return defaultConfig.getString(key);
            } else {
                return "Message not found for locale " + locale + " and key " + key;
            }
        }
    }

    /**
     * Reloads all available locales.
     *
     * @param plugin The JavaPlugin instance.
     */
    public static void reloadLocales(JavaPlugin plugin) {
        locales.clear();
        loadLocales(plugin);
    }

    /**
     * Gets the default locale.
     *
     * @return The default locale.
     */
    public static String getDefaultLocale() {
        return defaultLocale;
    }
}


