package com.Zrips.CMI.Modules.Kits;

import com.Zrips.CMI.Containers.CMIPlayerInventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Kit {
    private List<ItemStack> item = new ArrayList<>();
    private HashMap<CMIPlayerInventory.CMIInventorySlot, ItemStack> extraItems = new HashMap<>();

    public Kit(String paramString) {
    }

    public List<ItemStack> getItems() {
        return null;
    }

    public void setItem(List<ItemStack> paramList) {
    }

    public void addItem(ItemStack paramItemStack) {
    }

    public ItemStack getExtraItem(CMIPlayerInventory.CMIInventorySlot paramCMIInventorySlot) {
        return null;
    }

    public void setExtraItem(CMIPlayerInventory.CMIInventorySlot paramCMIInventorySlot, ItemStack paramItemStack) {
    }

    public long getDelay() {
        return 0L;
    }

    public void setDelay(long paramLong) {
    }

    public boolean isEnabled() {
        return false;
    }

    public void setEnabled(boolean paramBoolean) {
    }
}
