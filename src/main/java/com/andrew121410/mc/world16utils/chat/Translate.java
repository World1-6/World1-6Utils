package com.andrew121410.mc.world16utils.chat;

import org.bukkit.ChatColor;

public class Translate {

    public static String chat(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
