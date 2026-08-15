
package com.oryvexclient.modules.impl;
import com.oryvexclient.modules.Category;
import com.oryvexclient.modules.Module;
import org.lwjgl.input.Keyboard;

public class Velocity extends Module {
    public Velocity() { super("Velocity", "Reduces knockback.", Keyboard.KEY_NONE, Category.COMBAT); }
    @Override public void onUpdate() {
        if (mc.thePlayer.hurtTime > 0) { mc.thePlayer.motionX *= 0.6; mc.thePlayer.motionZ *= 0.6; }
    }
}
