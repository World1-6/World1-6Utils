package com.andrew121410.mc.world16utils.packet;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Packet_V1_12_R1 implements IPackets {

    public Packet_V1_12_R1() {
    }

    @Override
    public void sendDebugCreateMarkerPacket(Player player, Location blockPosition, int color, String message, int duration) {

    }

    @Override
    public void sendDebugGameTestClearPacket(Player player) {
    }
}
