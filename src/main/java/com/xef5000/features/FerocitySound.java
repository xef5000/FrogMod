package com.xef5000.features;

import com.xef5000.FrogMod;
import com.xef5000.gui.FrogModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FerocitySound {

    private final Object lock = new Object();
    private long lastFerocitySound = 0;

    public void playFerocitySound() {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.thePlayer;
        synchronized (lock) {
            try {
                player.playSound("mob.zombie.woodbreak", 1.0f, 1.3f + (float) Math.random() * 0.2f);
                Thread.sleep(20);
                player.playSound("fire.ignite", 1.0f, 0.4f + (float) Math.random() * 0.2f);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public void onMobAttack(LivingAttackEvent event) {
        if (!FrogMod.INSTANCE.getFrogModConfig().ferocitySoundEffects) return;
        if (lastFerocitySound + 300 > System.currentTimeMillis()) return;
        if (event.entity instanceof EntityLivingBase && event.source != null && event.source.getEntity() != null && event.source.getEntity().equals(Minecraft.getMinecraft().thePlayer)) {
            playFerocitySound();
            lastFerocitySound = System.currentTimeMillis();
        }
    }

}
