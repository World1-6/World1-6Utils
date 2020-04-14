package com.andrew121410.mc.world16utils.sign.screen;

import org.bukkit.entity.Player;

import java.util.LinkedList;

public interface ISignPage {

    void onButton(SignScreen signScreen, Player player, int line, int scroll);

    LinkedList<String> getButtons();
}
