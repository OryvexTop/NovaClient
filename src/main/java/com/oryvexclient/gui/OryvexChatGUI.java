
package com.oryvexclient.gui;
import com.oryvexclient.OryvexClient;
import com.oryvexclient.commands.Command;
import com.oryvexclient.modules.Module;
import net.minecraft.client.gui.GuiChat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OryvexChatGUI extends GuiChat {
    private List<String> suggestions = new ArrayList<>();
    @Override public void initGui() { super.initGui(); suggestions.clear(); }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 28) { // Enter
            String text = this.inputField.getText();
            if (text.startsWith(".")) {
                OryvexClient.getInstance().getCommandManager().executeCommand(text);
                this.mc.displayGuiScreen(null);
                return;
            }
        }
        super.keyTyped(typedChar, keyCode);
        updateSuggestions();
    }

    private void updateSuggestions() {
        String text = this.inputField.getText();
        suggestions.clear();
        if (!text.startsWith(".")) return;
        String[] parts = text.substring(1).split(" ", -1);
        if (parts.length == 0) return;
        String cmd = parts[0].toLowerCase();
        
        if (parts.length == 1) {
            for (Command c : OryvexClient.getInstance().getCommandManager().getCommands()) {
                if (c.getName().toLowerCase().startsWith(cmd)) suggestions.add("." + c.getName());
            }
        } else if (parts[0].equalsIgnoreCase("bind")) {
            if (parts.length == 2) {
                String modPart = parts[1].toLowerCase();
                for (Module m : OryvexClient.getInstance().getModuleManager().getModules()) {
                    if (m.getName().toLowerCase().startsWith(modPart)) suggestions.add(".bind " + m.getName());
                }
            } else if (parts.length == 3) {
                String[] keys = {"R", "F", "C", "X", "Z", "V", "G", "RSHIFT", "NONE"};
                for (String k : keys) {
                    if (k.toLowerCase().startsWith(parts[2].toLowerCase())) suggestions.add(".bind " + parts[1] + " " + k);
                }
            }
        } else if (parts[0].equalsIgnoreCase("toggle")) {
            if (parts.length == 2) {
                String modPart = parts[1].toLowerCase();
                for (Module m : OryvexClient.getInstance().getModuleManager().getModules()) {
                    if (m.getName().toLowerCase().startsWith(modPart)) suggestions.add(".toggle " + m.getName());
                }
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (!suggestions.isEmpty()) {
            int y = this.height - 30 - (suggestions.size() * 12);
            int maxW = 0;
            for (String s : suggestions) maxW = Math.max(maxW, mc.fontRendererObj.getStringWidth(s));
            drawRect(2, y - 2, maxW + 10, y + suggestions.size() * 12, 0xDD1E1E2E);
            int sy = y;
            for (String s : suggestions) {
                drawString(mc.fontRendererObj, s, 5, sy, 0xFF89B4FA);
                sy += 12;
            }
        }
    }
}
