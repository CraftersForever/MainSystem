package de.craftersforever.mainsystem.listener;

import com.vexsoftware.votifier.model.VotifierEvent;
import de.craftersforever.mainsystem.VoteManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.logging.Level;

public class VotifierEventListener implements Listener {
    private VoteManager voteManager;

    public VotifierEventListener(VoteManager voteManager) {
        this.voteManager = voteManager;
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onVote(VotifierEvent event) {
        String username = event.getVote().getUsername();
        if (!username.matches("[a-zA-Z0-9_]{1,16}")) {
            Bukkit.getLogger().log(Level.INFO, "Received vote with illegal username " + username + " from service "
                    + event.getVote().getServiceName() + " with IP " + event.getVote().getAddress());
            return;
        }
        voteManager.processVote(event.getVote());
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        voteManager.processJoin(event.getPlayer());
    }
}
