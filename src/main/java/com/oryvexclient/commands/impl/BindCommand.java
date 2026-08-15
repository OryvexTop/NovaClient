package com.oryvexclient.commands.impl;

import OryvexClient.OryvexClient;
import com.oryvexclient.commands.impl.commands.Command;
import com.oryvexclient.commands.impl.modules.Module;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.Map;

public class BindCommand extends Command {

    private static final Map<String, Integer> KEY_ALIASES = new HashMap<>();
    static {
        // Letters
        for (int i = Keyboard.KEY_A; i <= Keyboard.KEY_Z; i++) {
            KEY_ALIASES.put(Keyboard.getKeyName(i).toLowerCase(), i);
        }
        // Numbers
        for (int i = Keyboard.KEY_0; i <= Keyboard.KEY_9; i++) {
            KEY_ALIASES.put(Keyboard.getKeyName(i).toLowerCase(), i);
        }
        // F1-F12
        for (int i = Keyboard.KEY_F1; i <= Keyboard.KEY_F12; i++) {
            KEY_ALIASES.put(Keyboard.getKeyName(i).toLowerCase(), i);
        }
        // Special keys
        KEY_ALIASES.put("rshift", Keyboard.KEY_RSHIFT);
        KEY_ALIASES.put("lshift", Keyboard.KEY_LSHIFT);
        KEY_ALIASES.put("shift", Keyboard.KEY_LSHIFT);
        KEY_ALIASES.put("rctrl", Keyboard.KEY_RCONTROL);
        KEY_ALIASES.put("lctrl", Keyboard.KEY_LCONTROL);
        KEY_ALIASES.put("ctrl", Keyboard.KEY_LCONTROL);
        KEY_ALIASES.put("ralt", Keyboard.KEY_RMENU);
        KEY_ALIASES.put("lalt", Keyboard.KEY_LMENU);
        KEY_ALIASES.put("alt", Keyboard.KEY_LMENU);
        KEY_ALIASES.put("space", Keyboard.KEY_SPACE);
        KEY_ALIASES.put("tab", Keyboard.KEY_TAB);
        KEY_ALIASES.put("enter", Keyboard.KEY_RETURN);
        KEY_ALIASES.put("return", Keyboard.KEY_RETURN);
        KEY_ALIASES.put("backspace", Keyboard.KEY_BACK);
        KEY_ALIASES.put("delete", Keyboard.KEY_DELETE);
        KEY_ALIASES.put("insert", Keyboard.KEY_INSERT);
        KEY_ALIASES.put("home", Keyboard.KEY_HOME);
        KEY_ALIASES.put("end", Keyboard.KEY_END);
        KEY_ALIASES.put("pgup", Keyboard.KEY_PRIOR);
        KEY_ALIASES.put("pgdn", Keyboard.KEY_NEXT);
        KEY_ALIASES.put("up", Keyboard.KEY_UP);
        KEY_ALIASES.put("down", Keyboard.KEY_DOWN);
        KEY_ALIASES.put("left", Keyboard.KEY_LEFT);
        KEY_ALIASES.put("right", Keyboard.KEY_RIGHT);
        KEY_ALIASES.put("none", Keyboard.KEY_NONE);
    };

    public BindCommand() {
        super("bind", "Bind a module to a key");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 3) {
            OryvexClient.getInstance().getCommandManager().sendMessage("Usage: .bind [module] [key]");
            return;
        }

        String moduleName = args[1];
        String keyName = args[2].toLowerCase();

        Module module = OryvexClient.getInstance().getModuleManager().getModuleByName(moduleName);
        if (module == null) {
            OryvexClient.getInstance().getCommandManager().sendMessage("Module '" + moduleName + "' not found.");
            return;
        }

        Integer keyCode = KEY_ALIASES.get(keyName);
        if (keyCode == null) {
            OryvexClient.getInstance().getCommandManager().sendMessage("Invalid key: " + keyName);
            return;
        }

        module.setKeybind(keyCode);
        OryvexClient.getInstance().getKeybindManager().registerBind(module);
        OryvexClient.getInstance().getConfigManager().saveConfig();
        OryvexClient.getInstance().getCommandManager().sendMessage(
            "Bound " + module.getName() + " to " + Keyboard.getKeyName(keyCode)
        );
    }
}
