package com.oryvexclient.commands;

import com.oryvexclient.commands.commands.impl.BindCommand;
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
    }

    public void executeCommand(String input) {
        if (!input.startsWith(".")) return;
        String[] args = input.substring(1).split(" ");
        if (args.length == 0) return;

        for (Command cmd : commands) {
            if (cmd.getName().equalsIgnoreCase(args[0])) {
                cmd.execute(args);
                return;
            }
        }
        sendMessage("Unknown command. Use .bind [module] [key]");
    }

    public void sendMessage(String message) {
        if (mc.thePlayer != null) {
            mc.thePlayer.addChatComponentMessage(new ChatComponentText(
                EnumChatFormatting.BLUE + "[Oryvex] " + EnumChatFormatting.WHITE + message
            ));
        }
    }
}
