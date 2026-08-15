package com.oryvexclient.gui;

import com.oryvexclient.OryvexClient;
import com.oryvexclient.commands.Command;
import com.oryvexclient.modules.Module;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ChatComponentText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OryvexChatGUI extends GuiChat {

    private GuiTextField inputField;
    private List<String> suggestions = new ArrayList<>();
    private String lastText = "";

    @Override
    public void initGui() {
        super.initGui();
        this.inputField = (GuiTextField) this.getClass().getSuperclass().getDeclaredField("inputField").get(this) // reflection hack
            .orElseThrow?; // not needed, we'll use reflection later
    }

    // We'll override keyTyped to handle '.' and Enter
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
            java.lang.reflect.Field f = GuiChat.class.getDeclaredField("inputField");
            f.setAccessible(true);
            GuiTextField field = (GuiTextField) f.get(this);
            return field.getText();
        } catch (Exception e) {
            return "";
        }
    }

    private void updateSuggestions() {
        String text = getChatText();
        if (!text.startsWith(".")) {
            suggestions.clear();
            return;
        }
        String[] parts = text.substring(1).split(" ");
        if (parts.length == 0) return;
        String current = parts[0].toLowerCase();
        suggestions.clear();

        // Commands
        for (Command cmd : OryvexClient.getInstance().getCommandManager().getCommands()) {
            if (cmd.getName().startsWith(current)) {
                suggestions.add("." + cmd.getName());
            }
        }
        // If first part is a command, suggest modules for second arg
        if (parts.length >= 2) {
            String cmdName = parts[0].toLowerCase();
            if (cmdName.equals("bind")) {
                String modulePart = parts[1].toLowerCase();
                for (Module m : OryvexClient.getInstance().getModuleManager().getModules()) {
                    if (m.getName().toLowerCase().startsWith(modulePart)) {
                        suggestions.add("." + cmdName + " " + m.getName());
                    }
                }
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (!suggestions.isEmpty()) {
            int y = this.height - 25 - (suggestions.size() * 12);
            for (String s : suggestions) {
                drawString(mc.fontRendererObj, s, 5, y, 0x00AAFF);
                y += 12;
            }
        }
    }
}
