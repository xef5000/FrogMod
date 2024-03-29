package com.xef5000.features;

import com.xef5000.FrogMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.world.World;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

public class FerocitySound {

    private static final Object lock = new Object();
    private long lastFerocitySound = 0;

    @SubscribeEvent
    public void onSoundPlay(PlaySoundEvent event) {
        if (event.sound.getSoundLocation().toString().equals("minecraft:random.successful_hit") && FrogMod.INSTANCE.getFrogModConfig().artificialFerocity && lastFerocitySound + 300 < System.currentTimeMillis()) {
            lastFerocitySound = System.currentTimeMillis();
            playFerocitySound((int) randomDouble(0, 2));

        }
    }

    @SubscribeEvent
    public void onMobAttack(LivingAttackEvent event) {
        if (!FrogMod.INSTANCE.getFrogModConfig().artificialFerocity) return; /*config*/
        if (lastFerocitySound + 300 > System.currentTimeMillis()) return;
        if (event.entity instanceof EntityLivingBase && !(event.entity instanceof EntityArmorStand) && event.source != null && event.source.getEntity() != null && event.source.getEntity().equals(Minecraft.getMinecraft().thePlayer)) {
            lastFerocitySound = System.currentTimeMillis();
            playFerocitySound((int) randomDouble(0, 2));

        }
    }

    public static void playFerocitySound(int multiplier) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.thePlayer;
        World world = mc.theWorld;
        new Thread(() -> {
            synchronized (lock) {
                try {
                    float pitch = (float) randomDouble(1.4f, 1.6f);
                    for (int i = 0; i < multiplier; i++) {
                        mc.addScheduledTask(() -> player.playSound("fire.ignite", 0.9f, 0.53968257f));
                        mc.addScheduledTask(() -> player.playSound("fire.ignite", 0.9f, 0.53968257f)) ;
                        if (i == 0) Thread.sleep(150); else Thread.sleep(50);
                        mc.addScheduledTask(() -> player.playSound("mob.irongolem.throw", 1.0f, pitch)) ;
                        Thread.sleep(15);
                        mc.addScheduledTask(() -> player.playSound("mob.irongolem.throw", 1.0f, pitch)) ;
                        Thread.sleep(15);
                        mc.addScheduledTask(() -> player.playSound("mob.irongolem.throw", 1.0f, pitch)) ;
                        Thread.sleep(15);
                        mc.addScheduledTask(() -> player.playSound("mob.irongolem.throw", 1.0f, pitch)) ;
                        Thread.sleep(15);
                        mc.addScheduledTask(() -> player.playSound("mob.zombie.woodbreak", 0.6f, 1.3f)) ;
                        Thread.sleep(75);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }).start();
    }

    static double randomDouble(double min, double max) {
        Random r = new Random();
        return (r.nextInt((int) ((max - min) * 10 + 1)) + min * 10) / 10.0;
    }
}
