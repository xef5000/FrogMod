package com.xef5000.features;

import com.xef5000.FrogMod;
import com.xef5000.utils.Visual;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;

public class TerminalOverlay {

    private static ArrayList<TileEntityCommandBlock> queueRenderRed = new ArrayList<>();
    private static ArrayList<TileEntityCommandBlock> queueRenderGreen = new ArrayList<>();
    public static boolean phase3 = false;
    private int tick = 0;

    public static void resetRenderQueues() {
        queueRenderGreen.clear();
        queueRenderRed.clear();
    }

    @SubscribeEvent
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        if (!FrogMod.INSTANCE.getFrogModConfig().terminalOverlay) return;
        for (TileEntityCommandBlock commandBlock : queueRenderRed) {
            Visual.drawFilledBlockEsp(new BlockPos(commandBlock.getPos().getX(), commandBlock.getPos().getY(), commandBlock.getPos().getZ()), Color.RED);
            Visual.renderWaypointText("Inactive Terminal", new BlockPos(commandBlock.getPos().getX(), commandBlock.getPos().getY(), commandBlock.getPos().getZ()), event.partialTicks);
        }
        for (TileEntityCommandBlock commandBlock : queueRenderGreen) {
            Visual.drawFilledBlockEsp(new BlockPos(commandBlock.getPos().getX(), commandBlock.getPos().getY(), commandBlock.getPos().getZ()), Color.GREEN);
            Visual.renderWaypointText("Terminal Active", new BlockPos(commandBlock.getPos().getX(), commandBlock.getPos().getY(), commandBlock.getPos().getZ()), event.partialTicks);
        }


    }

    @SubscribeEvent
    public void onTick(TickEvent event) {
        if(FrogMod.INSTANCE.getFrogModConfig().terminalOverlay) {
            tick++;
            if (tick % 60 == 0) {
                if(phase3) {
                    new Thread(TerminalOverlay::scanMap).start();
                }
            }
        }


    }

    public static void scanMap() {
        ArrayList<EntityArmorStand> listArmorStands = cleanA((ArrayList) Minecraft.getMinecraft().theWorld.loadedEntityList);
        ArrayList<TileEntityCommandBlock> listCommandBlocks = cleanC((ArrayList) Minecraft.getMinecraft().theWorld.loadedTileEntityList);

        //resetRenderQueues();

        for (TileEntityCommandBlock commandBlock : listCommandBlocks) {
            for (EntityArmorStand armorStand : listArmorStands) {
                if (((armorStand.getPosition().getX() - commandBlock.getPos().getX()) > -3 && (armorStand.getPosition().getX() - commandBlock.getPos().getX()) < 3 && (armorStand.getPosition().getY() - commandBlock.getPos().getY()) > -3 && (armorStand.getPosition().getY() - commandBlock.getPos().getY()) < 3 && (armorStand.getPosition().getZ() - commandBlock.getPos().getZ()) > -3 && (armorStand.getPosition().getZ()- commandBlock.getPos().getZ()) < 3)) {
                    if (armorStand.getName().contains("Terminal Active")) {
                        if (!queueRenderGreen.contains(commandBlock)) {
                            queueRenderGreen.add(commandBlock);
                            queueRenderRed.remove(commandBlock);
                            listArmorStands.remove(armorStand);
                            return;
                        }
                    }
                    if (armorStand.getName().contains("Inactive Terminal")) {
                        if (!queueRenderRed.contains(commandBlock)) {
                            if (queueRenderGreen.contains(commandBlock)) return;
                            queueRenderRed.add(commandBlock);
                            listArmorStands.remove(armorStand);
                            return;
                        }
                    }
                }

            }
        }

    }

    private static ArrayList<EntityArmorStand> cleanA(ArrayList list) {
        ArrayList<EntityArmorStand> returned = new ArrayList();
        for (Object i : list) {
            if (i instanceof EntityArmorStand) {
                returned.add((EntityArmorStand) i);
            }
        }
        return returned;
    }
    private static ArrayList<TileEntityCommandBlock> cleanC(ArrayList list) {
            ArrayList<TileEntityCommandBlock> returned = new ArrayList();
            for (Object i : list) {
                if (i instanceof TileEntityCommandBlock) {
                    returned.add((TileEntityCommandBlock) i);
                }
            }
            return returned;
    }


}
