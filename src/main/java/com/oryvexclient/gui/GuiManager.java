package com.oryvexclient.gui;

import com.oryvexclient.gui.clickgui.OryvexClickGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class GuiManager {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final OryvexClickGUI clickGUI = new OryvexClickGUI();

    public GuiManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.gui instanceof GuiMainMenu) {
            event.gui = new OryvexMainMenu();
        }
        if (event.gui instanceof GuiChat && !(event.gui instanceof OryvexChatGUI)) {
            event.gui = new OryvexChatGUI();
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (mc.theWorld == null || mc.thePlayer == null) return;

        if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            if (mc.currentScreen == null) {
                mc.displayGuiScreen(clickGUI);
            }
        }
    }

    public void toggleGUI() {
        if (mc.currentScreen == clickGUI) mc.displayGuiScreen(null);
        else mc.displayGuiScreen(clickGUI);
    }

    public OryvexClickGUI getClickGUI() { return clickGUI; }
}
