package com.xef5000.features;

import com.xef5000.FrogMod;
import com.xef5000.utils.Visual;
import gg.essential.universal.UMatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import scala.Int;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TerminalOverlay {

    private static ArrayList<TileEntityCommandBlock> queueRenderRed = new ArrayList<>();
    private static ArrayList<TileEntityCommandBlock> queueRenderGreen = new ArrayList<>();
    public static boolean phase3 = false;

    public static void resetRenderQueues() {
        queueRenderGreen.clear();
        queueRenderRed.clear();
    }

    @SubscribeEvent
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        if (!FrogMod.INSTANCE.getFrogModConfig().terminalOverlay) return;
        for (TileEntityCommandBlock commandBlock : queueRenderRed) {
            Visual.drawFilledEsp(new BlockPos(commandBlock.getPos().getX(), commandBlock.getPos().getY(), commandBlock.getPos().getZ()), Color.RED);
            Visual.renderWaypointText("Inactive Terminal", new BlockPos(commandBlock.getPos().getX(), commandBlock.getPos().getY(), commandBlock.getPos().getZ()), event.partialTicks);
        }
        for (TileEntityCommandBlock commandBlock : queueRenderGreen) {
            Visual.drawFilledEsp(new BlockPos(commandBlock.getPos().getX(), commandBlock.getPos().getY(), commandBlock.getPos().getZ()), Color.GREEN);
            Visual.renderWaypointText("Terminal Active", new BlockPos(commandBlock.getPos().getX(), commandBlock.getPos().getY(), commandBlock.getPos().getZ()), event.partialTicks);
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
                        }
                    }
                    if (armorStand.getName().contains("Inactive Terminal")) {
                        if (!queueRenderRed.contains(commandBlock)) {
                            if (queueRenderGreen.contains(commandBlock)) return;
                            queueRenderRed.add(commandBlock);
                        }
                    }
                }

            }
        }
        System.out.println("Scanned map!");
        System.out.println("Queue Render Red: " + queueRenderRed.size());
        System.out.println("Queue Render Green: " + queueRenderGreen.size());

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
