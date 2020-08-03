package com.andrew121410.mc.world16utils.blocks.sign;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public interface SignUtils {
    void edit(Player player, Sign sign);

    String centerText(String text, int max);

    Sign isSign(Block block);
}
