package de.craftersforever.mainsystem;

import org.bukkit.ChatColor;

public class Util {

    /**
     * Converts an input string to an minecraft output string. Translates color codes and replaces "/n" with a line break
     * @param input
     * @return translated string
     */
    public static String convertToOutputString(String input) {
        String output = ChatColor.translateAlternateColorCodes('&', input);
        output = output.replace("/n", "\n");
        return output;

    }
}
