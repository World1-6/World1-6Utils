package com.andrew121410.mc.world16utils.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.MinecraftKey;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.EncoderException;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

public class Packet_V1_16_R3 implements IPackets {

    private ProtocolManager protocolManager;

    public Packet_V1_16_R3() {
        this.protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void sendDebugCreateMarkerPacket(Player player, Location blockPosition, int color, String message, int duration) {
        PacketDataSerializer packetDataSerializer = new PacketDataSerializer(Unpooled.buffer());

        packetDataSerializer.writeLong(new BlockPosition(blockPosition.getBlockX(), blockPosition.getBlockY(), blockPosition.getBlockZ()).asLong());
        packetDataSerializer.writeInt(color);
        writeString(packetDataSerializer, message);
        packetDataSerializer.writeInt(duration);

        PacketContainer packetContainer = protocolManager.createPacket(PacketType.Play.Server.CUSTOM_PAYLOAD);
        packetContainer.getMinecraftKeys().write(0, new MinecraftKey("debug/game_test_add_marker"));
        packetContainer.getModifier().write(1, packetDataSerializer);

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

    public void writeString(PacketDataSerializer packetDataSerializer, String string) {
        byte[] bs = string.getBytes(StandardCharsets.UTF_8);
        if (bs.length > 32767) {
            throw new EncoderException("String too big (was " + bs.length + " bytes encoded, max " + 32767 + ")");
        } else {
            writeVarInt(packetDataSerializer, bs.length);
            packetDataSerializer.writeBytes(bs);
        }
    }

    public void writeVarInt(PacketDataSerializer packetDataSerializer, int value) {
        while ((value & -128) != 0) {
            packetDataSerializer.writeByte(value & 127 | 128);
            value >>>= 7;
        }
        packetDataSerializer.writeByte(value);
    }
}
