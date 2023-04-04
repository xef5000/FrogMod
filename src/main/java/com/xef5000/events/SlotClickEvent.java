package com.xef5000.events;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class SlotClickEvent extends Event {
    public final GuiContainer guiContainer;
    public final Slot slot;
    public final int slotId;
    public int clickedButton;
    public int clickType;
    public boolean usePickblockInstead = false;

    public SlotClickEvent(GuiContainer guiContainer, Slot slot, int slotId, int clickedButton, int clickType) {
        this.guiContainer = guiContainer;
        this.slot = slot;
        this.slotId = slotId;
        this.clickedButton = clickedButton;
        this.clickType = clickType;
    }

    public void usePickblockInstead() {
        usePickblockInstead = true;
    }

    @Override
    public void setCanceled(boolean cancel) {
        super.setCanceled(cancel);
    }
}
