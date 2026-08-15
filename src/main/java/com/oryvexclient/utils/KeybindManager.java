package com.oryvexclient.utils;

import com.oryvexclient.OryvexClient;
import com.oryvexclient.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class KeybindManager {
    private final Minecraft mc = Minecraft.getMinecraft();
    private boolean[] pressed = new boolean[256];

    public void updateBinds() {}

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (mc.currentScreen != null) return;

        for (Module module : OryvexClient.getInstance().getModuleManager().getModules()) {
            int key = module.getKeybind();
            if (key == Keyboard.KEY_NONE) continue;
            if (key >= 256) continue;

            boolean isDown = Keyboard.isKeyDown(key);
            if (isDown && !pressed[key]) {
                module.toggle();
                OryvexClient.getInstance().getConfigManager().saveConfig();
            }
            pressed[key] = isDown;
        }
    }
}
