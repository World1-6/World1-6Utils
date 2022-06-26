package com.Zrips.CMI;

import com.Zrips.CMI.Modules.Warps.WarpManager;

public class CMI {

    protected PlayerManager PM;
    protected WarpManager warpManager;

    public PlayerManager getPlayerManager() {
        return this.PM;
    }

    public WarpManager getWarpManager() {
        return this.warpManager;
    }
}
