
package com.oryvexclient.modules.impl;
import com.oryvexclient.modules.Category;
import com.oryvexclient.modules.Module;
import org.lwjgl.input.Keyboard;

public class Speed extends Module {
    public Speed() { super("Speed", "Moves faster.", Keyboard.KEY_NONE, Category.MOVEMENT); }
    @Override public void onUpdate() {
        if (mc.thePlayer.onGround && mc.thePlayer.moveForward > 0) { mc.thePlayer.motionX *= 1.15; mc.thePlayer.motionZ *= 1.15; }
    }
}
