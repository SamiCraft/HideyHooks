package com.samifying.hideyhooks;

import org.bukkit.ChatColor;

public class PluginUtils {
    public static String sanitize(String string) {
        return ChatColor.stripColor(string)
                .replace("*", "")
                .replace("_", "")
                .replace("#", "")
                .replace(">", "")
                .replace("-", "")
                .replace("`", "")
                .replace("|", "");
    }
}
