package com.xef5000.utils;

import com.xef5000.FrogMod;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.*;
import net.minecraft.client.Minecraft;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import net.minecraft.block.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.regex.Pattern;


public class Visual {

    public static final RenderManager renderManager;
    private static final double HALF_PI = Math.PI / 2D;
    private static final double PI = Math.PI;
    public static void showTitle(String title, String subtitle, int fadeIn, int time, int fadeOut) {
        GuiIngame gui = Minecraft.getMinecraft().ingameGUI;
        gui.displayTitle(addColor(title), null, fadeIn, time, fadeOut);
        gui.displayTitle(null, addColor(subtitle), fadeIn, time, fadeOut);
        gui.displayTitle(null, null, fadeIn, time, fadeOut);
    }

    public static String addColor(String message) {
        return message.toString().replaceAll("(?<!\\\\)&(?![^0-9a-fk-or]|$)", "\u00a7");
    }


    public static void drawFilledBlockEsp(final BlockPos pos, final Color c) {
        final Block block = FrogMod.mc.theWorld.getBlockState(pos).getBlock();
        block.setBlockBoundsBasedOnState((IBlockAccess) FrogMod.mc.theWorld, pos);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.color(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, 0.5f);
        drawFullAABB(block.getSelectedBoundingBox((World)FrogMod.mc.theWorld, pos).offset(-Visual.renderManager.viewerPosX, -Visual.renderManager.viewerPosY, -Visual.renderManager.viewerPosZ));
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawFilledEsp(final Vec3 pos, final Color c) {
        final AxisAlignedBB bb = new AxisAlignedBB(pos.xCoord, pos.yCoord, pos.zCoord, pos.xCoord + 1, pos.yCoord + 2, pos.zCoord + 1);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.color(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, 0.5f);
        drawFullAABB(bb.offset(-Visual.renderManager.viewerPosX, -Visual.renderManager.viewerPosY, -Visual.renderManager.viewerPosZ));
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    // Create a function to draw a filled ESP with 2 blocks height for entities
    public static void drawFilledEsp(final Entity entity, final Color c) {
        final AxisAlignedBB bb = entity.getEntityBoundingBox();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.color(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, 0.5f);
        drawFullAABB(bb.offset(-entity.posX, -entity.posY, -entity.posZ));
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    private static void drawFullAABB(final AxisAlignedBB aabb) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        tessellator.draw();
    }

    /**
     * Taken from NotEnoughUpdates under Creative Commons Attribution-NonCommercial 3.0
     * https://github.com/Moulberry/NotEnoughUpdates/blob/master/LICENSE
     * @author Moulberry
     */
    public static void renderWaypointText(String str, BlockPos loc, float partialTicks) {
        GlStateManager.alphaFunc(516, 0.1F);

        GlStateManager.pushMatrix();

        Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
        double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks;
        double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks;
        double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks;

        double x = loc.getX() + 0.5 - viewerX;
        double y = loc.getY() - viewerY - viewer.getEyeHeight() + 1;
        double z = loc.getZ() + 0.5 - viewerZ;

        double distSq = x*x + y*y + z*z;
        double dist = Math.sqrt(distSq);
        if(distSq > 144) {
            x *= 12/dist;
            y *= 12/dist;
            z *= 12/dist;
        }
        GlStateManager.translate(x, y, z);
        GlStateManager.translate(0, viewer.getEyeHeight(), 0);

        drawNametag(str);

        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(Minecraft.getMinecraft().getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(0, -0.25f, 0);
        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);

        drawNametag(EnumChatFormatting.YELLOW.toString()+Math.round(dist)+"m");

        GlStateManager.popMatrix();

        GlStateManager.disableLighting();
    }
    /**
     * Taken from NotEnoughUpdates under Creative Commons Attribution-NonCommercial 3.0
     * https://github.com/Moulberry/NotEnoughUpdates/blob/master/LICENSE
     * @author Moulberry
     */
    public static void drawNametag(String str) {
        FontRenderer fontrenderer = Minecraft.getMinecraft().fontRendererObj;
        float f = 2.5F;
        float f1 = 0.016666668F * f;
        GlStateManager.pushMatrix();
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(Minecraft.getMinecraft().getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-f1, -f1, f1);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        int i = 0;

        int j = fontrenderer.getStringWidth(str) / 2;
        GlStateManager.disableTexture2D();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(-j - 1, -1 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos(-j - 1, 8 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos(j + 1, 8 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos(j + 1, -1 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, 553648127);
        GlStateManager.depthMask(true);

        fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, -1);

        GlStateManager.enableDepth();
        GlStateManager.enableBlend();
        GlStateManager.color(0.5F, 0.1F, 255.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    //public static void drawList(Gui)

    public static void drawText(String text, float x, float y, int color, boolean dropshadow) {
        if (text == null) return;
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
        int colorAlpha = Math.max(getAlpha(color), 4);
        int colorBlack = new Color(0, 0, 0, colorAlpha / 255F).getRGB();
        String strippedText = Pattern.compile("(?i)§[0-9A-FK-ORZ]").matcher(text).replaceAll("");
        fontRenderer.drawString(strippedText, x + 1, y + 0, colorBlack, dropshadow);
        fontRenderer.drawString(strippedText, x - 1, y + 0, colorBlack, dropshadow);
        fontRenderer.drawString(strippedText, x + 0, y + 1, colorBlack, dropshadow);
        fontRenderer.drawString(strippedText, x + 0, y - 1, colorBlack, dropshadow);
        fontRenderer.drawString(text, x + 0, y + 0, color, dropshadow);
    }

    public static int getAlpha(int color) {
        return color >> 24 & 0xFF;
    }
    public static int getRed(int color) {return color >> 16 & 0xFF;}
    public static int getGreen(int color) {
        return color >> 8 & 0xFF;
    }
    public static int getBlue(int color) {
        return color & 0xFF;
    }

    public static void drawRectAbsolute(double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double savedLeft = left;
            left = right;
            right = savedLeft;
        }
        if (top < bottom) {
            double savedTop = top;
            top = bottom;
            bottom = savedTop;
        }
        drawRectInternal(left, top, right - left, bottom - top, color, 0);
    }

    private static void drawRectInternal(double x, double y, double w, double h, int color, int rounding) {
        if (rounding > 0) {
            drawRoundedRectangle(x, y, w, h, color, rounding);
            return;
        }

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        Tessellator.getInstance().getWorldRenderer().begin(7, DefaultVertexFormats.POSITION_COLOR);

        addQuadVertices(x, y, w, h, color);

        Tessellator.getInstance().draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    private static void drawRoundedRectangle(double x, double y, double w, double h, int color, double rounding) {
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        GlStateManager.disableTexture2D();

        double x1, y1, x2, y2;
        // Main vertical rectangle
        x1 = x + rounding;
        x2 = x + w - rounding;
        y1 = y;
        y2 = y + h;
        addVertex(x1, y2, color);
        addVertex(x2, y2, color);
        addVertex(x2, y1, color);
        addVertex(x1, y1, color);

        // Left rectangle
        x1 = x;
        x2 = x + rounding;
        y1 = y + rounding;
        y2 = y + h - rounding;
        addVertex(x1, y2, color);
        addVertex(x2, y2, color);
        addVertex(x2, y1, color);
        addVertex(x1, y1, color);

        // Right rectangle
        x1 = x + w - rounding;
        x2 = x + w;
        y1 = y + rounding;
        y2 = y + h - rounding;
        addVertex(x1, y2, color);
        addVertex(x2, y2, color);
        addVertex(x2, y1, color);
        addVertex(x1, y1, color);

        int segments = 64;
        double angleStep = HALF_PI / (float) segments;

        // Top left corner
        double startAngle = -HALF_PI;
        double startX = x + rounding;
        double startY = y + rounding;
        addVertex(startX, startY, color);
        for (int segment = 0; segment <= segments; segment++) {
            double angle = startAngle - angleStep * segment;
            addVertex(startX + rounding * Math.cos(angle), startY + rounding * Math.sin(angle), color);
        }

        // Top right corner
        startAngle = 0;
        startX = x + w - rounding;
        startY = y + rounding;
        addVertex(startX, startY, color);
        for (int segment = 0; segment <= segments; segment++) {
            double angle = startAngle - angleStep * segment;
            addVertex(startX + rounding * Math.cos(angle), startY + rounding * Math.sin(angle), color);
        }

        // Bottom right corner
        startAngle = HALF_PI;
        startX = x + w - rounding;
        startY = y + h - rounding;
        addVertex(startX, startY, color);
        for (int segment = 0; segment <= segments; segment++) {
            double angle = startAngle - angleStep * segment;
            addVertex(startX + rounding * Math.cos(angle), startY + rounding * Math.sin(angle), color);
        }

        // Bottom right corner
        startAngle = PI;
        startX = x + rounding;
        startY = y + h - rounding;
        addVertex(startX, startY, color);
        for (int segment = 0; segment <= segments; segment++) {
            double angle = startAngle - angleStep * segment;
            addVertex(startX + rounding * Math.cos(angle), startY + rounding * Math.sin(angle), color);
        }

    }

    private static void addQuadVertices(double x, double y, double w, double h, int color) {
        addQuadVerticesAbsolute(x, y, x + w, y + h, color);
    }

    private static void addQuadVerticesAbsolute(double left, double top, double right, double bottom, int color) {
        addVertex(left, bottom, color);
        addVertex(right, bottom, color);
        addVertex(right, top, color);
        addVertex(left, top, color);
    }

    private static void addVertex(double x, double y, int color) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.pos(x, y, 0).color(getRed(color), getGreen(color), getBlue(color), getAlpha(color)).endVertex();
    }




    static {
        renderManager = FrogMod.mc.getRenderManager();
    }
}
