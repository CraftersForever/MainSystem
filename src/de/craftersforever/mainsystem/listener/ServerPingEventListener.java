package de.craftersforever.mainsystem.listener;

import de.craftersforever.mainsystem.MainSystem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.util.CachedServerIcon;

public class ServerPingEventListener implements Listener {
    private MainSystem mainSystem;



    public ServerPingEventListener(MainSystem mainSystem){
        this.mainSystem = mainSystem;
    }

    @EventHandler
    public void onServerPingEvent(ServerListPingEvent event){
        event.setMotd(mainSystem.getMotd());
        event.setServerIcon(mainSystem.getIcon());
    }

}
