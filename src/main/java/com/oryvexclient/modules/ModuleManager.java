
package com.oryvexclient.modules;
import com.oryvexclient.modules.impl.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();
    public ModuleManager() {
        register(new KillAura()); register(new AutoClicker()); register(new Criticals()); register(new Velocity()); register(new AntiBot());
        register(new Sprint()); register(new Fly()); register(new Speed()); register(new NoFall()); register(new Jesus());
        register(new Scaffold()); register(new FastBreak()); register(new Nuker());
        register(new Fullbright()); register(new Tracers()); register(new HUD()); register(new StorageESP());
        register(new FastPlace()); register(new ChestStealer()); register(new AutoRespawn());
        MinecraftForge.EVENT_BUS.register(this);
    }
    private void register(Module module) { modules.add(module); }
    public List<Module> getModules() { return modules; }
    public Module getModuleByName(String name) {
        for (Module module : modules) if (module.getName().equalsIgnoreCase(name)) return module;
        return null;
    }
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        for (Module module : modules) if (module.isToggled()) module.onUpdate();
    }
}
