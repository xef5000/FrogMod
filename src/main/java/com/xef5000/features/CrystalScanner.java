package com.xef5000.features;

import com.xef5000.FrogMod;
import com.xef5000.utils.LocationManager;
import com.xef5000.utils.Visual;
import com.xef5000.utils.WaypointsManager;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;

public class CrystalScanner {

    private static final CrystalScanner INSTANCE = new CrystalScanner();

    private HashMap<String, BlockPos> structureCoords = new HashMap<>();
    /*
        Locations (Keys):
        - Throne
        - City
        - MinesDivan
        - GoblinQueen
        - GolbinKing
        - JungleTemple
        - Xalx
     */

    private int ticks;
    public static CrystalScanner getInstance() {
        return INSTANCE;
    }




    @SubscribeEvent
    public void onTick(TickEvent event) {
        if(FrogMod.mc == null || FrogMod.mc.theWorld == null || FrogMod.mc.thePlayer == null && !LocationManager.getInstance().getLocation().equals("crystal_hollows")) return;
        if (FrogMod.INSTANCE.getFrogModConfig().crystalScanner) {
            ticks++;
            if (ticks % ((FrogMod.INSTANCE.getFrogModConfig().crystalScannerDelay) * 20) == 0) {
                ticks = 0;
                scan();
            }
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        WaypointsManager.removeByName("Throne");
        WaypointsManager.removeByName("City");
        WaypointsManager.removeByName("Goblin Queen");
        WaypointsManager.removeByName("Goblin King");
        WaypointsManager.removeByName("Xalx");
        WaypointsManager.removeByName("Pete");
        WaypointsManager.removeByName("Jungle Temple");
        WaypointsManager.removeByName("Mines of Divan");
        WaypointsManager.removeByName("Corleone");
        WaypointsManager.removeByName("Bal");
        WaypointsManager.removeByName("Fairy Grotto");
        WaypointsManager.removeByName("Odawa");

        structureCoords.clear();
    }

    private void precursorRemnantsScan() {
        new Thread(() -> {

            int x, y, z;

            int belowY = 31;
            int aboveY = 189;

            for (x = 513; x < 823; ++x) {
                for(y = belowY; y < aboveY; ++y) {
                    for (z = 513; z < 813; ++z) {
                        if (FrogMod.INSTANCE.getFrogModConfig().crystalScannerMainStructures1) {
                            if (!structureCoords.containsKey("City") && FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.cobblestone_wall && isCity(x, y, z)) {
                                BlockPos cityCoords = new BlockPos(x + 9, y - 23, z - 7);
                                structureCoords.put("City", cityCoords);
                                WaypointsManager.addWaypoint(cityCoords, "City");
                                FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + "Found city at: "  + EnumChatFormatting.YELLOW + "X = " + cityCoords.getX() + ", Y = " + cityCoords.getY() + ", Z = " + cityCoords.getZ()));
                            }
                        }
                        if (FrogMod.INSTANCE.getFrogModConfig().crystalScannerImportantStructures2) {
                            if (!structureCoords.containsKey("Throne") && FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.cobblestone_wall && isThrone(x, y, z)) {
                                BlockPos throneCoords = new BlockPos(x - 1, y - 1, z - 2);
                                structureCoords.put("Throne", throneCoords);
                                WaypointsManager.addWaypoint(throneCoords, "Throne");
                                FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + "Found throne at: "  + EnumChatFormatting.YELLOW + "X = " + throneCoords.getX() + ", Y = " + throneCoords.getY() + ", Z = " + throneCoords.getZ()));
                                Visual.showTitle("&cFOUND THRONE", "", 5, 45, 5);
                                Minecraft.getMinecraft().thePlayer.playSound("random.orb", 1, 6);
                            }
                        }

                    }
                }
            }
        }).start();
    }

    private void golbinHoldoutScan() {
        new Thread(() -> { // QUEEN SCAN

            int x, y, z;

            int belowY = 31;
            int aboveY = 189;

            for (x = 202; x < 523; ++x) {
                for(y = belowY; y < aboveY; ++y) {
                    for (z = 513; z < 823; ++z) {
                        if (FrogMod.INSTANCE.getFrogModConfig().crystalScannerMainStructures1) {
                            if (!structureCoords.containsKey("GoblinQueen") && FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.oak_fence && isQueen(x, y, z)) {
                                BlockPos queenCoords = new BlockPos(x + 1, y - 11, z + 4);
                                structureCoords.put("GoblinQueen", queenCoords);
                                WaypointsManager.addWaypoint(queenCoords, "Goblin Queen");
                                FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + "Found goblin queen at: "  + EnumChatFormatting.YELLOW + "X = " + queenCoords.getX() + ", Y = " + queenCoords.getY() + ", Z = " + queenCoords.getZ()));
                            } else if (!structureCoords.containsKey("GoblinKing") && FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.wool && isKing(x, y, z)) {
                                BlockPos kingCoords = new BlockPos(x + 1, y, z + 1);
                                structureCoords.put("GoblinKing", kingCoords);
                                WaypointsManager.addWaypoint(kingCoords, "Goblin King");
                                FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + "Found goblin king at: "  + EnumChatFormatting.YELLOW + "X = " + kingCoords.getX() + ", Y = " + kingCoords.getY() + ", Z = " + kingCoords.getZ()));
                            }
                        }
                        if (FrogMod.INSTANCE.getFrogModConfig().crystalScannerImportantStructures2) {
                            if (!structureCoords.containsKey("Grotto") && FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.stained_glass && FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getValue(BlockStainedGlass.COLOR) == EnumDyeColor.MAGENTA) {
                                BlockPos grottoCoords = new BlockPos(x, y, z);
                                structureCoords.put("Grotto", grottoCoords);
                                WaypointsManager.addWaypoint(grottoCoords, "Fairy Grotto");
                                FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + "Found fairy grotto at: " + EnumChatFormatting.YELLOW + "X = " + grottoCoords.getX() + ", Y = " + grottoCoords.getY() + ", Z = " + grottoCoords.getZ()));
                            }
                        }
                        if (FrogMod.INSTANCE.getFrogModConfig().crystalScannerOtherStructures3) {
                            if (!structureCoords.containsKey("Xalx") && FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.oak_fence && isXalx(x, y, z)) {
                                BlockPos xalxCoords = new BlockPos(x - 2, y, z - 5);
                                structureCoords.put("Xalx", xalxCoords);
                                WaypointsManager.addWaypoint(xalxCoords, "Xalx");
                                FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + "Found xalx at: "  + EnumChatFormatting.YELLOW + "X = " + xalxCoords.getX() + ", Y = " + xalxCoords.getY() + ", Z = " + xalxCoords.getZ()));
                            } else if (!structureCoords.containsKey("Pete") && FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.cobblestone_wall && isPete(x, y, z)) {
                                BlockPos peteCoords = new BlockPos(x + 3, y, z + 1);
                                structureCoords.put("Pete", peteCoords);
                                WaypointsManager.addWaypoint(peteCoords, "Pete");
                                FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + "Found pete at: "  + EnumChatFormatting.YELLOW + "X = " + peteCoords.getX() + ", Y = " + peteCoords.getY() + ", Z = " + peteCoords.getZ()));
                            }
                        }
                    }
                }
            }
        }).start();
    }

    private void jungleScan() {
        new Thread(() -> { // QUEEN SCAN

            int x, y, z;

            int belowY = 31;
            int aboveY = 189;

            for (x = 202; x < 523; ++x) {
                for(y = belowY; y < aboveY; ++y) {
                    for (z = 202; z < 523; ++z) {
                        if (FrogMod.INSTANCE.getFrogModConfig().crystalScannerMainStructures1) {
                            if (!structureCoords.containsKey("JungleTemple") && FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.carpet && isTemple(x, y, z)) {
                                BlockPos jungleCoords = new BlockPos(x - 3, y - 39, z + 5);
                                structureCoords.put("JungleTemple", jungleCoords);
                                WaypointsManager.addWaypoint(jungleCoords, "Jungle Temple");
                                FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + "Found jungle temple at: "  + EnumChatFormatting.YELLOW + "X = " + jungleCoords.getX() + ", Y = " + jungleCoords.getY() + ", Z = " + jungleCoords.getZ()));
                            }
                        }
                        if (FrogMod.INSTANCE.getFrogModConfig().crystalScannerImportantStructures2) {
                            if (!structureCoords.containsKey("Grotto") && FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.stained_glass && FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getValue(BlockStainedGlass.COLOR) == EnumDyeColor.MAGENTA) {
                                BlockPos grottoCoords = new BlockPos(x, y, z);
                                structureCoords.put("Grotto", grottoCoords);
                                WaypointsManager.addWaypoint(grottoCoords, "Fairy Grotto");
                                FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + "Found fairy grotto at: " + EnumChatFormatting.YELLOW + "X = " + grottoCoords.getX() + ", Y = " + grottoCoords.getY() + ", Z = " + grottoCoords.getZ()));

                            }
                        }
                        if (FrogMod.INSTANCE.getFrogModConfig().crystalScannerOtherStructures3) {
                            if (!structureCoords.containsKey("Odawa") && FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.log && isOdawa(x, y, z)) {
                                BlockPos odawaCoords = new BlockPos(x - 6, y + 3, z + 9);
                                structureCoords.put("Odawa", odawaCoords);
                                WaypointsManager.addWaypoint(odawaCoords, "Odawa");
                                FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + "Found odawa at: " + EnumChatFormatting.YELLOW + "X = " + odawaCoords.getX() + ", Y = " + odawaCoords.getY() + ", Z = " + odawaCoords.getZ()));

                            }
                        }

                    }
                }
            }
        }).start();
    }

    private void mithrilDepositScan() {
        new Thread(() -> { // QUEEN SCAN

            int x, y, z;

            int belowY = 31;
            int aboveY = 189;

            for (x = 523; x < 823; ++x) {
                for(y = belowY; y < aboveY; ++y) {
                    for (z = 202; z < 523; ++z) {
                        if (FrogMod.INSTANCE.getFrogModConfig().crystalScannerMainStructures1) {
                            if (!structureCoords.containsKey("MinesDivan") && FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.stained_glass && isDivan(x, y, z)) {
                                BlockPos divanCoords = new BlockPos(x + 14, y - 35, z - 14);
                                structureCoords.put("MinesDivan", divanCoords);
                                WaypointsManager.addWaypoint(divanCoords, "Mines of Divan");
                                FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + "Found mines of divan at: "  + EnumChatFormatting.YELLOW + "X = " + divanCoords.getX() + ", Y = " + divanCoords.getY() + ", Z = " + divanCoords.getZ()));
                            }
                        }
                        if (FrogMod.INSTANCE.getFrogModConfig().crystalScannerImportantStructures2) {
                            if (!structureCoords.containsKey("Corleone") &&
                                    (FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.cobblestone_wall || FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.log) &&
                                    isCorleone(x, y, z)) {
                                BlockPos corleoneCoords = new BlockPos(x, y, z);
                                structureCoords.put("Corleone", corleoneCoords);
                                WaypointsManager.addWaypoint(corleoneCoords, "Corleone");
                                FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + "Found corleone at: "  + EnumChatFormatting.YELLOW + "X = " + corleoneCoords.getX() + ", Y = " + corleoneCoords.getY() + ", Z = " + corleoneCoords.getZ()));
                            } else if (!structureCoords.containsKey("Grotto") && FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.stained_glass && FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getValue(BlockStainedGlass.COLOR) == EnumDyeColor.MAGENTA) {
                                BlockPos grottoCoords = new BlockPos(x, y, z);
                                structureCoords.put("Grotto", grottoCoords);
                                WaypointsManager.addWaypoint(grottoCoords, "Fairy Grotto");
                                FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + "Found fairy grotto at: "  + EnumChatFormatting.YELLOW + "X = " + grottoCoords.getX() + ", Y = " + grottoCoords.getY() + ", Z = " + grottoCoords.getZ()));

                            }
                        }

                    }
                }
            }
        }).start();
    }

    private void magmaScan() {
        new Thread(() -> { // MAGMA SCAN

            int x, y, z;

            int belowY = 31;
            int aboveY = 80;

            for (x = 202; x < 823; ++x) {
                for(y = belowY; y < aboveY; ++y) {
                    for (z = 202; z < 823; ++z) {
                        if (FrogMod.INSTANCE.getFrogModConfig().crystalScannerMainStructures1) {
                            if (!structureCoords.containsKey("Bal") && FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.cobblestone_wall && isBal(x, y, z)) {
                                BlockPos balCoords = new BlockPos(x + 15, y - 10, z - 21);
                                structureCoords.put("Bal", balCoords);
                                WaypointsManager.addWaypoint(balCoords, "Bal");
                                FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + "Found bal at: "  + EnumChatFormatting.YELLOW + "X = " + balCoords.getX() + ", Y = " + balCoords.getY() + ", Z = " + balCoords.getZ()));
                            }
                        }
                        if (FrogMod.INSTANCE.getFrogModConfig().crystalScannerImportantStructures2) {
                            if (!structureCoords.containsKey("Grotto") && FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.stained_glass && FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getValue(BlockStainedGlass.COLOR) == EnumDyeColor.MAGENTA) {
                                BlockPos grottoCoords = new BlockPos(x, y, z);
                                structureCoords.put("Grotto", grottoCoords);
                                WaypointsManager.addWaypoint(grottoCoords, "Fairy Grotto");
                                FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + "Found fairy grotto at: "  + EnumChatFormatting.YELLOW + "X = " + grottoCoords.getX() + ", Y = " + grottoCoords.getY() + ", Z = " + grottoCoords.getZ()));

                            }
                        }

                    }
                }
            }
        }).start();
    }



    public void scan() {
        if (!structureCoords.containsKey("Throne") || !structureCoords.containsKey("City") || !structureCoords.containsKey("Grotto")) precursorRemnantsScan();
        if (!structureCoords.containsKey("GoblinQueen") || !structureCoords.containsKey("GoblinKing") || !structureCoords.containsKey("Xalx") || !structureCoords.containsKey("Pete") || !structureCoords.containsKey("Grotto")) golbinHoldoutScan();
        if (!structureCoords.containsKey("JungleTemple") || !structureCoords.containsKey("Odawa") || !structureCoords.containsKey("Grotto")) jungleScan();
        if (!structureCoords.containsKey("MinesDivan") || !structureCoords.containsKey("Corleone") | !structureCoords.containsKey("Grotto")) mithrilDepositScan();
        if (!structureCoords.containsKey("Bal") | !structureCoords.containsKey("Grotto")) magmaScan();


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

    private boolean isQueen(double x, double y, double z) {
        /*
         * SOUTH
         * CHECKS
         */
        boolean check1_s = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y , z)).getBlock() == Blocks.oak_fence;
        boolean check2_s = FrogMod.mc.theWorld.getBlockState(new BlockPos(x + 1, y , z)).getBlock() == Blocks.oak_fence;
        boolean check3_s = FrogMod.mc.theWorld.getBlockState(new BlockPos(x + 2, y , z)).getBlock() == Blocks.oak_fence;
        boolean check4_s = FrogMod.mc.theWorld.getBlockState(new BlockPos(x - 4, y + 1 , z)).getBlock() == Blocks.leaves;
        boolean check5_s = FrogMod.mc.theWorld.getBlockState(new BlockPos(x - 3, y + 1 , z + 1)).getBlock() == Blocks.leaves;
        boolean check6_s = FrogMod.mc.theWorld.getBlockState(new BlockPos(x - 5, y + 1 , z + 1)).getBlock() == Blocks.leaves;

        return check1_s && check2_s && check3_s && check4_s && check5_s && check6_s;
    }

    private boolean isKing(double x, double y, double z) {
        /*
         * NORTH
         * CHECKS
         */
        boolean check1_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y , z)).getBlock() == Blocks.wool;
        boolean check2_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x + 1, y , z)).getBlock() == Blocks.wool;
        boolean check3_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x + 2, y , z)).getBlock() == Blocks.wool;
        boolean check4_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x + 1, y , z - 1)).getBlock() == Blocks.wool;
        boolean check5_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x - 1, y + 1 , z)).getBlock() == Blocks.dark_oak_stairs;
        boolean check6_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x + 3, y + 1 , z)).getBlock() == Blocks.dark_oak_stairs;

        return check1_n && check2_n && check3_n && check4_n && check5_n && check6_n;
    }

    private boolean isXalx(double x, double y, double z) {
        /*
         * NORTH
         * CHECKS
         */
        boolean check1_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y , z)).getBlock() == Blocks.oak_fence;
        boolean check2_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y + 1 , z)).getBlock() == Blocks.oak_fence;
        if(!check2_n) return false;
        boolean check3_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y + 1 , z - 1)).getBlock() == Blocks.quartz_ore;
        boolean check4_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x , y  + 1, z - 2)).getBlock() == Blocks.quartz_ore;
        boolean check5_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x , y + 1 , z - 3)).getBlock() == Blocks.quartz_ore;
        boolean check6_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x , y + 1 , z - 4)).getBlock() == Blocks.oak_fence;
        boolean check7_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x , y , z - 4)).getBlock() == Blocks.oak_fence;
        boolean check8_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x , y - 1 , z - 4)).getBlock() == Blocks.cobblestone;
        boolean check9_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x , y - 1 , z)).getBlock() == Blocks.cobblestone;
        boolean check10_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x , y - 1 , z - 1)).getBlock() == Blocks.coal_block;
        boolean check11_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x - 1, y - 1 , z)).getBlock() == Blocks.stone_stairs;

        return check1_n && check2_n && check3_n && check4_n && check5_n && check6_n && check7_n && check8_n && check9_n && check10_n && check11_n;

    }

    private boolean isPete(double x, double y, double z) {
        boolean check1_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y , z)).getBlock() == Blocks.cobblestone_wall;
        boolean check2_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y + 1 , z)).getBlock() == Blocks.iron_bars;
        boolean check3_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y - 1 , z)).getBlock() == Blocks.planks;

        return check1_n && check2_n && check3_n;
    }

    private boolean isTemple(double x, double y, double z) {
        /*
         * NORTH
         * CHECKS
         */
        boolean check1_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.carpet;
        boolean check2_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x - 6, y, z)).getBlock() == Blocks.carpet;
        boolean check3_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y, z - 6)).getBlock() == Blocks.carpet;
        boolean check4_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x - 6, y, z - 6)).getBlock() == Blocks.carpet;
        boolean check5_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x - 1, y - 1, z)).getBlock() == Blocks.hardened_clay;

        return check1_n && check2_n && check3_n && check4_n && check5_n;
    }

    private boolean isDivan(double x, double y, double z) {
        /*
         * NORTH
         * CHECKS
         */
        boolean check1_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.stained_glass;
        boolean check2_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y, z + 1)).getBlock() == Blocks.stone_brick_stairs;
        boolean check3_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x + 5, y, z - 5)).getBlock() == Blocks.stained_glass;
        boolean check4_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x + 5, y, z - 4)).getBlock() == Blocks.stone_brick_stairs;

        return check1_n && check2_n && check3_n && check4_n;
    }

    private boolean isCorleone(double x, double y, double z) {

        // Variant 1:
        boolean check1_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y , z)).getBlock() == Blocks.cobblestone_wall;
        boolean check2_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y + 1 , z)).getBlock() == Blocks.cobblestone_wall;
        boolean check3_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y - 1 , z)).getBlock() == Blocks.stone;
        boolean check4_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y , z - 1)).getBlock() == Blocks.stone_brick_stairs;
        boolean check5_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x + 2, y , z - 2)).getBlock() == Blocks.cobblestone_wall;
        boolean check6_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y - 2, z + 1)).getBlock() == Blocks.double_stone_slab;
        boolean check7_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y + 2, z)).getBlock() == Blocks.double_stone_slab;
        boolean check8_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x + 4, y, z + 5)).getBlock() == Blocks.stone_slab;

        //Variant 2:
        boolean check1_s = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y , z)).getBlock() == Blocks.log;
        boolean check2_s = FrogMod.mc.theWorld.getBlockState(new BlockPos(x - 5, y - 2 , z + 3)).getBlock() == Blocks.cobblestone;
        boolean check3_s = FrogMod.mc.theWorld.getBlockState(new BlockPos(x + 1, y , z - 1)).getBlock() == Blocks.stained_hardened_clay;
        boolean check4_s = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y - 1, z)).getBlock() == Blocks.log;
        boolean check5_s = FrogMod.mc.theWorld.getBlockState(new BlockPos(x - 1, y , z - 6)).getBlock() == Blocks.log;


        return check1_n && check2_n && check3_n && check4_n && check5_n && check6_n && check7_n && check8_n || check1_s && check2_s && check3_s && check4_s && check5_s;
    }

    private boolean isBal(double x, double y, double z) {
        boolean check1_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y , z)).getBlock() == Blocks.cobblestone_wall;
        boolean check2_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x + 1, y , z)).getBlock() == Blocks.cobblestone;
        boolean check3_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y + 1 , z)).getBlock() == Blocks.cobblestone_wall;
        boolean check4_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y + 2, z)).getBlock() == Blocks.cobblestone_wall;
        boolean check5_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x + 5, y - 1, z)).getBlock() == Blocks.cobblestone_wall; //
        boolean check6_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x + 8, y - 8, z - 4)).getBlock() == Blocks.cobblestone_wall;
        boolean check7_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x + 1, y + 6, z - 1)).getBlock() == Blocks.cobblestone_wall;


        return check1_n && check2_n && check3_n && check4_n && check5_n && check6_n && check7_n;
    }

    private boolean isOdawa(double x, double y, double z) {
        boolean check1_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y , z)).getBlock() == Blocks.log;
        boolean check2_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x + 1, y , z + 1)).getBlock() == Blocks.log;
        boolean check3_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y , z + 2)).getBlock() == Blocks.log;
        boolean check4_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x - 1, y , z + 3)).getBlock() == Blocks.log;
        boolean check5_n = FrogMod.mc.theWorld.getBlockState(new BlockPos(x, y + 1 , z)).getBlock() == Blocks.spruce_fence;


        return check1_n && check2_n && check3_n && check4_n && check5_n;
    }




}
