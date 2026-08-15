package com.oryvexclient.gui;

import com.oryvexclient.OryvexClient;
import com.oryvexclient.commands.Command;
import com.oryvexclient.modules.Module;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class OryvexChatGUI extends GuiChat {
    private List<String> suggestions = new ArrayList<>();

    @Override
    public void initGui() {
        super.initGui();
        suggestions.clear();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 28) { // Enter
            String text = getChatText();
            if (text.startsWith(".")) {
                OryvexClient.getInstance().getCommandManager().executeCommand(text);
                this.mc.displayGuiScreen(null);
                return;
            }
        }
        super.keyTyped(typedChar, keyCode);
        updateSuggestions();
    }

    private String getChatText() {
        try {
            Field field = GuiChat.class.getDeclaredField("inputField");
            field.setAccessible(true);
            return ((GuiTextField) field.get(this)).getText();
        } catch (Exception e) { return ""; }
    }

    private void updateSuggestions() {
        String text = getChatText();
        suggestions.clear();
        if (!text.startsWith(".")) return;
        String[] parts = text.substring(1).split(" ");
        if (parts.length == 0) return;
        String current = parts[0].toLowerCase();

        for (Command cmd : OryvexClient.getInstance().getCommandManager().getCommands()) {
            if (cmd.getName().startsWith(current)) suggestions.add("." + cmd.getName());
        }

        if (parts.length >= 2 && parts[0].equalsIgnoreCase("bind")) {
            String modulePart = parts[1].toLowerCase();
            for (Module m : OryvexClient.getInstance().getModuleManager().getModules()) {
                if (m.getName().toLowerCase().startsWith(modulePart)) suggestions.add("." + parts[0] + " " + m.getName());
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (!suggestions.isEmpty()) {
            int y = this.height - 30 - (suggestions.size() * 12);
            for (String s : suggestions) {
                drawRect(2, y - 2, 10 + mc.fontRendererObj.getStringWidth(s), y + 10, 0xDD000000);
                drawString(mc.fontRendererObj, s, 5, y, 0xFF89B4FA);
                y += 12;
            }
        }
    }
}
