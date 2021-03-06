package de.craftersforever.mainsystem;

import de.craftersforever.mainsystem.command.TextCommand;
import de.craftersforever.mainsystem.command.TicketCommand;
import de.craftersforever.mainsystem.listener.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.CachedServerIcon;

import java.io.File;
import java.util.List;
import java.util.logging.Level;

public class MainSystem extends JavaPlugin {

    private String motd, joinSubTitle, joinTitle, tablistHeader, tablistFooter, trelloKey, trelloToken, trelloListID;
    private CachedServerIcon icon = Bukkit.getServerIcon();
    private List<String> tntEnabledWorlds;
    private TrelloWebhook trelloWebhook;
    private boolean shopChestLoaded;
    private boolean shopChestLimitEnabled;
    private VoteManager voteManager;


    public void onEnable() {
        if (!initialiseFiles()) {
            return;
        }
        loadDataFromConfig();
        voteManager = new VoteManager(this);
        registerListeners();
        shopChestLoaded = getServer().getPluginManager().getPlugin("ShopChest") != null;

    }

    public void onDisable() {
        voteManager.saveUnclaimedVotesToConfig();
    }

    private boolean initialiseFiles() {
        this.saveDefaultConfig();
        //Create folder for server icons
        boolean success = (new File("icons")).mkdir();
        if (!success) {
            if (!new File("icons").exists()) {
                Bukkit.getLogger().log(Level.SEVERE, "Failed to create folder icons. Disabling.");
                Bukkit.getPluginManager().disablePlugin(this);
                return false;
            }
        }
        return true;
    }

    private void registerListeners() {
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new ServerPingEventListener(this), this);
        manager.registerEvents(new ExplosionPrimeEventListener(this), this);
        manager.registerEvents(new PlayerAsyncChatEventListener(this), this);
        manager.registerEvents(new PlayerJoinEventListener(this), this);
        manager.registerEvents(new PlayerQuitEventListener(this), this);
        manager.registerEvents(new VotifierEventListener(voteManager), this);
    }

    private void registerCommands() {
        getCommand("teamspeak").setExecutor(new TextCommand(getConfig().getString("command.teamspeak")));
        getCommand("discord").setExecutor(new TextCommand(getConfig().getString("command.discord")));
        getCommand("vote").setExecutor(new TextCommand(getConfig().getString("command.vote")));
        getCommand("ticket").setExecutor(new TicketCommand(this, trelloWebhook));
    }

    private void loadDataFromConfig() {
        reloadConfig();
        //Load Values for ServerlistPing
        motd = Util.convertToOutputString(getConfig().getString("ping.motd"));
        String filename = getConfig().getString("ping.image");
        try {
            icon = Bukkit.loadServerIcon(new File("icons/" + getConfig().getString("ping.image")));
        } catch (Exception ex) {
            Bukkit.getLogger().log(Level.WARNING, "Failed to load server icon \"" + filename + "\" from folder icons");
        }
        //Load Values for PlayerJoin
        joinTitle = Util.convertToOutputString(getConfig().getString("join.title"));
        joinSubTitle = Util.convertToOutputString(getConfig().getString("join.subtitle"));
        //Tablist
        tablistHeader = Util.convertToOutputString(getConfig().getString("tablist.header"));
        tablistFooter = Util.convertToOutputString(getConfig().getString("tablist.footer"));
        //Load Values for TNT
        tntEnabledWorlds = getConfig().getStringList("tnt.enabledworlds");
        trelloKey = getConfig().getString("trello.key");
        trelloToken = getConfig().getString("trello.token");
        trelloListID = getConfig().getString("trello.listID");
        trelloWebhook = new TrelloWebhook(trelloListID, trelloKey, trelloToken);
        if(voteManager!=null) {
            voteManager.loadMessagesAndRewardsFromConfig();
        }
        registerCommands();
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("reloadMainSystem")) {
            loadDataFromConfig();
        }
        if (command.getName().equalsIgnoreCase("clearchat")) {
            for (Player p : getServer().getOnlinePlayers()) {
                if (!p.hasPermission("clearchat.ignore")) {
                    for (int i = 0; i < 100; i++) {
                        p.sendMessage("");
                    }
                }
                p.sendMessage("§2Der Chat wurde von §6" + sender.getName() + " §2geleert!");
            }
            sender.sendMessage("§2Der Chat wurde erfolgreich geleert. Die Nachrichten tauchen aber weiter im Log auf.");
        }
        return true;
    }

    //Getter-Methods
    public String getMotd() {
        return motd;
    }

    public CachedServerIcon getIcon() {
        return icon;
    }

    public String getJoinSubtitle() {
        return joinSubTitle;
    }

    public String getJoinTitle() {
        return joinTitle;
    }

    public List<String> getTntEnabledWorlds() {
        return tntEnabledWorlds;
    }

    public String getTablistHeader() {
        return tablistHeader;
    }

    public String getTablistFooter() {
        return tablistFooter;
    }
}
