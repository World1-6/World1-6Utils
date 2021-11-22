package com.andrew121410.mc.world16utils.packet;

import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Packet_V1_18_R1 implements IPackets {

    public Packet_V1_18_R1() {
    }

    @Override
    public void sendDebugCreateMarkerPacket(Player player, Location blockPosition, int color, String message, int duration) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(Unpooled.buffer());

        friendlyByteBuf.writeBlockPos(new BlockPos(blockPosition.getBlockX(), blockPosition.getBlockY(), blockPosition.getBlockZ()));
        friendlyByteBuf.writeInt(color);
        friendlyByteBuf.writeUtf(message);
        friendlyByteBuf.writeInt(duration);

        ClientboundCustomPayloadPacket customPayloadPacket = new ClientboundCustomPayloadPacket(ClientboundCustomPayloadPacket.DEBUG_GAME_TEST_ADD_MARKER, friendlyByteBuf);
        serverPlayer.connection.send(customPayloadPacket);
    }

    @Override
    public void sendDebugGameTestClearPacket(Player player) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        ClientboundCustomPayloadPacket customPayloadPacket = new ClientboundCustomPayloadPacket(ClientboundCustomPayloadPacket.DEBUG_GAME_TEST_ADD_MARKER, new FriendlyByteBuf(Unpooled.buffer()));
        serverPlayer.connection.send(customPayloadPacket);
    }
}
