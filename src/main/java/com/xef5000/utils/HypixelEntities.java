package com.xef5000.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import com.xef5000.utils.HypixelEntity;
import net.minecraft.util.BlockPos;
import java.awt.*;
import net.minecraft.util.Vec3;

public class HypixelEntities {

    private static List<HypixelEntity> entities = new ArrayList<>();
    private static List<String> entityNames = new ArrayList<>();
    private static List<String> stationaryEntities = new ArrayList<>();

    public static void updateEntities() {
        entities.clear();
        for (Entity entity : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (entity instanceof EntityArmorStand) {
                EntityArmorStand armorStand = (EntityArmorStand) entity;
                if (armorStand.hasCustomName()) {
                    String name = StringUtils.stripControlCodes(armorStand.getCustomNameTag());
                    // Check if the entity is in the entityNames list
                    if (entityNames.contains(name)) {
                        // Add the entity to the entities list
                        entities.add(new HypixelEntity(name, new Vec3(armorStand.posX, armorStand.posY, armorStand.posZ), name, stationaryEntities.contains(name)));
                    }
                }
            }
        }
    }

    public static List<HypixelEntity> getEntities() {
        return entities;
    }

    public static HypixelEntity getEntityByName(String name) {
        for (HypixelEntity entity : entities) {
            if (entity.getName().equals(name)) {
                return entity;
            }
        }
        return null;
    }

    public static HypixelEntity getEntityByType(String type) {
        for (HypixelEntity entity : entities) {
            if (entity.getType().equals(type)) {
                return entity;
            }
        }
        return null;
    }

    public static List<HypixelEntity> getEntitiesByName(String name) {
        List<HypixelEntity> entities = new ArrayList<>();
        for (HypixelEntity entity : HypixelEntities.entities) {
            if (entity.getName().equals(name)) {
                entities.add(entity);
            }
        }
        return entities;
    }

    public static List<String> getEntityNames() {
        return entityNames;
    }

    public static void addEntityName(String name) {
        entityNames.add(name);
    }

    public static void removeEntityName(String name) {
        entityNames.remove(name);
    }

    public static void clearEntityNames() {
        entityNames.clear();
    }

    public static void addWaypointToEntities(String name) {
        for (HypixelEntity entity : getEntitiesByName(name)) {
            WaypointsManager.addWaypoint(new BlockPos(entity.getLocation()), entity.getName());
        }
    }

    public static void clearWaypointsFromEntities(String name) {
        for (HypixelEntity entity : getEntitiesByName(name)) {
            WaypointsManager.removeByName(entity.getName());
        }
    }

    public static void addESPToEntities(String name, Color color) {
        for (HypixelEntity entity : getEntitiesByName(name)) {
            Visual.drawFilledEsp(new BlockPos(entity.getLocation()), color);
        }
    }

    public static void addStationaryEntity(String name) {
        stationaryEntities.add(name);
    }

    public static void removeStationaryEntity(String name) {
        stationaryEntities.remove(name);
    }

    public static void clearStationaryEntities() {
        stationaryEntities.clear();
    }

}