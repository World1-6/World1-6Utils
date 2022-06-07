package com.andrew121410.mc.world16utils.blocks;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class BlockUtils_V1_19_R1 implements BlockUtils {
    @Override
    public void edit(Player player, Sign sign) {
        //https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/commits/27a27cdb840880f785b654bbcacb0b4c7c77fad9
        player.openSign(sign);
    }
}
