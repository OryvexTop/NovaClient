
package com.oryvexclient.modules.impl;
import com.oryvexclient.modules.Category;
import com.oryvexclient.modules.Module;
import com.oryvexclient.utils.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class Tracers extends Module {
    public Tracers() { super("Tracers", "Draws lines to players.", Keyboard.KEY_NONE, Category.RENDER); }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityPlayer && entity != mc.thePlayer) {
                double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * e.partialTicks - mc.getRenderManager().viewerPosX;
                double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * e.partialTicks - mc.getRenderManager().viewerPosY;
                double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * e.partialTicks - mc.getRenderManager().viewerPosZ;
                RenderUtils.drawLine3D(0, mc.thePlayer.getEyeHeight(), 0, x, y, z, 1.5f, 0xFFFF0000);
            }
        }
    }
}
