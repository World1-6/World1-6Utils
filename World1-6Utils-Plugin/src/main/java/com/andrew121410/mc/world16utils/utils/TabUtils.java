package com.andrew121410.mc.world16utils.utils;

import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TabUtils {
    public static List<String> getContainsString(String args, List<String> list) {
        List<String> newList = new ArrayList<>();
        for (String string : list) {
            if (string.contains(args)) {
                newList.add(string);
            }
        }
        return newList;
    }

    public static List<String> getOfflinePlayerNames(List<OfflinePlayer> offlinePlayers) {
        return offlinePlayers.stream()
                .filter(Objects::nonNull) // Filter out null OfflinePlayers
                .filter(offlinePlayer -> offlinePlayer.getName() != null) // Filter out players with null names
                .filter(offlinePlayer -> !offlinePlayer.getName().isEmpty()) // Filter out players with empty names
                .filter(offlinePlayer -> !offlinePlayer.getName().equals("null")) // Filter out players with "null" as their name
                .map(OfflinePlayer::getName) // Map to player names
                .collect(Collectors.toList());
    }

    public static List<String> getOfflinePlayerNames(OfflinePlayer[] offlinePlayers) {
        return getOfflinePlayerNames(Arrays.asList(offlinePlayers));
    }
}
