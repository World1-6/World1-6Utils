package com.andrew121410.mc.world16utils.sign.screen;

import org.bukkit.entity.Player;

public interface ISignScreen {

    boolean onButton(SignScreenManager signScreenManager, Player player, int line, int scroll);
}
