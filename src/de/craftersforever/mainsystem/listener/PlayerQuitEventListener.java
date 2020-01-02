package de.craftersforever.mainsystem.listener;

import de.craftersforever.mainsystem.MainSystem;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class PlayerQuitEventListener implements Listener {
    private MainSystem mainSystem;

    public PlayerQuitEventListener(MainSystem mainSystem) {
        this.mainSystem = mainSystem;
    }

    //Quitsound
    @EventHandler
    public void onPlayerQuitEvent(final PlayerQuitEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(mainSystem, new Runnable() {
            @Override
            public void run() {
                sendChatSoundToPlayers();
            }
        });

    }

    public void sendChatSoundToPlayers() {
        for (Player player : mainSystem.getServer().getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.ENTITY_COW_STEP, 1.0F, 1.0F);
        }
    }

}
