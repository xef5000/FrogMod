package com.xef5000.features;

import com.xef5000.FrogMod;
import com.xef5000.utils.Visual;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ThroneFinder {

    private int ticks;
    private int scannerWidth = 64;
    private int pY;
    private int belowY;
    private int aboveY;
    private ArrayList<BlockPos> foundGems = new ArrayList<>();
    private int x = 0;
    private int y;
    private int z;
    private BlockPos pos;
    private IBlockState state;

    public boolean sayNewStructure = true;

    @SubscribeEvent
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        if (!FrogMod.INSTANCE.getFrogModConfig().throneFinder) return;
        for (BlockPos pos : foundGems) {
            //Visual.drawFilledEsp(new BlockPos(pos.getX(), pos.getY(), pos.getZ()), Color.BLUE);
        }


    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        foundGems.clear();
    }


    @SubscribeEvent
    public void onTick(TickEvent event) {
        if (FrogMod.INSTANCE.getFrogModConfig().throneFinder) {
            ticks++;
            if (ticks % 60 == 0) {
                ticks = 0;
                /*
                int pY;
                int belowY;
                int aboveY;
                final BlockPos[] foundGems;
                final int x = 0;
                int y;
                int z;

                 */
                new Thread(() -> {
                    pY = ((int) Math.floor(FrogMod.mc.thePlayer.posY));
                    belowY = (pY - scannerWidth);
                    aboveY = (pY + scannerWidth);
                    if (belowY < 31) {
                        belowY = 31;
                    }
                    if (aboveY > 189) {
                        aboveY = 189;
                    }
                    for (x = (int) (FrogMod.mc.thePlayer.posX - scannerWidth); x < FrogMod.mc.thePlayer.posX + scannerWidth; ++x) {
                        for(y = belowY; y < aboveY; ++y) {
                            for (z = (int) (Math.floor(FrogMod.mc.thePlayer.posZ) - scannerWidth); z < FrogMod.mc.thePlayer.posZ + scannerWidth; ++z) {
                                pos = new BlockPos(x, y, z);
                                BlockPos pos2 = new BlockPos(x, y + 1, z);
                                state = FrogMod.mc.theWorld.getBlockState(pos);
                                IBlockState state2 = FrogMod.mc.theWorld.getBlockState(pos2);
                                if (state.getBlock() == Blocks.sea_lantern && state2.getBlock() == Blocks.stained_glass && state2.getValue(BlockStainedGlass.COLOR) == EnumDyeColor.LIGHT_BLUE && isNewStructure(pos)) {
                                    foundGems.add(pos);
                                    FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + "Found structure at: "  + EnumChatFormatting.YELLOW + "X = " + x + ", Y = " + y + ", Z = " + z));
                                }
                            }
                        }
                    }


                }).start();

            }
        }
    }

    private boolean isNewStructure(BlockPos pos) {
        if (foundGems.isEmpty()) return true;
        ArrayList<Boolean> away = new ArrayList<>();
        for (BlockPos i : foundGems) {
            double dist = Math.sqrt((pos.getX() - i.getX())*(pos.getX() - i.getX()) + (pos.getY() - i.getY())*(pos.getY() - i.getY()) + (pos.getZ() - i.getZ())*(pos.getZ() - i.getZ()));
            if (dist >= 50) {
                away.add(true);
            } else {
                away.add(false);
            }
        }
        for (Boolean i : away) {
            if (!i) {
                return false;
            }
        }


        return true;
    }
}
