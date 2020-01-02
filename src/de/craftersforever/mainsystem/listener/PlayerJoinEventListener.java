package de.craftersforever.mainsystem.listener;

import de.craftersforever.mainsystem.MainSystem;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinEventListener implements Listener {
    private MainSystem mainSystem;

    public PlayerJoinEventListener(MainSystem mainSystem) {
        this.mainSystem = mainSystem;
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        String title = ChatColor.translateAlternateColorCodes('&', mainSystem.getJoinTitle());
        String subtitle = ChatColor.translateAlternateColorCodes('&', mainSystem.getJoinSubtitle());
        e.getPlayer().setPlayerListHeaderFooter(mainSystem.getTablistHeader(), mainSystem.getTablistFooter());

        e.getPlayer().sendTitle(title, subtitle, 0, 100, 20);
        mainSystem.getServer().getScheduler().scheduleSyncDelayedTask(mainSystem, new Runnable() {

            @Override
            public void run() {
                sendChatSoundToPlayers();
            }
        }, 2L);
    }


    public void sendChatSoundToPlayers() {
        for (Player player : mainSystem.getServer().getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.ENTITY_SHEEP_STEP, 1.0F, 1.0F);
        }
    }

}
