package com.xef5000.features;

import com.xef5000.FrogMod;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class UnbreakableCobble {

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        event.setCanceled(true);
        /*
        if (!FrogMod.INSTANCE.getFrogModConfig().unbreakableCobble) return;
        if (trueevent.world.getBlockState(event.pos).getBlock() == Blocks.cobblestone) {
            event.setCanceled(true);
            FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText("Cobble prevented from breaking"));
        }

         */
    }
}
