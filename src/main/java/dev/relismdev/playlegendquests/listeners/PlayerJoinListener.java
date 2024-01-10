package dev.relismdev.playlegendquests.listeners;

import dev.relismdev.playlegendquests.models.User;
import dev.relismdev.playlegendquests.Playlegendquests;
import dev.relismdev.playlegendquests.storage.DatabaseInterface;
import dev.relismdev.playlegendquests.utils.LocaleManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final Playlegendquests plugin = Playlegendquests.getPlugin();

    public PlayerJoinListener() {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    // Method to handle PlayerJoinEvent
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = DatabaseInterface.getUser(player);
        if(user == null){
            User newUser = new User(player, 0, LocaleManager.getDefaultLocale());
            DatabaseInterface.createUser(newUser);
        }
    }
}
