package de.craftersforever.mainsystem.command;

import de.craftersforever.mainsystem.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TextCommand implements CommandExecutor {
    private String output;

    public TextCommand(String output){
        this.output = Util.convertToOutputString(output);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        sender.sendMessage(output);
        return true;
    }
}
