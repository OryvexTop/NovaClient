package com.oryvexclient.commands.impl;

import com.oryvexclient.OryvexClient;
import com.oryvexclient.commands.Command;
import com.oryvexclient.modules.Module;

public class ToggleCommand extends Command {
    public ToggleCommand() { super("toggle", "Toggles a module."); }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            OryvexClient.getInstance().getCommandManager().sendMessage("Usage: .toggle <module>");
            return;
        }
        Module m = OryvexClient.getInstance().getModuleManager().getModuleByName(args[1]);
        if (m == null) {
            OryvexClient.getInstance().getCommandManager().sendMessage("Module not found.");
            return;
        }
        m.toggle();
        OryvexClient.getInstance().getConfigManager().saveConfig();
        OryvexClient.getInstance().getCommandManager().sendMessage(m.getName() + " is now " + (m.isToggled() ? "enabled" : "disabled"));
    }
}
