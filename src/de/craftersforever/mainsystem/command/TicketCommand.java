package de.craftersforever.mainsystem.command;

import de.craftersforever.mainsystem.MainSystem;
import de.craftersforever.mainsystem.TrelloWebhook;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TicketCommand implements CommandExecutor {
    private TrelloWebhook trelloWebhook;
    private MainSystem mainSystem;
    public TicketCommand(MainSystem mainSystem, TrelloWebhook trelloWebhook) {
        this.mainSystem = mainSystem;
        this.trelloWebhook = trelloWebhook;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)){
            commandSender.sendMessage("Only players can use this command");
            return false;
        }
        Player p = (Player) commandSender;
        if(strings.length <8){
            p.sendMessage("§aBitte beschreibe dein Problem genauer, damit wir dir besser helfen können.");
            return true;
        }
        String message = String.join(" ",strings);
        Bukkit.getScheduler().runTaskAsynchronously(mainSystem, () -> {
            if(trelloWebhook.createNewCard(p.getName(), message, p.getLocation())){
                p.sendMessage("§aDas Ticket wurde erfolgreich erstellt.");
            }else{
                p.sendMessage("§4Das Ticket konnte nicht erstellt werden. Bitte wende dich an ein Teammitglied.");
            }
        });

        return true;

    }
}
