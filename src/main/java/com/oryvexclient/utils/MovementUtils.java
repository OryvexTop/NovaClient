
package com.oryvexclient.utils;
import net.minecraft.client.Minecraft;
public class MovementUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static void strafe(float speed) {
        if (mc.thePlayer.moveForward == 0 && mc.thePlayer.moveStrafing == 0) return;
        float yaw = mc.thePlayer.rotationYaw;
        float forward = mc.thePlayer.moveForward;
        float strafe = mc.thePlayer.moveStrafing;
        if (forward != 0) {
            if (strafe > 0) yaw -= (forward > 0 ? 45 : -45);
            else if (strafe < 0) yaw += (forward > 0 ? 45 : -45);
            strafe = 0;
            if (forward > 0) forward = 1;
            else if (forward < 0) forward = -1;
        }
        double rad = Math.toRadians(yaw);
        mc.thePlayer.motionX = forward * speed * -Math.sin(rad) + strafe * speed * Math.cos(rad);
        mc.thePlayer.motionZ = forward * speed * Math.cos(rad) + strafe * speed * -Math.sin(rad);
    }
}
