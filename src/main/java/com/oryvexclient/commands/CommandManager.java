package com.oryvexclient.commands;

import com.oryvexclient.commands.impl.BindCommand;
import com.oryvexclient.commands.impl.ToggleCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private final List<Command> commands = new ArrayList<>();
    private final Minecraft mc = Minecraft.getMinecraft();

    public CommandManager() {
        commands.add(new BindCommand());
        commands.add(new ToggleCommand());
    }

    public List<Command> getCommands() { return commands; }

    public void executeCommand(String input) {
        if (!input.startsWith(".")) return;
        String[] args = input.substring(1).split(" ");
        if (args.length == 0 || args[0].isEmpty()) return;

        for (Command cmd : commands) {
            if (cmd.getName().equalsIgnoreCase(args[0])) {
                cmd.execute(args);
                return;
            }
        }
        sendMessage("Unknown command. Type .help for a list of commands.");
    }

    public void sendMessage(String message) {
        if (mc.thePlayer != null) {
            mc.thePlayer.addChatComponentMessage(new ChatComponentText(
                EnumChatFormatting.BLUE + "[Oryvex] " + EnumChatFormatting.GRAY + message
            ));
        }
    }
}
