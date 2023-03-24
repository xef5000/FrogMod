package com.xef5000.features;

import com.xef5000.FrogMod;
import com.xef5000.events.HitBlockEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class UnbreakableCobble {

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onBlockHit(HitBlockEvent event) {
        if (!FrogMod.INSTANCE.getFrogModConfig().unbreakableCobble) return;
        if (FrogMod.mc.theWorld.getBlockState(event.blockPos).getBlock() == Blocks.cobblestone) {
            Minecraft.getMinecraft().thePlayer.playSound("note.bass", 1, 0.5f);
            event.setCanceled(true);
        }
    }
}
