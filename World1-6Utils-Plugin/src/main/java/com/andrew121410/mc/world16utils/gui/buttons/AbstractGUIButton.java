package com.andrew121410.mc.world16utils.gui.buttons;

import com.andrew121410.mc.world16utils.gui.buttons.events.GUIClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public abstract class AbstractGUIButton {

    private int slot;
    private ItemStack itemStack;
    private Supplier<ItemStack> itemSupplier;

    public AbstractGUIButton(int slot, ItemStack itemStack) {
        this.slot = slot;
        this.itemStack = itemStack;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItemStack() {
        return itemSupplier != null ? itemSupplier.get() : itemStack;
    }

    public boolean isAnimated() {
        return itemSupplier != null;
    }

    /**
     * Makes this button animated. The supplier is called every 2 ticks to get the current frame.
     * Returns itself for fluent chaining.
     */
    @SuppressWarnings("unchecked")
    public <T extends AbstractGUIButton> T animate(Supplier<ItemStack> supplier) {
        this.itemSupplier = supplier;
        return (T) this;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public abstract void onClick(GUIClickEvent guiClickEvent);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractGUIButton guiButton = (AbstractGUIButton) o;

        if (slot != guiButton.slot) return false;
        return itemStack != null ? itemStack.equals(guiButton.itemStack) : guiButton.itemStack == null;
    }

    @Override
    public int hashCode() {
        int result = slot;
        result = 31 * result + (itemStack != null ? itemStack.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GUIButton{" +
                "slot=" + slot +
                ", itemStack=" + itemStack +
                '}';
    }
}
