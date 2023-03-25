package com.xef5000.features;

import com.xef5000.FrogMod;
import com.xef5000.utils.Visual;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandTitle;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import scala.Int;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

import javax.sound.midi.Soundbank;
import java.awt.*;
import java.util.ArrayList;

public class BarbarianDukeESP {

    public static int ticks = 0;
    public static boolean barbarianDukeFound = false;
    public static Entity barbarianDuke = null;

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        barbarianDukeFound = false;
    }

    @SubscribeEvent
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        if (!FrogMod.INSTANCE.getFrogModConfig().barbarianDukeESP || !barbarianDukeFound) return;
        Color color = new Color(139, 69, 19);
        Visual.drawFilledEsp(barbarianDuke, color);
    }

    @SubscribeEvent
    public void onTick(TickEvent event) {
        if (FrogMod.INSTANCE.getFrogModConfig().barbarianDukeESP && !barbarianDukeFound) {
            ticks++;
            if (ticks % 5 == 0) { // Every 250ms
                ticks = 0;
                new Thread(() -> {
                    // Get every entities in the world
                    for (Entity entity : Minecraft.getMinecraft().theWorld.loadedEntityList) {
                        if (entity instanceof EntityArmorStand) {
                            EntityArmorStand armorStand = (EntityArmorStand) entity;
                            // Get the name of the armor stand without the color codes
                            String name = StringUtils.stripControlCodes(armorStand.getCustomNameTag());
                            if (name.contains("Barbarian Duke")) {
                                FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText("Found duke"));
                                barbarianDukeFound = true;
                                barbarianDuke = armorStand;
                                return;
                            }
                        }
                    }
                }).start();
            }
        }
    }



}
