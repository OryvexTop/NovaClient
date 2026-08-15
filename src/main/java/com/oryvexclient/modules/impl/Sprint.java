
package com.oryvexclient.modules.impl;
import com.oryvexclient.modules.Category;
import com.oryvexclient.modules.Module;
import org.lwjgl.input.Keyboard;

public class Sprint extends Module {
    public Sprint() { super("Sprint", "Auto sprints.", Keyboard.KEY_NONE, Category.MOVEMENT); }
    @Override public void onUpdate() {
        if (mc.thePlayer.moveForward > 0 && !mc.thePlayer.isSneaking() && !mc.thePlayer.isCollidedHorizontally) mc.thePlayer.setSprinting(true);
    }
}
