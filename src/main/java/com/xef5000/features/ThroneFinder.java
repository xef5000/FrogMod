package com.xef5000.features;

import com.xef5000.FrogMod;
import com.xef5000.utils.Visual;
import com.xef5000.utils.WaypointsManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandTitle;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import scala.Int;

import javax.sound.midi.Soundbank;
import java.awt.*;
import java.util.ArrayList;

public class ThroneFinder {

    private int ticks;
    public static int scannerWidth = 0;
    private int pY;
    private int belowY;
    private int aboveY;
    private ArrayList<BlockPos> foundGems = new ArrayList<>();
    private int x = 0;
    private int y;
    private int z;
    private BlockPos pos;
    private IBlockState state;
    private boolean sayThrone = true;

    private BlockPos throneCords = new BlockPos(-1000, -1000, -1000);
    public boolean sayNewStructure = true;



    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        sayThrone = true;
        throneCords = new BlockPos(-1000, -1000, -1000);
    }

    @SubscribeEvent
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        if (!FrogMod.INSTANCE.getFrogModConfig().throneFinder) return;
        if (throneCords.getY() != -1000) {
            Visual.drawFilledBlockEsp(new BlockPos(throneCords.getX(), throneCords.getY(), throneCords.getZ()), Color.GREEN);
            Visual.renderWaypointText("Throne", new BlockPos(throneCords.getX(), throneCords.getY(), throneCords.getZ()), event.partialTicks);
        }


    }

    @SubscribeEvent
    public void onTick(TickEvent event) {
        if (FrogMod.INSTANCE.getFrogModConfig().throneFinder && sayThrone) {
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
                scannerWidth = FrogMod.INSTANCE.getFrogModConfig().throneFinderRange;
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
                                if (FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.cobblestone_wall && isThrone(x, y, z)) {
                                    FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + "Found throne at: "  + EnumChatFormatting.YELLOW + "X = " + x + ", Y = " + y + ", Z = " + z));
                                    sayThrone = false;
                                    //throneCords = new BlockPos(x, y , z);
                                    WaypointsManager.addWaypoint(new BlockPos(x, y, z), "Throne");
                                    Visual.showTitle("&cFOUND THRONE", "", 5, 45, 5);
                                    Minecraft.getMinecraft().thePlayer.playSound("random.orb", 1, 6);
                                }
                            }
                        }
                    }
                }).start();

            }
        }
    }

    private boolean isThrone(double x, double y, double z) {
        /*
         * NORTH
         * CHECKS
         */
        boolean check1_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y , z)).getBlock() == Blocks.cobblestone_wall;
        boolean check2_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y , z -1)).getBlock() == Blocks.cobblestone_wall;
        boolean check3_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y , z -2)).getBlock() == Blocks.double_stone_slab;
        boolean check4_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x + 1, y , z -2)).getBlock() == Blocks.double_stone_slab;
        boolean check5_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x + 2, y , z -2)).getBlock() == Blocks.double_stone_slab;
        boolean check6_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x + 2, y , z -1)).getBlock() == Blocks.cobblestone_wall;
        boolean check7_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x + 2, y , z )).getBlock() == Blocks.cobblestone_wall;
        boolean check8_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x +1, y, z)).getBlock() == Blocks.air;
        /*
         * WEST
         * CHECKS
         */
        boolean check1_w = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y , z)).getBlock() == Blocks.cobblestone_wall;
        boolean check2_w = FrogMod.mc.theWorld.getBlockState(new BlockPos(x -1, y , z)).getBlock() == Blocks.cobblestone_wall;
        boolean check3_w = FrogMod.mc.theWorld.getBlockState(new BlockPos(x -2, y , z)).getBlock() == Blocks.double_stone_slab;
        boolean check4_w = FrogMod.mc.theWorld.getBlockState(new BlockPos(x -2, y , z -1)).getBlock() == Blocks.double_stone_slab;
        boolean check5_w = FrogMod.mc.theWorld.getBlockState(new BlockPos(x -2, y , z -2)).getBlock() == Blocks.double_stone_slab;
        boolean check6_w = FrogMod.mc.theWorld.getBlockState(new BlockPos(x -1, y , z -2)).getBlock() == Blocks.cobblestone_wall;
        boolean check7_w = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y , z -2)).getBlock() == Blocks.cobblestone_wall;
        boolean check8_w = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y , z -1)).getBlock() == Blocks.air;
        /*
         * SOUTH
         * CHECKS
         */
        boolean check1_s = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y , z)).getBlock() == Blocks.cobblestone_wall;
        boolean check2_s = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y , z +1)).getBlock() == Blocks.cobblestone_wall;
        boolean check3_s = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y , z +2)).getBlock() == Blocks.double_stone_slab;
        boolean check4_s = FrogMod.mc.theWorld.getBlockState(new BlockPos(x -1, y , z +2)).getBlock() == Blocks.double_stone_slab;
        boolean check5_s = FrogMod.mc.theWorld.getBlockState(new BlockPos(x -2, y , z +2)).getBlock() == Blocks.double_stone_slab;
        boolean check6_s = FrogMod.mc.theWorld.getBlockState(new BlockPos(x -2, y , z +1)).getBlock() == Blocks.cobblestone_wall;
        boolean check7_s = FrogMod.mc.theWorld.getBlockState(new BlockPos(x -2, y , z)).getBlock() == Blocks.cobblestone_wall;
        boolean check8_s = FrogMod.mc.theWorld.getBlockState(new BlockPos(x -1, y, z)).getBlock() == Blocks.air;
        /*
         * EAST
         * CHECKS
         */
        boolean check1_e = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y , z)).getBlock() == Blocks.cobblestone_wall;
        boolean check2_e = FrogMod.mc.theWorld.getBlockState(new BlockPos(x +1, y , z)).getBlock() == Blocks.cobblestone_wall;
        boolean check3_e = FrogMod.mc.theWorld.getBlockState(new BlockPos(x +2, y , z)).getBlock() == Blocks.double_stone_slab;
        boolean check4_e = FrogMod.mc.theWorld.getBlockState(new BlockPos(x +2, y , z +1)).getBlock() == Blocks.double_stone_slab;
        boolean check5_e = FrogMod.mc.theWorld.getBlockState(new BlockPos(x +2, y , z +2)).getBlock() == Blocks.double_stone_slab;
        boolean check6_e = FrogMod.mc.theWorld.getBlockState(new BlockPos(x +1, y , z +2)).getBlock() == Blocks.cobblestone_wall;
        boolean check7_e = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y , z +2)).getBlock() == Blocks.cobblestone_wall;
        boolean check8_e = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y , z +1)).getBlock() == Blocks.air;

        return check1_n && check2_n && check3_n && check4_n && check5_n && check6_n && check7_n && check8_n|| check1_w && check2_w && check3_w && check4_w && check5_w && check6_w && check7_w && check8_w|| check1_s && check2_s && check3_s && check4_s && check5_s && check6_s && check7_s && check8_s|| check1_e && check2_e && check3_e && check4_e && check5_e && check6_e && check7_e && check8_e;
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
