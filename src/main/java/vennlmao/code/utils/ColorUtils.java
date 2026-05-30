package vennlmao.code.utils;

import org.bukkit.ChatColor;

public class ColorUtils {
    public static String colorize(String text) {
        if (text == null) return "";
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
