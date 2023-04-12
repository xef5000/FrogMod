package com.xef5000.listeners;

import com.xef5000.Feature;
import com.xef5000.FrogMod;
import com.xef5000.features.MilestoneOverlay;
import com.xef5000.features.TestText;
import com.xef5000.gui.buttons.ButtonLocation;
import com.xef5000.utils.Visual;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;

public class RenderListener {

    private static final RenderListener instance = new RenderListener();

    public static RenderListener getInstance() {return instance;}

    @SubscribeEvent()
    public void onRenderRegular(RenderGameOverlayEvent.Post event) {
        if(FrogMod.mc == null || FrogMod.mc.thePlayer == null) return;
        if (Minecraft.getMinecraft().ingameGUI instanceof GuiIngameForge) {
            if (event.type == RenderGameOverlayEvent.ElementType.EXPERIENCE || event.type == RenderGameOverlayEvent.ElementType.JUMPBAR) {
                // Add check if in skyblock maybe?
                renderOverlays();
            }
        }
    }


    public void renderOverlays(){
        //if (Minecraft.getMinecraft().currentScreen instanceof LocationsEditGUI) return;
        GlStateManager.disableBlend();

        boolean activated = false;
        for (Feature feature : Feature.getGuiFeatures()) {

            if (isFeatureActivated(feature)) {
                float scale = 1.1f;/*FrogMod.INSTANCE.getConfigValues().getGuiScale(feature);*/
                GlStateManager.pushMatrix();
                GlStateManager.scale(scale, scale, 1);
                feature.draw(scale, FrogMod.mc, null);
                GlStateManager.popMatrix();
            }
        }

    }


    public void drawText(Feature feature, float scale, Minecraft mc, ButtonLocation buttonLocation) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        String text = null;
        String editText = null;

        if (feature == Feature.TEST_TEXT) {
            text = TestText.getInstance().string;
            editText = "TestText";
        } else if (feature == Feature.MILESTONE_OVERLAY) {
            editText = MilestoneOverlay.editGuiString;
        }

        int height = 7;
        int width = FrogMod.mc.fontRendererObj.getStringWidth(text);
        float x = FrogMod.INSTANCE.getConfigValues().getRelativeCoords(feature).getX();
        float y = FrogMod.INSTANCE.getConfigValues().getRelativeCoords(feature).getY();
        x = transformXY(x, width, scale);
        y = transformXY(y, height, scale);

        if (feature == Feature.TEST_TEXT) {
            if (isFeatureActivated(feature) && buttonLocation == null /*&& buttonLocation != null*/) {
                Visual.drawText(text, x, y, 822368, true);
            }

        } else if (feature == Feature.MILESTONE_OVERLAY) {

            if (isFeatureActivated(feature) && buttonLocation == null) {
                if (MilestoneOverlay.currentCrop == null) return;
                String[] currentCrop = MilestoneOverlay.currentCrop;
                int yOffset = 0;
                int xOffset = 0;
                for (String line : currentCrop) {
                    if (line == null) continue;
                    if (line == currentCrop[1]) xOffset = 10;
                    Visual.drawText(line, x + xOffset, y + yOffset, 16766464, true);
                    yOffset += 10;
                    xOffset = 0;
                }
                //yOffset = 0;
                //if (currentCrop[0] != null) Visual.drawText(currentCrop[0], x, y, 16766464, true);
                //if (currentCrop[1] != null) Visual.drawText(currentCrop[1], x + 8, y + 10, 16766464, true);
                //if (currentCrop[2] != null) Visual.drawText(currentCrop[2], x, y + 20, 16766464, true);
                //if (currentCrop[3] != null) Visual.drawText(currentCrop[3], x, y + 30, 16766464, true);
                //if (currentCrop[4] != null) Visual.drawText(currentCrop[4], x, y + 40, 16766464, true);


            } else if (isFeatureActivated(feature) && buttonLocation != null) {
                String[] currentCrop = MilestoneOverlay.currentCrop;
                int[] width_height = recalculateWidthHeight(currentCrop);
                width = width_height[0];
                height = width_height[1];
            } else if (!isFeatureActivated(feature) && buttonLocation != null) {
                String[] currentCrop = new String[]{MilestoneOverlay.editGuiString};
                int[] width_height = recalculateWidthHeight(currentCrop);
                width = width_height[0];
                height = width_height[1];
            }
        }

        if (buttonLocation != null) {
            if (!isFeatureActivated(feature)) Visual.drawText(editText, x, y, 16777215, true);
            buttonLocation.checkHoveredAndDrawBox(x, x + width, y, y + height, scale);

        }
    }


    public float transformXY(float xy, int widthHeight, float scale) {
        float minecraftScale = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
        xy -= widthHeight / 2F * scale;
        xy = Math.round(xy * minecraftScale) / minecraftScale;
        return xy / scale;
    }

    public boolean isFeatureActivated(Feature feature) {
        boolean activated = false;
        for (Field field : FrogMod.INSTANCE.getFrogModConfig().getClass().getFields()) {
            if (field.getName().equals(feature.getConfigName())) {
                try {
                    activated = field.getBoolean(FrogMod.INSTANCE.getFrogModConfig());
                } catch (IllegalAccessException e) {
                    System.out.println(e);
                }

            }
        }
        return activated;
    }

    public int[] recalculateWidthHeight(String[] lines) {
        int width = 0;
        int height = 0;
        for (String string : lines) {
            int stringWidth = FrogMod.mc.fontRendererObj.getStringWidth(string);
            if (stringWidth > width) width = stringWidth;
        }
        height = lines.length * 8;
        return new int[]{width, height};
    }
}
