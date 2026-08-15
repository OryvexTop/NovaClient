
package com.oryvexclient.modules.impl;
import com.oryvexclient.modules.Category;
import com.oryvexclient.modules.Module;
import com.oryvexclient.utils.MovementUtils;
import com.oryvexclient.utils.NumberSetting;
import org.lwjgl.input.Keyboard;

public class Speed extends Module {
    private final NumberSetting speed = new NumberSetting("Speed", 0.2, 0.1, 1.0, 0.05);
    public Speed() { super("Speed", "Moves faster.", Keyboard.KEY_NONE, Category.MOVEMENT); addSetting(speed); }
    @Override public void onUpdate() {
        if (mc.thePlayer.onGround && (mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0)) {
            MovementUtils.strafe((float)speed.getValue());
        }
    }
}
