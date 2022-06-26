package com.Zrips.CMI.Modules.Warps;

import net.Zrips.CMILib.Container.CMILocation;

import java.util.UUID;

public class CmiWarp {

    private String name;
    private CMILocation loc;
    private UUID creator;

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

    public UUID getCreator() {
        return creator;
    }

    public void setCreator(UUID creator) {
        this.creator = creator;
    }
}
