package com.xef5000;

import com.xef5000.gui.buttons.ButtonLocation;
import com.xef5000.listeners.RenderListener;
import net.minecraft.client.Minecraft;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum Feature {

    TEST_TEXT(1, 0, "testText"),
    MILESTONE_OVERLAY(2, 0, "milestoneDisplay");


    public static final Set<Feature> guiFeatures = new HashSet<>(Arrays.asList(new Feature[]{MILESTONE_OVERLAY}));



    private int id;

    // Drawtypes:
    // 0 = text
    private int drawType;
    private String configName;

    Feature(int id, int drawType, String configName) {
        this.id = id;
        this.drawType = drawType;
        this.configName = configName;
        FrogMod.INSTANCE.getRegisteredFeatureIDs().add(id);
    }


    public void draw(float scale, Minecraft mc, ButtonLocation buttonLocation) {
        if (drawType == 0) {
            RenderListener.getInstance().drawText(this, scale, mc, buttonLocation);
        }

    }

    public String getConfigName() {return configName;}
    public int getDrawType() {return drawType;}

    public static Set<Feature> getGuiFeatures() { return guiFeatures; }
    public int getId() {return id;}

    public static Feature fromId(int id) {
        for (Feature feature : values()) {
            if (feature.getId() == id) {
                return feature;
            }
        }
        return null;
    }

}
