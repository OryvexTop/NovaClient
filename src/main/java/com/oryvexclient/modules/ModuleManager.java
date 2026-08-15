package com.oryvexclient.modules;

import com.oryvexclient.modules.impl.KillAura;
import com.oryvexclient.modules.impl.Scaffold;
import com.oryvexclient.modules.impl.Sprint;
import net.minecraftforge.common.MinecraftForge;
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
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void register(Module module) { modules.add(module); }
    public List<Module> getModules() { return modules; }

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
            if (module.isToggled()) module.onUpdate();
        }
    }
}
