package dev.relismdev.playlegendquests;

import dev.relismdev.playlegendquests.commands.CommandManager;
import dev.relismdev.playlegendquests.commands.subcommands.ListenerClass;
import dev.relismdev.playlegendquests.storage.DatabaseWrapper;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * PlayLegendQuests plugin - Submission for task "PlayLegend.net - Quest System [03.01.24]".
 * This plugin manages quests within the game environment.
 * Author: Relism
 * Start Date: 08/01/2024
 * GitHub Repository: <a href="https://github.com/Relism/PlaylegendQuests">Relism/PlaylegendQuests</a>
 * Documentation: <a href="https://relism.github.io/PlaylegendQuests/">PlaylegendQuests Documentation</a>
 */
public class Playlegendquests extends JavaPlugin {
    private static Playlegendquests plugin;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        DatabaseWrapper.init(); // Initialize the Database
        getCommand("quests").setExecutor(new CommandManager()); // Register the quests "main" command
        Bukkit.getServer().getPluginManager().registerEvents(new ListenerClass(), this);
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        DatabaseWrapper.disable();
    }

    /**
     * Retrieves the plugin instance.
     *
     * @return The instance of the Playlegendquests plugin.
     */
    public static Playlegendquests getPlugin() {
        return plugin;
    }
}

