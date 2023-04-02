package com.xef5000.gui;

import com.google.common.collect.Sets;
import com.xef5000.ConfigValues;
import com.xef5000.Feature;
import com.xef5000.FrogMod;
import com.xef5000.gui.buttons.ButtonLocation;
import com.xef5000.gui.buttons.ButtonResize;
import com.xef5000.listeners.RenderListener;
import com.xef5000.utils.ColorCode;
import com.xef5000.utils.Visual;
import com.xef5000.utils.objects.AnchorPoint;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public class LocationsEditGUI extends GuiScreen {

    // The feature that is currently being dragged. Might be null if nothing
    private Feature draggedFeature;
    // The feature that is currently being hovered over. Might be null if nothing
    private Feature hoveredFeature;
    private boolean enableSnapping = true;
    private boolean resizing;
    private ButtonResize.Corner resizingCorner;
    private float xOffset;
    private float yOffset;
    private final Map<Feature, ButtonLocation> buttonLocations = new EnumMap<>(Feature.class);
    private static final int SNAPPING_RADIUS = 120;
    private static final int SNAP_PULL = 1;

    @Override
    public void initGui() {
        for (Feature feature : Feature.getGuiFeatures()) {
            if (feature.getDrawType() == 0)  { //Drawtype == text
                ButtonLocation buttonLocation = new ButtonLocation(feature);
                buttonList.add(buttonLocation);
                buttonLocations.put(feature, buttonLocation);
            }
        }

        //addResizeButtonsToAllFeatures();
    }

    private void clearAllResizeButtons() {
        buttonList.removeIf((button) -> button instanceof ButtonResize);
    }

    private void addResizeButtonsToAllFeatures() {
        clearAllResizeButtons();
        // Add all gui elements that can be edited to the gui.
        for (Feature feature : Feature.getGuiFeatures()) {
            addResizeCorners(feature);
        }
    }

    private void addResizeCorners(Feature feature) {
        buttonList.removeIf((button) -> button instanceof ButtonResize && ((ButtonResize)button).getFeature() == feature);

        ButtonLocation buttonLocation = buttonLocations.get(feature);
        if (buttonLocation == null) {
            return;
        }

        float boxXOne = buttonLocation.getBoxXOne();
        float boxXTwo = buttonLocation.getBoxXTwo();
        float boxYOne = buttonLocation.getBoxYOne();
        float boxYTwo = buttonLocation.getBoxYTwo();
        float scaleX = FrogMod.INSTANCE.getConfigValues().getGuiScale(feature);
        float scaleY = FrogMod.INSTANCE.getConfigValues().getGuiScale(feature);
        buttonList.add(new ButtonResize(boxXOne * scaleX, boxYOne * scaleY, feature, ButtonResize.Corner.TOP_LEFT));
        buttonList.add(new ButtonResize(boxXTwo * scaleX, boxYOne * scaleY, feature, ButtonResize.Corner.TOP_RIGHT));
        buttonList.add(new ButtonResize(boxXOne * scaleX, boxYTwo * scaleY, feature, ButtonResize.Corner.BOTTOM_LEFT));
        buttonList.add(new ButtonResize(boxXTwo * scaleX, boxYTwo * scaleY, feature, ButtonResize.Corner.BOTTOM_RIGHT));
    }

    private void recalculateResizeButtons() {
        for (GuiButton button : this.buttonList) {
            if (button instanceof ButtonResize) {
                ButtonResize buttonResize = (ButtonResize) button;
                ButtonResize.Corner corner = buttonResize.getCorner();
                Feature feature = buttonResize.getFeature();
                ButtonLocation buttonLocation = buttonLocations.get(feature);
                if (buttonLocation == null) {
                    continue;
                }

                float scaleX = 1;
                float scaleY = 1;
                float boxXOne = buttonLocation.getBoxXOne() * scaleX;
                float boxXTwo = buttonLocation.getBoxXTwo() * scaleX;
                float boxYOne = buttonLocation.getBoxYOne() * scaleY;
                float boxYTwo = buttonLocation.getBoxYTwo() * scaleY;

                if (corner == ButtonResize.Corner.TOP_LEFT) {
                    buttonResize.x = boxXOne;
                    buttonResize.y = boxYOne;
                } else if (corner == ButtonResize.Corner.TOP_RIGHT) {
                    buttonResize.x = boxXTwo;
                    buttonResize.y = boxYOne;
                } else if (corner == ButtonResize.Corner.BOTTOM_LEFT) {
                    buttonResize.x = boxXOne;
                    buttonResize.y = boxYTwo;
                } else if (corner == ButtonResize.Corner.BOTTOM_RIGHT) {
                    buttonResize.x = boxXTwo;
                    buttonResize.y = boxYTwo;
                }
            }
        }
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Snap[] snaps = checkSnapping();

        onMouseMove(mouseX, mouseY, snaps);


        //recalculateResizeButtons();


        int startColor = new Color(0,0, 0, 64).getRGB();
        int endColor = new Color(0,0, 0, 128).getRGB();
        drawGradientRect(0, 0, width, height, startColor, endColor);

        super.drawScreen(mouseX, mouseY, partialTicks); // Draw buttons.

        if (snaps != null && false) {
            for (Snap snap : snaps) {
                if (snap != null) {
                    float left = snap.getRectangle().get(Edge.LEFT);
                    float top = snap.getRectangle().get(Edge.TOP);
                    float right = snap.getRectangle().get(Edge.RIGHT);
                    float bottom = snap.getRectangle().get(Edge.BOTTOM);

                    if (snap.getWidth() < 0.5) {
                        float averageX = (left+right)/2;
                        left = averageX-0.25F;
                        right = averageX+0.25F;
                    }
                    if (snap.getHeight() < 0.5) {
                        float averageY = (top+bottom)/2;
                        top = averageY-0.25F;
                        bottom = averageY+0.25F;
                    }

                    if ((right-left) == 0.5 || (bottom-top) == 0.5) {
                        Visual.drawRectAbsolute(left, top, right, bottom, 0xFF00FF00);
                    } else {
                        Visual.drawRectAbsolute(left, top, right, bottom, 0xFFFF0000);
                    }
                }
            }
        }

    }

    public Snap[] checkSnapping() {
        if (!enableSnapping) return null;

        if (draggedFeature != null) {
            ButtonLocation thisButton = buttonLocations.get(draggedFeature);
            if (thisButton == null) {
                return null;
            }

            Snap horizontalSnap = null;
            Snap verticalSnap = null;

            for (Map.Entry<Feature, ButtonLocation> buttonLocationEntry : this.buttonLocations.entrySet()) {
                ButtonLocation otherButton = buttonLocationEntry.getValue();

                if (otherButton == thisButton) continue;

                for (Edge otherEdge : Edge.getHorizontalEdges()) {
                    for (Edge thisEdge : Edge.getHorizontalEdges()) {

                        float deltaX = otherEdge.getCoordinate(otherButton) - thisEdge.getCoordinate(thisButton);

                        if (Math.abs(deltaX) <= SNAP_PULL) {
                            float deltaY = Edge.TOP.getCoordinate(otherButton) - Edge.TOP.getCoordinate(thisButton);

                            float topY;
                            float bottomY;
                            if (deltaY > 0) {
                                topY = Edge.BOTTOM.getCoordinate(thisButton);
                                bottomY = Edge.TOP.getCoordinate(otherButton);
                            } else {
                                topY = Edge.BOTTOM.getCoordinate(otherButton);
                                bottomY = Edge.TOP.getCoordinate(thisButton);
                            }

                            float snapX = otherEdge.getCoordinate(otherButton);
                            Snap thisSnap = new Snap(otherEdge.getCoordinate(otherButton), topY, thisEdge.getCoordinate(thisButton), bottomY, thisEdge, otherEdge, snapX);

                            if (thisSnap.getHeight() < SNAPPING_RADIUS) {
                                if (horizontalSnap == null || thisSnap.getHeight() < horizontalSnap.getHeight()) {
                                    if (true) { //dev mode
                                        Visual.drawRectAbsolute(snapX - 0.5, 0, snapX + 0.5, mc.displayHeight, 0xFF0000FF);
                                    }
                                    horizontalSnap = thisSnap;
                                }
                            }
                        }
                    }
                }

                for (Edge otherEdge : Edge.getVerticalEdges()) {
                    for (Edge thisEdge : Edge.getVerticalEdges()) {

                        float deltaY = otherEdge.getCoordinate(otherButton) - thisEdge.getCoordinate(thisButton);

                        if (Math.abs(deltaY) <= SNAP_PULL) {
                            float deltaX = Edge.LEFT.getCoordinate(otherButton) - Edge.LEFT.getCoordinate(thisButton);

                            float leftX;
                            float rightX;
                            if (deltaX > 0) {
                                leftX = Edge.RIGHT.getCoordinate(thisButton);
                                rightX = Edge.LEFT.getCoordinate(otherButton);
                            } else {
                                leftX = Edge.RIGHT.getCoordinate(otherButton);
                                rightX = Edge.LEFT.getCoordinate(thisButton);
                            }
                            float snapY = otherEdge.getCoordinate(otherButton);
                            Snap thisSnap = new Snap(leftX, otherEdge.getCoordinate(otherButton), rightX, thisEdge.getCoordinate(thisButton), thisEdge, otherEdge, snapY);

                            if (thisSnap.getWidth() < SNAPPING_RADIUS) {
                                if (verticalSnap == null || thisSnap.getWidth() < verticalSnap.getWidth()) {
                                    if (true) { //dev mode
                                        Visual.drawRectAbsolute(0, snapY - 0.5, mc.displayWidth, snapY + 0.5, 0xFF0000FF);
                                    }
                                    verticalSnap = thisSnap;
                                }
                            }
                        }
                    }
                }
            }

            return new Snap[] {horizontalSnap, verticalSnap};
        }

        return null;
    }




    protected void onMouseMove(int mouseX, int mouseY, Snap[] snaps) {
        ScaledResolution sr = new ScaledResolution(mc);
        float minecraftScale = sr.getScaleFactor();
        float floatMouseX = Mouse.getX() / minecraftScale;
        float floatMouseY = (mc.displayHeight - Mouse.getY()) / minecraftScale;

        if (draggedFeature != null) {
            ButtonLocation buttonLocation = buttonLocations.get(draggedFeature);
            if (buttonLocation == null) {
                return;
            }

            float x = floatMouseX /*- FrogMod.INSTANCE.getConfigValues().getAnchorPoint(draggedFeature).getX(sr.getScaledWidth())*/;
            float y = floatMouseY /*- FrogMod.INSTANCE.getConfigValues().getAnchorPoint(draggedFeature).getY(sr.getScaledHeight())*/;

            FrogMod.INSTANCE.getConfigValues().setCoords(draggedFeature, x, y);
            /*FrogMod.INSTANCE.getConfigValues().setClosestAnchorPoint(draggedFeature);*/

        }
    }

    @Override
    protected void actionPerformed(GuiButton abstractButton) {
        if (abstractButton instanceof ButtonLocation) {
            ButtonLocation buttonLocation = (ButtonLocation) abstractButton;
            draggedFeature = buttonLocation.getFeature();

            ScaledResolution sr = new ScaledResolution(mc);
            float minecraftScale = sr.getScaleFactor();
            float floatMouseX = Mouse.getX() / minecraftScale;
            float floatMouseY = (mc.displayHeight - Mouse.getY()) / minecraftScale;

            xOffset = floatMouseX - FrogMod.INSTANCE.getConfigValues().getActualX(buttonLocation.getFeature());
            yOffset = floatMouseY - FrogMod.INSTANCE.getConfigValues().getActualY(buttonLocation.getFeature());
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        draggedFeature = null;
        resizing = false;
    }

    @Override
    public void onGuiClosed() {
        FrogMod.INSTANCE.getConfigValues().saveConfig();
    }

    enum Edge {
        LEFT,
                TOP,
                RIGHT,
                BOTTOM,

                HORIZONTAL_MIDDLE,
                VERTICAL_MIDDLE,
        ;

        private static final Set<Edge> verticalEdges = Sets.newHashSet(TOP, BOTTOM, HORIZONTAL_MIDDLE);
        private static final Set<Edge> horizontalEdges = Sets.newHashSet(LEFT, RIGHT, VERTICAL_MIDDLE);

        public float getCoordinate(ButtonLocation button) {
            switch (this) {
                case LEFT:
                    return button.getBoxXOne() * button.getScale();
                case TOP:
                    return button.getBoxYOne() * button.getScale();
                case RIGHT:
                    return button.getBoxXTwo() * button.getScale();
                case BOTTOM:
                    return button.getBoxYTwo() * button.getScale();
                case HORIZONTAL_MIDDLE:
                    return TOP.getCoordinate(button)+(BOTTOM.getCoordinate(button)-TOP.getCoordinate(button))/2F;
                case VERTICAL_MIDDLE:
                    return LEFT.getCoordinate(button)+(RIGHT.getCoordinate(button)-LEFT.getCoordinate(button))/2F;
                default:
                    return 0;
            }
        }
        public static Set<Edge> getVerticalEdges() {return verticalEdges;}
        public static Set<Edge> getHorizontalEdges() {return horizontalEdges;}
    }

    static class Snap {

        private final Edge thisSnapEdge;
        private final Edge otherSnapEdge;
        private final float snapValue;
        private final Map<Edge, Float> rectangle = new EnumMap<>(Edge.class);

        public Snap(float left, float top, float right, float bottom, Edge thisSnapEdge, Edge otherSnapEdge, float snapValue) {
            rectangle.put(Edge.LEFT, left);
            rectangle.put(Edge.TOP, top);
            rectangle.put(Edge.RIGHT, right);
            rectangle.put(Edge.BOTTOM, bottom);

            rectangle.put(Edge.HORIZONTAL_MIDDLE, top + getHeight() / 2);
            rectangle.put(Edge.VERTICAL_MIDDLE, left + getWidth() / 2);

            this.otherSnapEdge = otherSnapEdge;
            this.thisSnapEdge = thisSnapEdge;
            this.snapValue = snapValue;
        }

        public float getHeight() {
            return rectangle.get(Edge.BOTTOM) - rectangle.get(Edge.TOP);
        }

        public float getWidth() {
            return rectangle.get(Edge.RIGHT) - rectangle.get(Edge.LEFT);
        }

        public float getSnapValue() {return snapValue;}
        public Edge getThisSnapEdge() {return thisSnapEdge;}

        public Map<Edge, Float> getRectangle() {return rectangle;}
    }
}
