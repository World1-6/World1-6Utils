package com.andrew121410.mc.world16utils.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.MinecraftKey;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class Packet_V1_17_R1 implements IPackets {

    private ProtocolManager protocolManager;

    public Packet_V1_17_R1() {
        this.protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void sendDebugCreateMarkerPacket(Player player, Location blockPosition, int color, String message, int duration) {
        FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(Unpooled.buffer());

        friendlyByteBuf.writeBlockPos(new BlockPos(blockPosition.getBlockX(), blockPosition.getBlockY(), blockPosition.getBlockZ()));
        friendlyByteBuf.writeInt(color);
        friendlyByteBuf.writeUtf(message);
        friendlyByteBuf.writeInt(duration);

        PacketContainer packetContainer = protocolManager.createPacket(PacketType.Play.Server.CUSTOM_PAYLOAD);
        packetContainer.getMinecraftKeys().write(0, new MinecraftKey("debug/game_test_add_marker"));
        packetContainer.getModifier().write(1, friendlyByteBuf);

        try {
            this.protocolManager.sendServerPacket(player, packetContainer);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendDebugGameTestClearPacket(Player player) {
        PacketContainer packetContainer = protocolManager.createPacket(PacketType.Play.Server.CUSTOM_PAYLOAD);
        packetContainer.getMinecraftKeys().write(0, new MinecraftKey("debug/game_test_clear"));

        try {
            this.protocolManager.sendServerPacket(player, packetContainer);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
