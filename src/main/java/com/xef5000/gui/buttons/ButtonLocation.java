package com.xef5000.gui.buttons;

import com.xef5000.Feature;
import com.xef5000.FrogMod;
import com.xef5000.utils.ColorCode;
import com.xef5000.utils.Visual;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class ButtonLocation extends ButtonFeature{

    private static Feature lastHoveredFeature = null;

    private float boxXOne;
    private float boxXTwo;
    private float boxYOne;
    private float boxYTwo;

    public float getBoxXOne() {
        return boxXOne;
    }

    public float getBoxXTwo() {
        return boxXTwo;
    }

    public float getBoxYOne() {
        return boxYOne;
    }

    public float getBoxYTwo() {
        return boxYTwo;
    }

    public float getScale() {
        return scale;
    }

    private float scale;



    public ButtonLocation(Feature feature) {
        super(-1, 0, 0, null, feature);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        float scale = 1.1f/*FrogMod.INSTANCE.getConfigValues().getGuiScale(feature)*/;
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1);

        feature.draw(scale, mc, this);

        GlStateManager.popMatrix();

        if (hovered) lastHoveredFeature = feature;

    }

    public void checkHoveredAndDrawBox(float boxXOne, float boxXTwo, float boxYOne, float boxYTwo, float scale) {

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        float minecraftScale = sr.getScaleFactor();
        float floatMouseX = Mouse.getX() / minecraftScale;
        float floatMouseY = (Minecraft.getMinecraft().displayHeight - Mouse.getY()) / minecraftScale;

        hovered = floatMouseX >= boxXOne * scale && floatMouseY >= boxYOne * scale && floatMouseX < boxXTwo * scale && floatMouseY < boxYTwo * scale;
        int boxAlpha = 70;
        if (hovered) {
            boxAlpha = 120;
        }
        Color ColorCode;
        int boxColor = com.xef5000.utils.ColorCode.GRAY.getColor(boxAlpha);
        Visual.drawRectAbsolute(boxXOne - 2, boxYOne - 2, boxXTwo + 2, boxYTwo + 2, boxColor);

        this.boxXOne = boxXOne - 2;
        this.boxXTwo = boxXTwo + 2;
        this.boxYOne = boxYOne - 2;
        this.boxYTwo = boxYTwo + 2;
        this.scale = scale;


    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return this.enabled && this.visible && hovered;
    }

    public static Feature getLastHoveredFeature() {return lastHoveredFeature;}
}
