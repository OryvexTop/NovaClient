package com.oryvexclient;

import com.oryvexclient.commands.CommandManager;
import com.oryvexclient.gui.GuiManager;
import com.oryvexclient.modules.ModuleManager;
import com.oryvexclient.utils.ConfigManager;
import com.oryvexclient.utils.KeybindManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.opengl.Display;

@Mod(modid = "oryvexclient", name = "OryvexClient", version = "1.0.0", clientSideOnly = true)
public class OryvexClient {
    public static final String MODID = "oryvexclient";
    public static final String NAME = "OryvexClient";
    public static final String VERSION = "1.0.0";

    private static OryvexClient instance;
    private ModuleManager moduleManager;
    private CommandManager commandManager;
    private ConfigManager configManager;
    private KeybindManager keybindManager;
    private GuiManager guiManager;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        instance = this;
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        keybindManager = new KeybindManager();
        guiManager = new GuiManager();
        configManager = new ConfigManager();

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(guiManager);
        MinecraftForge.EVENT_BUS.register(keybindManager);
        
        Display.setTitle("OryvexClient v1.0");
        System.out.println("[OryvexClient] PreInit complete.");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        configManager.loadConfig();
    }

    public static OryvexClient getInstance() { return instance; }
    public ModuleManager getModuleManager() { return moduleManager; }
    public CommandManager getCommandManager() { return commandManager; }
    public ConfigManager getConfigManager() { return configManager; }
    public KeybindManager getKeybindManager() { return keybindManager; }
    public GuiManager getGuiManager() { return guiManager; }
}
