
package com.oryvexclient.modules.impl;
import com.oryvexclient.modules.Category;
import com.oryvexclient.modules.Module;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.input.Keyboard;

public class Nuker extends Module {
    public Nuker() { super("Nuker", "Breaks surrounding blocks.", Keyboard.KEY_NONE, Category.WORLD); }
    @Override public void onUpdate() {
        for (int x = -3; x <= 3; x++) {
            for (int y = -3; y <= 3; y++) {
                for (int z = -3; z <= 3; z++) {
                    BlockPos pos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z);
                    if (mc.theWorld.getBlockState(pos).getBlock().getMaterial() != Material.air) {
                        mc.playerController.onPlayerDamageBlock(pos, EnumFacing.UP);
                        mc.thePlayer.swingItem();
                        return;
                    }
                }
            }
        }
    }
}
