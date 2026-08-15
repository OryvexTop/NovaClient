
package com.oryvexclient.modules.impl;
import com.oryvexclient.modules.Category;
import com.oryvexclient.modules.Module;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

public class Jesus extends Module {
    public Jesus() { super("Jesus", "Walks on water.", Keyboard.KEY_NONE, Category.MOVEMENT); }
    @Override public void onUpdate() {
        if (mc.thePlayer.isInWater()) mc.thePlayer.motionY = 0.1;
        BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.1, mc.thePlayer.posZ);
        if (mc.theWorld.getBlockState(pos).getBlock().getMaterial() == Material.water) { mc.thePlayer.motionY = 0; mc.thePlayer.onGround = true; }
    }
}
