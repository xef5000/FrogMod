package com.xef5000.utils;

import com.xef5000.FrogMod;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.HashMap;

public class WaypointsManager {

    private static HashMap<BlockPos, String> waypoints = new HashMap<>();

    @SubscribeEvent
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        for (BlockPos coords : waypoints.keySet()) {
            String str = waypoints.get(coords);
            Visual.drawFilledEsp(coords, Color.GREEN);
            Visual.renderWaypointText(str, coords, event.partialTicks);
        }

    }

    public static void addWaypoint(BlockPos pos, String str) {
        waypoints.put(pos, str);
    }

    public static void removeWaypoint(BlockPos pos) {
        waypoints.remove(pos);
    }

    public static void removeByName(String name) {
        waypoints.remove(getPosByName(name));
    }

    private static BlockPos getPosByName(String name) {
        for (BlockPos pos : waypoints.keySet()) {
            if (waypoints.get(pos).equals(name)) {
                return pos;
            }
        }
        return null;
    }

    public static String getWaypoint(String name) {
        return waypoints.get(getPosByName(name));
    }

    public static void modifyWaypoint(String name, BlockPos pos) {
        removeByName(name);
        waypoints.put(pos, name);
    }

}
