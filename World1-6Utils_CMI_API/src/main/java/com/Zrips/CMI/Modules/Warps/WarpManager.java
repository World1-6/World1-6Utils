package com.Zrips.CMI.Modules.Warps;

import com.Zrips.CMI.CMI;

import java.util.HashMap;
import java.util.UUID;

public class WarpManager {

    private CMI plugin;
    HashMap<String, CmiWarp> warps = new HashMap<String, CmiWarp>();
    HashMap<UUID, HashMap<String, CmiWarp>> userWarps = new HashMap<UUID, HashMap<String, CmiWarp>>();

    public WarpManager(CMI plugin) {
        this.plugin = plugin;
    }

    public HashMap<String, CmiWarp> getWarps() {
        return warps;
    }

    public void addWarp(CmiWarp warp) {
        addWarp(warp, true);
    }

    public void addWarp(CmiWarp warp, boolean save) {
    }
}
