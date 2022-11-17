package com.andrew121410.mc.world16utils.chat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;

public class Translate {

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String chat(String s) {
        return color(s);
    }

    public static Component colorc(String s) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(s);
    }

    public static Component miniMessage(String s) {
        return MiniMessage.miniMessage().deserialize(s);
    }
}
