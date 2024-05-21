package com.Zrips.CMI.Modules.SavedInv;

import com.Zrips.CMI.Containers.CMIUser;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SavedInventoryManager {

    public ConcurrentHashMap<UUID, SavedInventories> getMap() {
        return null;
    }

    private SavedInventories getInventories(UUID paramUUID) {
        return null;
    }

    public void addInventory(CMIUser paramCMIUser, CMIInventory paramCMIInventory) {
    }

    public SavedInventories getInventories(CMIUser paramCMIUser) {
        return null;
    }

    public boolean saveAllInventories(UUID paramUUID) {
        return false;
    }
}
