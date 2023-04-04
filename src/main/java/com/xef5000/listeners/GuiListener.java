package com.xef5000.listeners;

import com.xef5000.FrogMod;
import com.xef5000.events.SlotClickEvent;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class GuiListener {

    @SubscribeEvent
    public void onSlotClick(SlotClickEvent event) {
        if (event.guiContainer instanceof GuiChest) {
            if (!(event.slotId == 4)) return;
            GuiChest guiChest = (GuiChest) event.guiContainer;
            ContainerChest cc = (ContainerChest) guiChest.inventorySlots;
            String containerName = cc.getLowerChestInventory().getDisplayName().getUnformattedText();
            if (FrogMod.INSTANCE.getFrogModConfig().betterTradeMenu && containerName.contains("SkyBlock Menu") && cc.inventorySlots.size() >= 54 && cc.inventorySlots.get(4).getStack() != null) {
                FrogMod.mc.thePlayer.sendChatMessage("/trades");
            }
        }
    }
}
