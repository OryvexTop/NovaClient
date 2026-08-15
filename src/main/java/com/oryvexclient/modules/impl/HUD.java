
package com.oryvexclient.modules.impl;
import com.oryvexclient.OryvexClient;
import com.oryvexclient.modules.Category;
import com.oryvexclient.modules.Module;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class HUD extends Module {
    public HUD() { super("HUD", "Displays ArrayList.", Keyboard.KEY_NONE, Category.RENDER); }
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text e) {
        ScaledResolution sr = new ScaledResolution(mc);
        int y = 2;
        for (Module m : OryvexClient.getInstance().getModuleManager().getModules()) {
            if (m.isToggled() && m != this) {
                mc.fontRendererObj.drawStringWithShadow(m.getName(), sr.getScaledWidth() - mc.fontRendererObj.getStringWidth(m.getName()) - 2, y, 0xFF89B4FA);
                y += 10;
            }
        }
    }
}
