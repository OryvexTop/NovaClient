package com.oryvexclient.gui.altmanager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AltManagerGUI extends GuiScreen {

    private GuiScreen parent;
    private GuiTextField usernameField;
    private List<String> alts = new ArrayList<>();
    private int selected = -1;

    public AltManagerGUI(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        usernameField = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
        usernameField.setMaxStringLength(16);
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, 90, 200, 20, "Add Alt"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, 115, 200, 20, "Login Selected"));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 100, 140, 200, 20, "Back"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, "Alt Manager", this.width / 2, 20, 0xFFFFFF);
        usernameField.drawTextBox();
        int y = 170;
        for (int i = 0; i < alts.size(); i++) {
            int color = (i == selected) ? 0x00AAFF : 0xFFFFFF;
            drawString(fontRendererObj, alts.get(i), 10, y, color);
            y += 12;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        usernameField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (usernameField.textboxKeyTyped(typedChar, keyCode)) {
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1:
                if (!usernameField.getText().isEmpty()) {
                    alts.add(usernameField.getText());
                    usernameField.setText("");
                }
                break;
            case 2:
                if (selected >= 0 && selected < alts.size()) {
                    setSessionUsername(alts.get(selected));
                }
                break;
            case 3:
                mc.displayGuiScreen(parent);
                break;
        }
    }

    private void setSessionUsername(String name) {
        mc.setSession(new Session(name, "0", "0", "mojang"));
        System.out.println("Session changed to " + name);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
}
