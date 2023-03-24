package com.xef5000.features;

import com.xef5000.FrogMod;
import com.xef5000.utils.Visual;
import com.xef5000.utils.HypixelEntities;
import com.xef5000.utils.HypixelEntity;
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
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;

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
                    HypixelEntity barbarian = HypixelEntities.getEntityByName("Barbarian Duke X");
                    barbarianDuke = HypixelEntities.getRealEntity(barbarian);
                    if (barbarianDuke != null) {
                        barbarianDukeFound = true;
                    }
                }).start();
            }
        }
    }

}
