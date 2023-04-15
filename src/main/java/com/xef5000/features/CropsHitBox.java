package com.xef5000.features;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarrot;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockPotato;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CropsHitBox {

    private void modifyCropsHitboxes() {
        Block carrotBlock = Block.blockRegistry.getObject(new ResourceLocation("minecraft:carrots"));
        if (carrotBlock instanceof BlockCarrot) {
            ((BlockCarrot) carrotBlock).setBlockBounds(0.0625F, 0F, 0.0625F, 0.9375F, 0.9375F, 0.9375F);
        }

        Block potatoBlock = Block.blockRegistry.getObject(new ResourceLocation("minecraft:potatoes"));
        if (potatoBlock instanceof BlockPotato) {
            ((BlockPotato) potatoBlock).setBlockBounds(0, 0, 0, 1, 0.9375F, 1);
        }

        Block wheatBlock = Block.blockRegistry.getObject(new ResourceLocation("minecraft:wheat"));
        if (wheatBlock instanceof BlockCrops) {
            ((BlockCrops) wheatBlock).setBlockBounds(0, 0, 0, 1, 0.9375F, 1);
        }

        Block netherWartBlock = Block.blockRegistry.getObject(new ResourceLocation("minecraft:nether_wart"));
        if (netherWartBlock instanceof BlockNetherWart) {
            ((BlockNetherWart) netherWartBlock).setBlockBounds(0, 0, 0, 1, 0.9375F, 1);
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        modifyCropsHitboxes();
        System.out.println("Crops hitboxes modified");
    }

}
