package com.andrew121410.mc.world16utils.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class BlockUtils_V1_17_R1 implements BlockUtils {
    @Override
    public void edit(Player player, Sign sign) {
        Location location = sign.getLocation();
        BlockPos blockPos = new BlockPos(location.getX(), location.getY(), location.getZ());
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        SignBlockEntity signBlockEntity = (SignBlockEntity) serverPlayer.level.getTileEntity(blockPos, true);
        ServerGamePacketListenerImpl connection = serverPlayer.connection;

        if (signBlockEntity == null) return;

        signBlockEntity.setEditable(true);
        signBlockEntity.setAllowedPlayerEditor(serverPlayer.getUUID());
        connection.send(new ClientboundOpenSignEditorPacket(blockPos));
    }
}
