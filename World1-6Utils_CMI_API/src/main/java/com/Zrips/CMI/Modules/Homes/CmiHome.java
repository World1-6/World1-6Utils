package com.Zrips.CMI.Modules.Homes;

import net.Zrips.CMILib.Container.CMILocation;

public class CmiHome {

    private CMILocation loc;
    private String name;

    public CmiHome(String name, CMILocation loc) {
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