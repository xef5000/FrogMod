package com.xef5000.listeners;

import com.xef5000.FrogMod;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class ToolTipListener {

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onItemTooltip(ItemTooltipEvent event) {


        if (!FrogMod.INSTANCE.getFrogModConfig().fullNpcPrice) return;
        if (!event.itemStack.hasTagCompound()) return;
        List<String> lines = event.toolTip;
        boolean nextLine = false;
        int price = 0;
        String itemId = null;
        int priceIndex = 0;
        for (String line : lines) {
            if (nextLine) {
                price = Integer.parseInt(EnumChatFormatting.getTextWithoutFormattingCodes(line.toLowerCase()).split(" ")[0].replaceAll(",", "")) / event.itemStack.stackSize;
                NBTTagCompound extraAttributes = event.itemStack.getTagCompound().getCompoundTag("ExtraAttributes");
                itemId = extraAttributes.getString("id");
                priceIndex = lines.indexOf(line);
                break;
            }
            if (EnumChatFormatting.getTextWithoutFormattingCodes(line.toLowerCase()).equals("sell price")) nextLine = true;
        }
        if (price == 0 || itemId == null) return;

        int inventoryAmount = 0;

        for (int i =0; i < Minecraft.getMinecraft().thePlayer.inventory.mainInventory.length; i++) {
            ItemStack item = FrogMod.mc.thePlayer.inventory.mainInventory[i];
            if (item == null) continue;
            if (!item.hasTagCompound()) continue;
            String id = item.getTagCompound().getCompoundTag("ExtraAttributes").getString("id");
            if (id != null && id.equals(itemId)) {
                inventoryAmount += item.stackSize;
            }
        }

        int totalValue = price * inventoryAmount;

        event.toolTip.set(priceIndex, event.toolTip.get(priceIndex) + " §7(" + String.format("%,d", totalValue) + ")");



    }
}
