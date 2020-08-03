package com.andrew121410.mc.world16utils.blocks.sign;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SignUtils_V1_12_R1 implements SignUtils {

    private JavaPlugin plugin;

    public SignUtils_V1_12_R1(JavaPlugin plugin) {
        this.plugin = plugin;
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

    @Override
    public String centerText(String text, int max) {
        if (text.length() > max)
            return text.substring(0, max);
        else {
            int pad = max - text.length();
            StringBuilder sb = new StringBuilder(text);
            for (int i = 0; i < pad; i++)
                if (i % 2 == 0)
                    sb.insert(0, " ");
                else
                    sb.append(" ");
            return sb.toString();
        }
    }

    @Override
    public Sign isSign(Block block) {
        BlockState blockState = block.getState();
        if (blockState instanceof Sign) {
            return (Sign) blockState;
        }
        return null;
    }
}
