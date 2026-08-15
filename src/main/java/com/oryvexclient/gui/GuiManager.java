package com.oryvexclient.gui;

import com.oryvexclient.gui.clickgui.OryvexClickGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class GuiManager {

    private boolean guiOpen = false;
    private Minecraft mc = Minecraft.getMinecraft();
    private OryvexClickGUI clickGUI;

    public GuiManager() {
        MinecraftForge.EVENT_BUS.register(this);
        clickGUI = new OryvexClickGUI();
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        // Replace main menu with OryvexMainMenu
        if (event.gui instanceof GuiMainMenu) {
            event.gui = new OryvexMainMenu();
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (mc.theWorld == null || mc.thePlayer == null) return;

        // Toggle ClickGUI with Right Shift
        if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            if (!guiOpen) {
                openGUI();
                guiOpen = true;
            }
        } else {
            if (guiOpen && mc.currentScreen != clickGUI) {
                guiOpen = false;
            }
        }

        // Toggle ClickGUI via command or other means
    }

    public void openGUI() {
        mc.displayGuiScreen(clickGUI);
    }

    public void toggleGUI() {
        if (mc.currentScreen == clickGUI) {
            mc.displayGuiScreen(null);
        } else {
            mc.displayGuiScreen(clickGUI);
        }
    }

    public OryvexClickGUI getClickGUI() {
        return clickGUI;
    }
}
