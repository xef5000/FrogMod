package com.xef5000.features;

import com.xef5000.FrogMod;
import com.xef5000.utils.LocationManager;
import com.xef5000.utils.Visual;
import com.xef5000.utils.WaypointsManager;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;

public class CrystalScanner {

    private static final CrystalScanner INSTANCE = new CrystalScanner();

    private HashMap<String, BlockPos> structureCoords = new HashMap<>();
    /*
        Locations:
        - Throne
        - City
        - MinesDivan
        - GoblinQueen
        - JungleTemple
     */

    public static CrystalScanner getInstance() {
        return INSTANCE;
    }


    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) { // TODO: Add if in chollows check
        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(FrogMod.mc == null || FrogMod.mc.theWorld == null || FrogMod.mc.thePlayer == null) return;
            FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + "WORLD SCAN"));
            if (LocationManager.getInstance().getLocation().equals("crystal_hollows")) {
                FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + "Starting scan..."));
                scan();
                FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + "Finished scan..."));
            }
            FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + LocationManager.getInstance().getLocation()));

        }).start();

    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        WaypointsManager.removeByName("Throne");
        WaypointsManager.removeByName("City");
        WaypointsManager.removeByName("MinesDivan");
        WaypointsManager.removeByName("JungleTemple");
        WaypointsManager.removeByName("GoblinQueen");
        structureCoords.clear();
    }

    private void precursorRemnantsScan() {
        new Thread(() -> { // THRONE SCAN

            int x, y, z;

            int belowY = 31;
            int aboveY = 189;

            for (x = 513; x < 823; ++x) {
                for(y = belowY; y < aboveY; ++y) {
                    for (z = 513; z < 813; ++z) {
                        if (FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.cobblestone_wall && isThrone(x, y, z)) {
                            structureCoords.put("Throne", new BlockPos(x, y, z));
                            WaypointsManager.addWaypoint(new BlockPos(x, y, z), "Throne");
                            FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + "Found throne at: "  + EnumChatFormatting.YELLOW + "X = " + x + ", Y = " + y + ", Z = " + z));
                            Visual.showTitle("&cFOUND THRONE", "", 5, 45, 5);
                            Minecraft.getMinecraft().thePlayer.playSound("random.orb", 1, 6);
                        } else if (FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.cobblestone_wall && isCity(x, y, z)) {
                            structureCoords.put("City", new BlockPos(x, y, z));
                            WaypointsManager.addWaypoint(new BlockPos(x, y, z), "City");
                            FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + "Found city at: "  + EnumChatFormatting.YELLOW + "X = " + x + ", Y = " + y + ", Z = " + z));
                        }
                    }
                }
            }
        }).start();
    }




    public void scan() {
        precursorRemnantsScan();
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

    private boolean isCity(double x, double y, double z) {

        /*
         * SOUTH
         * CHECKS
         */
        boolean check1_s = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y , z)).getBlock() == Blocks.cobblestone_wall; //
        boolean check2_s = FrogMod.mc.theWorld.getBlockState(new BlockPos(x + 2, y , z + 2)).getBlock() == Blocks.cobblestone_wall; //
        boolean check3_s = FrogMod.mc.theWorld.getBlockState(new BlockPos(x + 2, y , z + 4)).getBlock() == Blocks.cobblestone_wall; //
        boolean check4_s = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y , z + 6)).getBlock() == Blocks.cobblestone_wall; //
        boolean check5_s = FrogMod.mc.theWorld.getBlockState(new BlockPos(x - 2, y , z + 6)).getBlock() == Blocks.cobblestone_wall; //
        boolean check6_s = FrogMod.mc.theWorld.getBlockState(new BlockPos(x - 4, y , z + 4)).getBlock() == Blocks.cobblestone_wall; //
        boolean check7_s = FrogMod.mc.theWorld.getBlockState(new BlockPos(x - 4, y , z + 2)).getBlock() == Blocks.cobblestone_wall; //
        boolean check8_s = FrogMod.mc.theWorld.getBlockState(new BlockPos(x - 2, y , z)).getBlock() == Blocks.cobblestone_wall;

        return  check1_s && check2_s && check3_s && check4_s && check5_s && check6_s && check7_s && check8_s;




    }
}
