package com.andrew121410.mc.world16utils.gui.buttons.defaults;

import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.gui.GUIWindow;
import com.andrew121410.mc.world16utils.gui.buttons.GUIButton;
import com.andrew121410.mc.world16utils.gui.buttons.GUIClickEvent;
import com.andrew121410.mc.world16utils.utils.Utils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class LoreShifterButton extends GUIButton {

    private BiConsumer<GUIClickEvent, Integer> biConsumer;

    private ItemStack itemStack;
    private String prefix = "&b&l>&r ";
    private List<String> loreList;

    private int lineNumber = 0;
    private String untouchedLine;
    private String modifiedLine;

    public LoreShifterButton(int slot, ItemStack itemStack, String[] lores, BiConsumer<GUIClickEvent, Integer> biConsumer) {
        super(slot, itemStack);
        this.itemStack = itemStack;
        this.loreList = Arrays.asList(lores);
        this.biConsumer = biConsumer;

        String lore = Utils.getIndexFromStringList(this.loreList, 0);
        this.untouchedLine = lore;
        this.modifiedLine = prefix + lore;
        this.loreList.set(0, modifiedLine);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(this.loreList.stream().map(Translate::color).collect(Collectors.toList()));
        itemStack.setItemMeta(itemMeta);
    }

    @Override
    public void onClick(GUIClickEvent guiClickEvent) {
        InventoryClickEvent event = guiClickEvent.getEvent();
        GUIWindow guiWindow = guiClickEvent.getGuiWindow();
        Player player = (Player) event.getWhoClicked();

        if (event.getClick() == ClickType.RIGHT) {
            this.loreList.set(this.lineNumber, this.untouchedLine);
            this.lineNumber++;
            String lore = Utils.getIndexFromStringList(this.loreList, this.lineNumber);
            if (lore == null) {
                this.lineNumber = 0; //Reset it back to 0;
                lore = Utils.getIndexFromStringList(this.loreList, this.lineNumber);
            }
            this.untouchedLine = lore;
            this.modifiedLine = this.prefix + lore;
            this.loreList.set(this.lineNumber, this.modifiedLine);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setLore(this.loreList.stream().map(Translate::color).collect(Collectors.toList()));
            itemStack.setItemMeta(itemMeta);
            //Refresh?
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1F, 1F);
            guiWindow.refresh(player);
        } else if (event.getClick() == ClickType.LEFT) {
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
            this.biConsumer.accept(guiClickEvent, this.lineNumber);
        }
    }
}
