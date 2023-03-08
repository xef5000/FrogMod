package com.xef5000.features;

import com.xef5000.FrogMod;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class XaltFinder {

    private boolean sayXalt;
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        sayXalt = true;
    }

    @SubscribeEvent
    public void onRenderEntity(RenderLivingEvent.Pre<EntityLivingBase> event) {
        if (FrogMod.INSTANCE.getFrogModConfig().xaltFinder) {
            if (event.entity instanceof  EntityArmorStand) {
                EntityArmorStand entity = (EntityArmorStand) event.entity;
                if (!entity.hasCustomName()) return;
                double x = entity.posX;
                double y = entity.posY;
                double z = entity.posZ;
                String entityName = StringUtils.stripControlCodes(entity.getCustomNameTag());
                if (entityName.contains("Xalx") && sayXalt) {
                    FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + "Found xalx at: "  + EnumChatFormatting.YELLOW + "X = " + x + ", Y = " + y + ", Z = " + z));
                    sayXalt = false;
                }
            }
        }
    }



}

