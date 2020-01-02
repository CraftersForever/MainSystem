package de.craftersforever.mainsystem.listener;

import de.craftersforever.mainsystem.MainSystem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ExplosionPrimeEventListener implements Listener {

    private MainSystem mainSystem;

    public ExplosionPrimeEventListener(MainSystem mainSystem) {
        this.mainSystem = mainSystem;
    }


    @EventHandler
    public void onTnTPrimeEvent(ExplosionPrimeEvent e) {
        if (e.isCancelled()) return;
        //Only checks for tnt
        if (e.getEntityType() != EntityType.PRIMED_TNT) return;
        //Checks if tnt is enabled in this world. If worldname not in list, tnt is disabled.
        if (mainSystem.getTntEnabledWorlds().contains(e.getEntity().getWorld().getName())) return;
        //We assume here that tnt is willingly disabled and thus should not work. So we cancel the event and drop the tnt
        e.setCancelled(true);
        Location loc = e.getEntity().getLocation();
        loc.getWorld().dropItem(loc, new ItemStack(Material.TNT));
    }
}
