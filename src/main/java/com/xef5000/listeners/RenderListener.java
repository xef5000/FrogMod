package com.xef5000.listeners;

import com.xef5000.Feature;
import com.xef5000.FrogMod;
import com.xef5000.features.TestText;
import com.xef5000.gui.LocationsEditGUI;
import com.xef5000.gui.buttons.ButtonLocation;
import com.xef5000.utils.LocationManager;
import com.xef5000.utils.Visual;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.world.WorldEvent;
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

            for (Field field : FrogMod.INSTANCE.getFrogModConfig().getClass().getFields()) {
                if (field.getName().equals(feature.getConfigName())) {
                    try {
                        activated = field.getBoolean(FrogMod.INSTANCE.getFrogModConfig());
                    } catch (IllegalAccessException e) {
                        System.out.println(e);
                    }

                }
            }
            if (activated) {
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

        if (feature == Feature.TEST_TEXT) {
            text = TestText.getInstance().string;
        }

        int height = 7;
        int width = FrogMod.mc.fontRendererObj.getStringWidth(text);
        float x = FrogMod.INSTANCE.getConfigValues().getRelativeCoords(feature).getX();
        float y = FrogMod.INSTANCE.getConfigValues().getRelativeCoords(feature).getY();
        x = transformXY(x, width, scale);
        y = transformXY(y, height, scale);

        if (feature == Feature.TEST_TEXT) {
            if (FrogMod.INSTANCE.getConfigValues().isEnabled(feature) /*&& buttonLocation != null*/) {
                Visual.drawText(text, x, y, 822368, true);
            }

        }

        if (buttonLocation != null) {
            buttonLocation.checkHoveredAndDrawBox(x, x + width, y, y + height, scale);
        }
    }


    public float transformXY(float xy, int widthHeight, float scale) {
        float minecraftScale = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
        xy -= widthHeight / 2F * scale;
        xy = Math.round(xy * minecraftScale) / minecraftScale;
        return xy / scale;
    }
}
