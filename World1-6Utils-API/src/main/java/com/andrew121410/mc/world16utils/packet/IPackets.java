package com.andrew121410.mc.world16utils.packet;

import com.andrew121410.mc.world16utils.blocks.MarkerColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public interface IPackets {

    default void sendDebugCreateMarkerPacket(World world, Location blockPosition, MarkerColor markerColor, String message) {
        for (Player player : world.getPlayers())
            this.sendDebugCreateMarkerPacket(player, blockPosition, markerColor, message);
    }

    default void sendDebugCreateMarkerPacket(Player player, Location blockPosition, MarkerColor markerColor, String message) {
        this.sendDebugCreateMarkerPacket(player, blockPosition, markerColor.getColor(), message, Integer.MAX_VALUE);
    }

    void sendDebugCreateMarkerPacket(Player player, Location blockPosition, int color, String message, int duration);

    void sendDebugGameTestClearPacket(Player player);
}
