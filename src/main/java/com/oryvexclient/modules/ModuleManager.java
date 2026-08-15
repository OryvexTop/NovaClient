package com.oryvexclient.modules;

import com.oryvexclient.modules.impl.KillAura;
import com.oryvexclient.modules.impl.Scaffold;
import com.oryvexclient.modules.impl.Sprint;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    private final List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        register(new KillAura());
        register(new Scaffold());
        register(new Sprint());
    }

    private void register(Module module) {
        modules.add(module);
    }

    public List<Module> getModules() {
        return modules;
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> T getModule(Class<T> clazz) {
        for (Module module : modules) {
            if (module.getClass().equals(clazz)) return (T) module;
        }
        return null;
    }

    public Module getModuleByName(String name) {
        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(name)) return module;
        }
        return null;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        for (Module module : modules) {
            if (module.isToggled()) {
                module.onUpdate();
            }
        }
    }

    public void toggleModule(String name) {
        Module module = getModuleByName(name);
        if (module != null) module.toggle();
    }
}
