package com.andrew121410.mc.world16utils.sign.screen;

import com.andrew121410.mc.world16utils.sign.screen.pages.SignLayout;
import com.andrew121410.mc.world16utils.sign.screen.pages.SignOS;
import com.andrew121410.mc.world16utils.sign.screen.pages.SignPage;
import org.bukkit.entity.Player;

public interface ISignScreen {

    SignOS getSignOS();

    boolean onDoneConstructed(SignScreenManager signScreenManager);

    boolean onButton(SignScreenManager signScreenManager, Player player, SignLayout signLayout, SignPage signPage, int linePointer);
}
