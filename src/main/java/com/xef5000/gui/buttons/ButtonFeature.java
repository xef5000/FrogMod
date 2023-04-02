package com.xef5000.gui.buttons;

import com.xef5000.Feature;
import net.minecraft.client.gui.GuiButton;


// TAKEN FROM SBA: https://github.com/BiscuitDevelopment/SkyblockAddons/blob/d5f53c0a4e486c5094aefaee683aacd79c1db756/src/main/java/codes/biscuit/skyblockaddons/gui/buttons/ButtonFeature.java
public class ButtonFeature extends GuiButton {

    // The feature that this button moves.
    public Feature feature;
    /**
     * Create a button that is assigned a feature (to toggle/change color etc.).
     */
    ButtonFeature(int buttonId, int x, int y, String buttonText, Feature feature) {
        super(buttonId, x, y, buttonText);
        this.feature = feature;
    }

    public Feature getFeature() {
        return feature;
    }
}
