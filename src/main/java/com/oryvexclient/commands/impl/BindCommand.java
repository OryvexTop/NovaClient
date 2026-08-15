package com.oryvexclient.commands.impl;

import com.oryvexclient.OryvexClient;
import com.oryvexclient.commands.Command;
import com.oryvexclient.modules.Module;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.Map;

public class BindCommand extends Command {

    private static final Map<String, Integer> KEY_ALIASES = new HashMap<>();

    static {
        for (char c = 'A'; c <= 'Z'; c++) {
            String name = String.valueOf(c);
            int code = Keyboard.getKeyIndex(name);
            if (code != Keyboard.KEY_NONE) {
                KEY_ALIASES.put(name.toLowerCase(), code);
            }
        }
        for (char c = '0'; c <= '9'; c++) {
            String name = String.valueOf(c);
            int code = Keyboard.getKeyIndex(name);
            if (code != Keyboard.KEY_NONE) {
                KEY_ALIASES.put(name, code);
            }
        }
        for (int i = 1; i <= 12; i++) {
            String name = "F" + i;
            int code = Keyboard.getKeyIndex(name);
            if (code != Keyboard.KEY_NONE) {
                KEY_ALIASES.put(name.toLowerCase(), code);
            }
        }

        addKey("rshift", Keyboard.KEY_RSHIFT);
        addKey("lshift", Keyboard.KEY_LSHIFT);
        addKey("shift", Keyboard.KEY_LSHIFT);
        addKey("rctrl", Keyboard.KEY_RCONTROL);
        addKey("lctrl", Keyboard.KEY_LCONTROL);
        addKey("ctrl", Keyboard.KEY_LCONTROL);
        addKey("ralt", Keyboard.KEY_RMENU);
        addKey("lalt", Keyboard.KEY_LMENU);
        addKey("alt", Keyboard.KEY_LMENU);
        addKey("space", Keyboard.KEY_SPACE);
        addKey("tab", Keyboard.KEY_TAB);
        addKey("enter", Keyboard.KEY_RETURN);
        addKey("return", Keyboard.KEY_RETURN);
        addKey("backspace", Keyboard.KEY_BACK);
        addKey("delete", Keyboard.KEY_DELETE);
        addKey("insert", Keyboard.KEY_INSERT);
        addKey("home", Keyboard.KEY_HOME);
        addKey("end", Keyboard.KEY_END);
        addKey("pgup", Keyboard.KEY_PRIOR);
        addKey("pgdn", Keyboard.KEY_NEXT);
        addKey("up", Keyboard.KEY_UP);
        addKey("down", Keyboard.KEY_DOWN);
        addKey("left", Keyboard.KEY_LEFT);
        addKey("right", Keyboard.KEY_RIGHT);
        addKey("none", Keyboard.KEY_NONE);
    }

    private static void addKey(String alias, int keyCode) {
        if (keyCode != Keyboard.KEY_NONE) {
            KEY_ALIASES.put(alias, keyCode);
        }
    }

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
