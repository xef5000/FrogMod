package com.xef5000.utils;

import net.minecraft.util.Vec3;

public class HypixelEntity {
    private String type;
    private Vec3 location;
    private String name;
    private boolean stationary;

    public HypixelEntity(String type, Vec3 location, String name, boolean stationary) {
        this.type = type;
        this.location = location;
        this.name = name;
        this.stationary = stationary;
    }

    public String getType() {
        return type;
    }

    public Vec3 getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public boolean isStationary() {
        return stationary;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLocation(Vec3 location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStationary(boolean stationary) {
        this.stationary = stationary;
    }

    public boolean equals(HypixelEntity entity) {
        return entity.getType().equals(type) && entity.getLocation().equals(location) && entity.getName().equals(name) && entity.isStationary() == stationary;
    }
}