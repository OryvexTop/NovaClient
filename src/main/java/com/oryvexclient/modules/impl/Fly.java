
package com.oryvexclient.modules.impl;
import com.oryvexclient.modules.Category;
import com.oryvexclient.modules.Module;
import com.oryvexclient.utils.NumberSetting;
import org.lwjgl.input.Keyboard;

public class Fly extends Module {
    private final NumberSetting speed = new NumberSetting("Speed", 0.5, 0.1, 2.0, 0.1);
    public Fly() { super("Fly", "Survival fly.", Keyboard.KEY_NONE, Category.MOVEMENT); addSetting(speed); }
    @Override public void onUpdate() {
        mc.thePlayer.motionY = 0;
        if (mc.gameSettings.keyBindJump.isKeyDown()) mc.thePlayer.motionY = speed.getValue();
        if (mc.gameSettings.keyBindSneak.isKeyDown()) mc.thePlayer.motionY = -speed.getValue();
    }
}
