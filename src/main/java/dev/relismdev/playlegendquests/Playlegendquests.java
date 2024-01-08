package dev.relismdev.playlegendquests;

import dev.relismdev.playlegendquests.storage.DatabaseWrapper;
import dev.relismdev.playlegendquests.utils.DatabaseInterface;
import dev.relismdev.playlegendquests.utils.msg;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Playlegendquests extends JavaPlugin {

    private static Playlegendquests plugin;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        DatabaseWrapper.init();
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        DatabaseWrapper.disable();
    }

    public static Playlegendquests getPlugin(){ return plugin; }
}
