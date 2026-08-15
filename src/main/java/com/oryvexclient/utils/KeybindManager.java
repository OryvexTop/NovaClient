package com.oryvexclient.utils;

import com.oryvexclient.utils.OryvexClient;
import com.oryvexclient.utils.modules.Module;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class KeybindManager {

    private final List<Module> binds = new ArrayList<>();
    private long lastPressTime = 0;

    public KeybindManager() {
        // Binds are registered automatically when a module is created
        for (Module module : OryvexClient.getInstance().getModuleManager().getModules()) {
            if (module.getKeybind() != Keyboard.KEY_NONE) binds.add(module);
        }
    }

    public void registerBind(Module module) {
        if (!binds.contains(module) && module.getKeybind() != Keyboard.KEY_NONE) {
            binds.add(module);
        } else if (module.getKeybind() == Keyboard.KEY_NONE) {
            binds.remove(module);
        }
    }

    public void checkKeybinds() {
        if (System.currentTimeMillis() - lastPressTime < 100) return;
        for (Module module : binds) {
            if (Keyboard.isKeyDown(module.getKeybind())) {
                module.toggle();
                lastPressTime = System.currentTimeMillis();
                OryvexClient.getInstance().getConfigManager().saveConfig();
            }
        }
    }
}
