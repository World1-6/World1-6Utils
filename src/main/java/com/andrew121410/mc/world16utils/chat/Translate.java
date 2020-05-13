package com.andrew121410.mc.world16utils.chat;

import org.bukkit.ChatColor;

public class Translate {

    public static String translate(String s) {
        String realMessage = LanguageLocale.LANGUAGE_MAP.get(s);
        return color(realMessage);
    }

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
