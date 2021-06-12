package com.andrew121410.mc.world16utils.sign;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SignUtils_V1_17_R1 implements SignUtils {

    private JavaPlugin plugin;

    public SignUtils_V1_17_R1(JavaPlugin plugin) {
        this.plugin = plugin;
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
