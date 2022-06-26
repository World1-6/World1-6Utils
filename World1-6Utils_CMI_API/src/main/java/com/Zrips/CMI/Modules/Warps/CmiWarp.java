package com.Zrips.CMI.Modules.Warps;

import net.Zrips.CMILib.Container.CMILocation;

public class CmiWarp {

    private String name;
    private CMILocation loc;

    public CmiWarp(String name, CMILocation loc) {
        this.name = name;
        this.loc = loc;
    }

    public CMILocation getLoc() {
        return loc;
    }

    public String getName() {
        return name;
    }

}
