package com.xef5000.listeners;

import com.xef5000.FrogMod;
import com.xef5000.utils.LocationManager;
import com.xef5000.utils.Visual;
import com.xef5000.utils.WaypointsManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.*;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;

public class RenderEntityListener {

    private boolean foundXalx;
    private boolean foundDuke;
    public static Vec3 barbarianDuke = null;
    public static Entity barbarianDukeEntity = null;

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        foundXalx = false;
        foundDuke = false;
        barbarianDuke = null;
        barbarianDukeEntity = null;
        WaypointsManager.removeByName("Xalx");
    }

    @SubscribeEvent
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        if (!FrogMod.INSTANCE.getFrogModConfig().barbarianDukeESP || !foundDuke) return;
        Color color = new Color(139, 69, 19);
        //Visual.drawFilledEsp(barbarianDuke, color);
        Visual.drawFilledEsp(barbarianDuke.add(new Vec3(0, -1, 0)), color);
        //Visual.drawFilledEsp(barbarianDukeEntity, color);
    }

    @SubscribeEvent
    public void onRenderEntity(RenderLivingEvent.Pre<EntityLivingBase> event) {
        if (LocationManager.getInstance().getLocation() == null) return;
        if (!foundXalx) {
            staticNPC(event);
        }
        movingNPC(event);

    }

    private void staticNPC(RenderLivingEvent.Pre<EntityLivingBase> event) {
        if (event.entity instanceof EntityArmorStand) {
            EntityArmorStand entity = (EntityArmorStand) event.entity;
            if (!entity.hasCustomName()) return;
            double x = entity.posX;
            double y = entity.posY;
            double z = entity.posZ;
            String entityName = StringUtils.stripControlCodes(entity.getCustomNameTag());
            if (FrogMod.INSTANCE.getFrogModConfig().crystalScanner && entityName.contains("Xalx") && !foundXalx && false) {
                FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + "Found xalx at: "  + EnumChatFormatting.YELLOW + "X = " + x + ", Y = " + y + ", Z = " + z));
                WaypointsManager.addWaypoint(new BlockPos(x, y, z), "Xalx");
                foundXalx = true;
            }
        }
    }

    private void movingNPC(RenderLivingEvent.Pre<EntityLivingBase> event) {
        if(event.entity instanceof EntityOtherPlayerMP) {
            EntityOtherPlayerMP npc = (EntityOtherPlayerMP) event.entity;
            if (LocationManager.getInstance().getLocation().equals("crimson_isle") && FrogMod.INSTANCE.getFrogModConfig().barbarianDukeESP && npc.getName().contains("DukeBarb")) {
                if(!foundDuke) FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + "Found duke at: "  + EnumChatFormatting.YELLOW + "X = " + Math.round(npc.getPosition().getX()) + ", Y = " + Math.round(npc.getPosition().getY()) + ", Z = " + Math.round(npc.getPosition().getZ())));
                foundDuke = true;
                barbarianDuke = new Vec3(npc.posX - 0.5, npc.posY + 1 , npc.posZ - 0.5);
                barbarianDukeEntity = npc;
            }
        }
        /*
        if (event.entity instanceof EntityArmorStand) {
            EntityArmorStand entity = (EntityArmorStand) event.entity;
            if (!entity.hasCustomName()) return;
            double x = entity.posX;
            double y = entity.posY;
            double z = entity.posZ;
            String entityName = StringUtils.stripControlCodes(entity.getCustomNameTag());
            ArrayList<EntityOtherPlayerMP> npcs = new ArrayList<>();
            for (Entity entityy : Minecraft.getMinecraft().theWorld.loadedEntityList) {
                if (entityy instanceof EntityOtherPlayerMP) npcs.add((EntityOtherPlayerMP) entityy);
            }
            if (LocationManager.getInstance().getLocation().equals("crimson_isle") && FrogMod.INSTANCE.getFrogModConfig().barbarianDukeESP && entityName.contains("Barbarian Duke")) {
                if(!foundDuke) FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + "Found duke at: "  + EnumChatFormatting.YELLOW + "X = " + Math.round(x) + ", Y = " + Math.round(y) + ", Z = " + Math.round(z)));
                foundDuke = true;
                //barbarianDuke = new Vec3(entity.posX, entity.posY , entity.posZ);

                for (EntityOtherPlayerMP npc : npcs) {
                    if(npc.getName().contains("DukeBarb")) FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText("TRUE"));
                    Vec3 distance = getDistance(npc.getPositionVector(), new Vec3(entity.posX, entity.posY, entity.posZ));
                    if ((distance.xCoord < 3 && distance.xCoord > -3) && (distance.yCoord < 3 && distance.yCoord > -3) && (distance.zCoord < 3 && distance.zCoord > -3)) {
                        barbarianDuke = new Vec3(npc.posX - 0.5, npc.posY + 1 , npc.posZ - 0.5);
                    }
                }

                //barbarianDuke = new Vec3(entity.posX, entity.posY, entity.posZ);
            }
        }

         */
    }

    private Vec3 getDistance(Vec3 pos1, Vec3 pos2) {
        return new Vec3(pos1.xCoord - pos2.xCoord, pos1.yCoord - pos2.yCoord, pos1.zCoord - pos2.zCoord);
    }



}
