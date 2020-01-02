package de.craftersforever.mainsystem.listener;

import de.craftersforever.mainsystem.MainSystem;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

public class PlayerAsyncChatEventListener implements Listener {
    private MainSystem mainSystem;

    public PlayerAsyncChatEventListener(MainSystem mainSystem) {
        this.mainSystem = mainSystem;
    }

    //Chatsound
    @EventHandler
    public void onPlayerAsyncChatEvent(AsyncPlayerChatEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(mainSystem, new Runnable() {
            @Override
            public void run() {
                sendChatSoundToPlayers();
            }
        });

    }

    public void sendChatSoundToPlayers() {
        for (Player player : mainSystem.getServer().getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0F, 1.0F);
        }
    }
}
