
package com.oryvexclient.modules.impl;
import com.oryvexclient.modules.Category;
import com.oryvexclient.modules.Module;
import com.oryvexclient.utils.RenderUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class StorageESP extends Module {
    public StorageESP() { super("StorageESP", "Highlights chests.", Keyboard.KEY_NONE, Category.RENDER); }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        for (TileEntity te : mc.theWorld.loadedTileEntityList) {
            if (te instanceof TileEntityChest) {
                double x = te.getPos().getX() - mc.getRenderManager().viewerPosX;
                double y = te.getPos().getY() - mc.getRenderManager().viewerPosY;
                double z = te.getPos().getZ() - mc.getRenderManager().viewerPosZ;
                RenderUtils.drawLine3D(0, mc.thePlayer.getEyeHeight(), 0, x + 0.5, y + 0.5, z + 0.5, 2.0f, 0xFFFFFF00);
            }
        }
    }
}
