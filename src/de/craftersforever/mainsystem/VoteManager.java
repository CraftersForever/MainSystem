package de.craftersforever.mainsystem;

import com.vexsoftware.votifier.model.Vote;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class VoteManager {
    private final MainSystem mainSystem;
    private final HashMap<UUID, Integer> votepoints = new HashMap<>();
    private YamlConfiguration savedVotesConfig;
    private File savedVotesFile;
    private List<String> rewards;
    private String voteBroadcast;
    private String voteThankMessage;

    public VoteManager(MainSystem mainSystem) {
        this.mainSystem = mainSystem;
        loadVotesFromConfig();
        loadMessagesAndRewardsFromConfig();
    }

    public void loadMessagesAndRewardsFromConfig() {
        rewards = mainSystem.getConfig().getStringList("vote.rewards");
        voteBroadcast = ChatColor.translateAlternateColorCodes('&', mainSystem.getConfig().getString("vote.broadcast"));
        voteThankMessage = ChatColor.translateAlternateColorCodes('&', mainSystem.getConfig().getString("vote.thank"));
    }

    private void loadVotesFromConfig() {
        //Config file is in folder of plugins and is named savedVotes.yml
        savedVotesFile = new File(mainSystem.getDataFolder(), "savedVotes.yml");
        //Create if not exists
        if (!savedVotesFile.exists()) {
            savedVotesFile.getParentFile().mkdirs();
            mainSystem.saveResource("savedVotes.yml", false);
        }
        //File should exist here, otherwise an exeption will follow
        savedVotesConfig = new YamlConfiguration();
        try {
            savedVotesConfig.load(savedVotesFile);
        } catch (IOException | InvalidConfigurationException ex) {
            ex.printStackTrace();
        }
        for (String key : savedVotesConfig.getKeys(false)) {
            UUID uuid = UUID.fromString(key);
            int votes = savedVotesConfig.getInt(key);
            votepoints.put(uuid, votes);
        }
    }

    public void saveUnclaimedVotesToConfig() {
        for (String key : savedVotesConfig.getKeys(false)) {
            savedVotesConfig.set(key, null);
        }
        for (UUID uuid : votepoints.keySet()) {
            savedVotesConfig.set(uuid.toString(), votepoints.get(uuid));
        }
        try {
            savedVotesConfig.save(savedVotesFile);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot save unclaimed votes!");
            e.printStackTrace();
        }

    }

    public void processVote(Vote vote) {
        Player player = mainSystem.getServer().getPlayer(vote.getUsername());
        if (player == null) {
            OfflinePlayer offlinePlayer = mainSystem.getServer().getOfflinePlayer(vote.getUsername());
            if (!offlinePlayer.hasPlayedBefore()) {
                Bukkit.getLogger().log(Level.INFO, "Received vote by a unknown player named " + vote.getUsername() + ". Gonna ignore the vote.");
                return;
            }
            mainSystem.getServer().broadcastMessage(voteBroadcast.replace("%player%", offlinePlayer.getName()));
            UUID uuid = offlinePlayer.getUniqueId();
            if (votepoints.containsKey(uuid)) {
                votepoints.put(uuid, votepoints.get(uuid) + 1);
            } else {
                votepoints.put(uuid, 1);
            }
        } else {
            mainSystem.getServer().broadcastMessage(voteBroadcast.replace("%player%", player.getName()));
            player.sendMessage(voteThankMessage.replace("%player%", player.getName()));
            grantReward(player);
        }
    }

    private void grantReward(Player player) {
        for (String reward : rewards) {
            String command = reward.replace("%player%", player.getName());
            System.out.println("Performing command " + command);
            mainSystem.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }

    public void processJoin(Player player) {
        if (votepoints.containsKey(player.getUniqueId())) {
            Bukkit.getScheduler().runTaskLater(mainSystem, new Runnable() {
                @Override
                public void run() {
                    if (!player.isOnline()) return;
                    player.sendMessage(voteThankMessage.replace("%player%", player.getName()));
                    for (int i = 0; i < votepoints.get(player.getUniqueId()); i++) {
                        grantReward(player);
                    }
                    votepoints.remove(player.getUniqueId());
                }
            }, 30);
        }
    }
}
