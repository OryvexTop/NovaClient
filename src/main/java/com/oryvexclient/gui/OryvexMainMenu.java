package com.oryvexclient.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.resources.I18n;

public class OryvexMainMenu extends GuiScreen {

    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BACKGROUND_COLOR = 0xFF0A0A0A;
    private static final int ACCENT_COLOR = 0x0096FF;

    @Override
    public void initGui() {
        int centerX = this.width / 2 - BUTTON_WIDTH / 2;
        int startY = this.height / 2 - 40;

        this.buttonList.add(new GuiButton(1, centerX, startY, BUTTON_WIDTH, BUTTON_HEIGHT, "Singleplayer"));
        this.buttonList.add(new GuiButton(2, centerX, startY + 25, BUTTON_WIDTH, BUTTON_HEIGHT, "Multiplayer"));
        this.buttonList.add(new GuiButton(3, centerX, startY + 50, BUTTON_WIDTH, BUTTON_HEIGHT, "Settings"));
        this.buttonList.add(new GuiButton(4, centerX, startY + 75, BUTTON_WIDTH, BUTTON_HEIGHT, "Quit Game"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(this.fontRendererObj, "ORYVEX CLIENT", this.width / 2, this.height / 2 - 80, ACCENT_COLOR);
        drawCenteredString(this.fontRendererObj, "v1.0", this.width / 2, this.height / 2 - 60, 0xAAAAAA);

        super.drawScreen(mouseX, mouseY, partialTicks);

        for (GuiButton button : this.buttonList) {
            boolean hovered = mouseX >= button.xPosition && mouseX <= button.xPosition + button.width &&
                              mouseY >= button.yPosition && mouseY <= button.yPosition + button.height;
            if (hovered) {
                drawRect(button.xPosition, button.yPosition, button.xPosition + button.width, button.yPosition + button.height, 0x330096FF);
            }
            button.drawButton(this.mc, mouseX, mouseY);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1:
                this.mc.displayGuiScreen(new GuiSelectWorld(this));
                break;
            case 2:
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            case 3:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            case 4:
                this.mc.shutdown();
                break;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
