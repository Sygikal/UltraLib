package dev.sygii.ultralib;

import net.minecraft.client.option.KeyBinding;

public class LatchedKeyBinding {
    public final KeyBinding key;
    public boolean wasPressed = false;

    public LatchedKeyBinding(KeyBinding key) {
        this.key = key;
    }

    public void handle(Runnable runnable) {
        boolean isPressed = key.isPressed();

        if (isPressed && !wasPressed) {
            runnable.run();
        }

        wasPressed = isPressed;
    }
}
