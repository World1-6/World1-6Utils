package com.andrew121410.mc.world16utils.sign.screen;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class SignScreen {

    private final Map<Integer, ISignPage> pagesMap;

    private final JavaPlugin javaPlugin;

    private final String name;
    private final SignScreenManager signScreenManager;

    public SignScreen(JavaPlugin javaPlugin, SignScreenManager signScreenManager, String name) {
        this.javaPlugin = javaPlugin;
        this.signScreenManager = signScreenManager;
        this.name = name;

        this.pagesMap = new HashMap<>();
    }

    public void load() {
        ISignPage iSignPage = this.pagesMap.get(0);
        signScreenManager.setLine(0);
        signScreenManager.setMin(0);
        signScreenManager.setMax(3);
        signScreenManager.updateSign(signScreenManager.getSign(), iSignPage.getButtons());
    }

    public void doFind(Player player, int number, int scroll) {
        ISignPage iSignPage = this.pagesMap.get(number);
        if (iSignPage != null) {
            iSignPage.onButton(this, player, number, scroll);
        } else {
            throw new NullPointerException("iSignPage = null");
        }
    }

    public Map<Integer, ISignPage> getPagesMap() {
        return pagesMap;
    }

    public JavaPlugin getJavaPlugin() {
        return javaPlugin;
    }

    public String getName() {
        return name;
    }

    public SignScreenManager getSignScreenManager() {
        return signScreenManager;
    }
}
