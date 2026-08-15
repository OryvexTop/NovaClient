
package com.oryvexclient.gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.renderer.GlStateManager;
import com.oryvexclient.gui.altmanager.AltManagerGUI;
import java.awt.Color;

public class OryvexMainMenu extends GuiScreen {
    @Override
    public void initGui() {
        this.buttonList.clear();
        int centerY = this.height / 2;
        int centerX = this.width / 2;
        this.buttonList.add(new OryvexButton(1, centerX - 100, centerY - 40, 200, 20, "Singleplayer"));
        this.buttonList.add(new OryvexButton(2, centerX - 100, centerY - 15, 200, 20, "Multiplayer"));
        this.buttonList.add(new OryvexButton(3, centerX - 100, centerY + 10, 98, 20, "Alt Manager"));
        this.buttonList.add(new OryvexButton(4, centerX + 2, centerY + 10, 98, 20, "Settings"));
        this.buttonList.add(new OryvexButton(5, centerX - 100, centerY + 45, 200, 20, "Quit Game"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int topColor = new Color(17, 17, 27, 255).getRGB();
        int bottomColor = new Color(30, 30, 46, 255).getRGB();
        drawGradientRect(0, 0, this.width, this.height, topColor, bottomColor);
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        drawCenteredString(this.fontRendererObj, "ORYVEX CLIENT", this.width / 4, 20, 0xFF89B4FA);
        GlStateManager.popMatrix();
        drawCenteredString(this.fontRendererObj, "v1.0.0 | Modern 1.8.9 Client", this.width / 2, 60, 0xFFA6ADC8);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    class OryvexButton extends GuiButton {
        public OryvexButton(int buttonId, int x, int y, int width, int height, String buttonText) { super(buttonId, x, y, width, height, buttonText); }
        @Override
        public void drawButton(net.minecraft.client.Minecraft mc, int mouseX, int mouseY) {
            if (this.visible) {
                boolean hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                int bgColor = hovered ? new Color(65, 65, 85, 255).getRGB() : new Color(45, 45, 65, 255).getRGB();
                drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, bgColor);
                this.mouseDragged(mc, mouseX, mouseY);
                int textColor = hovered ? 0xFFFFFFFF : 0xFFCDD6F4;
                drawCenteredString(mc.fontRendererObj, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, textColor);
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1: this.mc.displayGuiScreen(new GuiSelectWorld(this)); break;
            case 2: this.mc.displayGuiScreen(new GuiMultiplayer(this)); break;
            case 3: this.mc.displayGuiScreen(new AltManagerGUI(this)); break;
            case 4: this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings)); break;
            case 5: this.mc.shutdown(); break;
        }
    }
    @Override public boolean doesGuiPauseGame() { return false; }
}
