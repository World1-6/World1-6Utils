package com.andrew121410.mc.world16utils.blocks.sign.screen;

import com.andrew121410.mc.world16utils.blocks.sign.screen.pages.SignLayout;
import com.andrew121410.mc.world16utils.blocks.sign.screen.pages.SignPage;
import org.bukkit.entity.Player;

public interface ISignScreen {

    boolean onDoneConstructed(SignScreenManager signScreenManager);

    boolean onButton(SignScreenManager signScreenManager, Player player, SignLayout signLayout, SignPage signPage, int linePointer, int currentSide);

    boolean nullPage(SignScreenManager signScreenManager, Player player, boolean up);
}
