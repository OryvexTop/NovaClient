
package com.oryvexclient.modules.impl;
import com.oryvexclient.modules.Category;
import com.oryvexclient.modules.Module;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class AutoRespawn extends Module {
    public AutoRespawn() { super("AutoRespawn", "Respawns automatically.", Keyboard.KEY_NONE, Category.OTHER); }
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (mc.currentScreen instanceof GuiGameOver) { mc.thePlayer.respawnPlayer(); mc.displayGuiScreen(null); }
    }
}
