
package com.oryvexclient.commands.impl;
import com.oryvexclient.OryvexClient;
import com.oryvexclient.commands.Command;
import com.oryvexclient.modules.Module;
import org.lwjgl.input.Keyboard;

public class BindCommand extends Command {
    public BindCommand() { super("bind", "Bind a module to a key"); }
    @Override
    public void execute(String[] args) {
        if (args.length < 3) { OryvexClient.getInstance().getCommandManager().sendMessage("Usage: .bind <module> <key>"); return; }
        String moduleName = args[1];
        String keyName = args[2].toUpperCase();
        Module module = OryvexClient.getInstance().getModuleManager().getModuleByName(moduleName);
        if (module == null) { OryvexClient.getInstance().getCommandManager().sendMessage("Module '" + moduleName + "' not found."); return; }

        int keyCode = Keyboard.KEY_NONE;
        if (keyName.equals("NONE")) keyCode = Keyboard.KEY_NONE;
        else if (keyName.equals("RSHIFT")) keyCode = Keyboard.KEY_RSHIFT;
        else if (keyName.equals("LSHIFT") || keyName.equals("SHIFT")) keyCode = Keyboard.KEY_LSHIFT;
        else if (keyName.equals("RCONTROL") || keyName.equals("RCTRL")) keyCode = Keyboard.KEY_RCONTROL;
        else if (keyName.equals("LCONTROL") || keyName.equals("LCTRL") || keyName.equals("CTRL")) keyCode = Keyboard.KEY_LCONTROL;
        else if (keyName.equals("RALT")) keyCode = Keyboard.KEY_RMENU;
        else if (keyName.equals("LALT") || keyName.equals("ALT")) keyCode = Keyboard.KEY_LMENU;
        else if (keyName.equals("CAPITAL") || keyName.equals("CAPSLOCK")) keyCode = Keyboard.KEY_CAPITAL;
        else keyCode = Keyboard.getKeyIndex(keyName);

        if (keyCode == Keyboard.KEY_NONE && !keyName.equals("NONE")) {
            OryvexClient.getInstance().getCommandManager().sendMessage("Invalid key: " + keyName); return;
        }
        module.setKeybind(keyCode);
        OryvexClient.getInstance().getKeybindManager().updateBinds();
        OryvexClient.getInstance().getConfigManager().saveConfig();
        OryvexClient.getInstance().getCommandManager().sendMessage("Bound " + module.getName() + " to " + (keyCode == Keyboard.KEY_NONE ? "NONE" : Keyboard.getKeyName(keyCode)));
    }
}
