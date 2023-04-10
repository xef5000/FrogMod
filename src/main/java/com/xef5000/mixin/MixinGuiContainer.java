package com.xef5000.mixin;


import com.xef5000.FrogMod;
import com.xef5000.events.SlotClickEvent;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiContainer.class, priority = 500)
public abstract class MixinGuiContainer extends GuiScreen {
    private static boolean hasTradeMenuStack = false;
    private static final ItemStack tradeMenuStack = createItemStack(
            Items.emerald,
            EnumChatFormatting.GREEN + "Trade menu" + EnumChatFormatting.GRAY + " (FrogMod)", 0,
            EnumChatFormatting.YELLOW + "",
            EnumChatFormatting.YELLOW + "Click to open!"
    );
    private int tradeMenuStackIndex = -1;


    private static ItemStack createItemStack(Item item, String displayName, int damage, String... lore) {
        ItemStack stack = new ItemStack(item, 1, damage);
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagCompound display = new NBTTagCompound();
        NBTTagList Lore = new NBTTagList();

        for (String line : lore) {
            Lore.appendTag(new NBTTagString(line));
        }

        display.setString("Name", displayName);
        display.setTag("Lore", Lore);

        tag.setTag("display", display);
        tag.setInteger("HideFlags", 254);

        stack.setTagCompound(tag);

        return stack;
    }

    @Inject(method = "drawSlot", at = @At("HEAD"), cancellable = true)
    public void drawSlot(Slot slot, CallbackInfo ci) {
        if (slot == null) return;
        GuiContainer $this = (GuiContainer) (Object) this;
        if ($this instanceof GuiChest && slot.getSlotIndex() == 4/*< 9 && slot.getSlotIndex() > 0 && (slot.getSlotIndex() % 9 == 4)*/) {
            tradeMenuStackIndex = -1;
            hasTradeMenuStack = true;

            GuiChest eventGui = (GuiChest) $this;
            ContainerChest cc = (ContainerChest) eventGui.inventorySlots;
            String containerName = cc.getLowerChestInventory().getDisplayName().getUnformattedText();
            if (containerName.contains("SkyBlock Menu") && cc.inventorySlots.size() >= 54 && cc.inventorySlots.get(4).getStack() != null && FrogMod.INSTANCE.getFrogModConfig().betterTradeMenu) {
                ci.cancel();
                this.zLevel = 100.0F;
                this.itemRender.zLevel = 100.0F;

                GlStateManager.enableDepth();
                this.itemRender.renderItemAndEffectIntoGUI(
                        tradeMenuStack,
                        slot.xDisplayPosition,
                        slot.yDisplayPosition
                );
                this.itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, tradeMenuStack,
                        slot.xDisplayPosition, slot.yDisplayPosition, ""
                );

                this.itemRender.zLevel = 0.0F;
                this.zLevel = 0.0F;

                tradeMenuStackIndex = slot.getSlotIndex();
            } else if (slot.getSlotIndex() == 0)
                hasTradeMenuStack = false;
            else if (!($this instanceof GuiChest))
                tradeMenuStackIndex = -1;
        }
    }
/*
    @Redirect(method = "drawScreen", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/inventory/GuiContainer;renderToolTip(Lnet/minecraft/item/ItemStack;II)V"))
    public void drawScreen_renderTooltip(GuiContainer guiContainer, ItemStack stack, int x, int y) {
        //GuiContainer $this = (GuiContainer) (Object) this;
        //GuiChest eventGui = (GuiChest) $this;
        //ContainerChest cc = (ContainerChest) eventGui.inventorySlots;
        //String containerName = cc.getLowerChestInventory().getDisplayName().getUnformattedText();
        if (theSlot.slotNumber == tradeMenuStackIndex && FrogMod.INSTANCE.getFrogModConfig().betterTradeMenu) {
            this.renderToolTip(tradeMenuStack, x, y);
        } else {
            this.renderToolTip(stack, x, y);
        }
    }
    */

    @ModifyArg(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/inventory/GuiContainer;renderToolTip(Lnet/minecraft/item/ItemStack;II)V"), index = 0)
    public ItemStack adjustItemStack(ItemStack itemStack) {
        if (theSlot.slotNumber == tradeMenuStackIndex && FrogMod.INSTANCE.getFrogModConfig().betterTradeMenu) {
            return tradeMenuStack;
        } else {
            return itemStack;
        }
    }

    @Shadow
    private Slot theSlot;

    @Inject(method = "handleMouseClick", at = @At(value = "HEAD"), cancellable = true)
    public void handleMouseClick(Slot slotIn, int slotId, int clickedButton, int clickType, CallbackInfo ci) {
        if (slotIn == null) return;
        GuiContainer $this = (GuiContainer) (Object) this;
        SlotClickEvent event = new SlotClickEvent($this, slotIn, slotId, clickedButton, clickType);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) ci.cancel(); return;

    }
}
