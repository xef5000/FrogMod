package com.xef5000.utils.objects;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class Keybind {
    public KeyBinding getKeyBinding() {
        return keyBinding;
    }

    public String getName() {
        return name;
    }

    public int getDefaultKeyCode() {
        return defaultKeyCode;
    }

    public boolean isRegistered() {
        return registered;
    }

    private final KeyBinding keyBinding;
    private final String name;
    private final int defaultKeyCode;
    private boolean registered = false;
    private boolean isFirstRegistration = true;
    private int previousKeyCode = 999;

    public Keybind(String name, int defaultKeyCode) {
        this.name = name;
        this.defaultKeyCode = defaultKeyCode;
        keyBinding = new KeyBinding(name, this.getDefaultKeyCode(), "FrogMod");
    }

    public boolean isKeyDown() {
        if (registered) {
            return keyBinding.isKeyDown();
        }
        else {
            return false;
        }
    }
    public boolean isPressed() {
        if (registered) {
            return keyBinding.isPressed();
        }
        else {
            return false;
        }
    }

    public void register() {
        if (registered) {
            System.out.println("Tried to register a key binding with the name \"" + name + "\" which is already registered.");
            return;
        }

        ClientRegistry.registerKeyBinding(keyBinding);

        if (isFirstRegistration) {
            isFirstRegistration = false;
        } else if (previousKeyCode < Keyboard.KEYBOARD_SIZE) {
            keyBinding.setKeyCode(defaultKeyCode);
            KeyBinding.resetKeyBindingArrayAndHash();
        }
        registered = true;
    }

}
