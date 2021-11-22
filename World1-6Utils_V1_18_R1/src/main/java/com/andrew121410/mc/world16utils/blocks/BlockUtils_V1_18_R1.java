package com.andrew121410.mc.world16utils.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class BlockUtils_V1_18_R1 implements BlockUtils {
    @Override
    public boolean isStairs(Block block) {
        return block.getBlockData() instanceof Stairs;
    }

    @Override
    public boolean isDoor(Block block) {
        return Tag.DOORS.isTagged(block.getType()); //or Material.getClass().isAssignableFrom(Door.class)
    }

    @Override
    public Block getDoorBaseBlock(Block block) {
        if (!isDoor(block)) return null;
        Door door = (Door) block.getBlockData();
        if (door.getHalf() == Bisected.Half.TOP) return block.getRelative(BlockFace.DOWN);
        return block;
    }

    @Override
    public boolean isOpenable(Block block) {
        return block.getBlockData() instanceof Openable;
    }

    @Override
    public boolean doOpenable(Block block, boolean value) {
        if (!isOpenable(block)) return false;
        Openable openable = (Openable) block.getBlockData();
        openable.setOpen(value);
        block.setBlockData(openable);
        return true;
    }

    @Override
    public Sign isSign(Block block) {
        BlockState blockState = block.getState();
        if (blockState instanceof Sign) {
            return (Sign) blockState;
        }
        return null;
    }

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
