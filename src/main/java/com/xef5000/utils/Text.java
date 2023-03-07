package com.xef5000.utils;

import com.xef5000.FrogMod;
import net.minecraft.client.renderer.GlStateManager;
import scala.Int;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

abstract class Text {
    private String string;
    private float x = 0f;
    private float y = 0f;
    private String[] lines = new ArrayList<String>().toArray(new String[0]);
    private long color = 0xffffffffL;
    private boolean formatted = true;
    private boolean shadow = true;
    private float width = 0f;
    private float maxWidth = 0;

    public Text(String string, float x, float y) {
        setString(string);
        setX(x);
        setY(y);
    }

    public void draw(float x, float y) {
        GlStateManager.enableBlend();
        GlStateManager.scale(scale, scale, scale);

        int longestLine = 0;
        String longestLineText = "";
        for (String i : lines) {
            if (Visual.renderManager.getFontRenderer().getStringWidth(i) > Visual.renderManager.getFontRenderer().getStringWidth(longestLineText)){
                longestLineText = i;
                longestLine = Visual.renderManager.getFontRenderer().getStringWidth(longestLineText);
            }
        }

        if(maxWidth != 0) {
            longestLine = coerceAtMost(longestLine, (int) maxWidth);
        }
        width = longestLine;
    }

    private int coerceAtMost(int a, int b) {
        int maxValue;
        int minValue;
        if(a > b) {
            maxValue = a;
            minValue = b;
        } else {
            maxValue = b;
            minValue = a;
        }
        if(a <= maxValue) {
            return a;
        } else return b;
    }


    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String[] getLines() {
        return lines;
    }

    public void setLines(String[] lines) {
        this.lines = lines;
    }

    public long getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isFormatted() {
        return formatted;
    }

    public void setFormatted(boolean formatted) {
        this.formatted = formatted;
    }

    public boolean isShadow() {
        return shadow;
    }

    public void setShadow(boolean shadow) {
        this.shadow = shadow;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(float maxWidth) {
        this.maxWidth = maxWidth;
    }

    public int getMaxLines() {
        return maxLines;
    }

    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    private int maxLines = Int.MaxValue();
    private float scale = 1f;


}
