package com.andrew121410.mc.world16utils;

import com.andrew121410.ccutils.utils.HashBasedUpdater;

public class Updater extends HashBasedUpdater {

    private static final String JAR_URL = "https://github.com/World1-6/World1-6Utils/releases/download/latest/World1-6Utils.jar";
    private static final String HASH_URL = "https://github.com/World1-6/World1-6Utils/releases/download/latest/hash.txt";

    public Updater(World16Utils plugin) {
        super(plugin.getClass(), JAR_URL, HASH_URL);
    }
}
