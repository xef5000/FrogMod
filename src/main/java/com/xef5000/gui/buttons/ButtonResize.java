package com.xef5000.gui.buttons;

import com.xef5000.FrogMod;
import com.xef5000.utils.ColorCode;
import com.xef5000.utils.Visual;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import com.xef5000.Feature;

//Taken from SBA https://github.com/BiscuitDevelopment/SkyblockAddons/blob/d5f53c0a4e486c5094aefaee683aacd79c1db756/src/main/java/codes/biscuit/skyblockaddons/gui/buttons/ButtonResize.java
public class ButtonResize extends ButtonFeature {

    private static final int SIZE = 2;
    private Corner corner;

    public float x;
    public float y;

    private float cornerOffsetX;
    private float cornerOffsetY;

    public ButtonResize(float x, float y, Feature feature, Corner corner) {
        super(0, 0, 0, "", feature);
        this.corner = corner;
        this.x = x;
        this.y = y;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {

        float scale = FrogMod.INSTANCE.getConfigValues().getGuiScale(feature);
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale,scale,1);

        hovered = mouseX >= (x- SIZE)*scale && mouseY >= (y- SIZE)*scale && mouseX < (x+ SIZE)*scale && mouseY < (y+ SIZE)* scale;
        int color = hovered ? ColorCode.WHITE.getColor() : ColorCode.WHITE.getColor(70);
        Visual.drawRectAbsolute(x- SIZE,y- SIZE, x+ SIZE, y+ SIZE, color);

        GlStateManager.popMatrix();
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        ScaledResolution sr = new ScaledResolution(mc);
        float minecraftScale = sr.getScaleFactor();
        float floatMouseX = Mouse.getX() / minecraftScale;
        float floatMouseY = (mc.displayHeight - Mouse.getY()) / minecraftScale;

        cornerOffsetX = floatMouseX;
        cornerOffsetY = floatMouseY;

        return hovered;
    }

    public Corner getCorner() {return corner;}
    public float getX() {return x;}
    public float getY() {return y;}

    public enum Corner {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_RIGHT,
        BOTTOM_LEFT
    }
}
