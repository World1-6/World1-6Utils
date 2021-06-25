package com.andrew121410.mc.world16utils.blocks;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.material.Door;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;
import org.bukkit.material.Stairs;

public class BlockUtils_V1_12_R1 implements BlockUtils {

    @Override
    public boolean isStairs(Block block) {
        return block.getState().getData() instanceof Stairs;
    }

    @Override
    public boolean isDoor(Block block) {
        return block.getState().getData().getClass().isAssignableFrom(Door.class);
    }

    @Override
    public Block getDoorBaseBlock(Block block) {
        if (!isDoor(block)) return null;
        Door door = (Door) block.getState().getData();
        if (door.isTopHalf()) return block.getRelative(BlockFace.DOWN);
        return block;
    }

    @Override
    public boolean isOpenable(Block block) {
        return block.getState().getData() instanceof Openable;
    }

    @Override
    public boolean doOpenable(Block block, boolean value) {
        if (!isOpenable(block)) return false;
        //Calling getState returns a NEW snapshot of the state (SO DON'T TOUCH)
        BlockState blockState = block.getState();
        Openable openable = (Openable) blockState.getData();
        openable.setOpen(value);
        blockState.setData((MaterialData) openable);
        blockState.update();
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
        Location loc = sign.getLocation();
        BlockPosition pos = new BlockPosition(loc.getX(), loc.getY(), loc.getZ());
        EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        TileEntitySign tileEntitySign = (TileEntitySign) nmsPlayer.world.getTileEntity(pos);
        PlayerConnection conn = nmsPlayer.playerConnection;

        tileEntitySign.isEditable = true;
        tileEntitySign.a(nmsPlayer);
        conn.sendPacket(new PacketPlayOutOpenSignEditor(pos));
    }
}
