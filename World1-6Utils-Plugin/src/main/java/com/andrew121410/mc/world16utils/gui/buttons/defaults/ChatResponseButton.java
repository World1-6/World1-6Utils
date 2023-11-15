package com.andrew121410.mc.world16utils.gui.buttons.defaults;

import com.andrew121410.mc.world16utils.World16Utils;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.gui.buttons.CloneableGUIButton;
import com.andrew121410.mc.world16utils.gui.buttons.events.GUIClickEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

public class ChatResponseButton extends CloneableGUIButton {

    private final Component title;
    private final Component subtitle;
    private final BiConsumer<Player, String> event;

    public ChatResponseButton(int slot, ItemStack itemStack, Component title, Component subtitle, BiConsumer<Player, String> event) {
        super(slot, itemStack);
        this.title = title;
        this.subtitle = subtitle;
        this.event = event;
    }

    public ChatResponseButton(int slot, ItemStack itemStack, String title, String subtitle, BiConsumer<Player, String> event) {
        this(slot, itemStack, Translate.colorc(title), Translate.colorc(subtitle), event);
    }

    @Override
    public void onClick(GUIClickEvent guiClickEvent) {
        Player player = (Player) guiClickEvent.getEvent().getWhoClicked();
        player.closeInventory();
        World16Utils.getInstance().getChatResponseManager().create(player, this.title, this.subtitle, this.event);
    }
}
