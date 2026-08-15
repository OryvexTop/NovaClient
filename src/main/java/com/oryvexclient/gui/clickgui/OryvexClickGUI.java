package com.oryvexclient.gui.clickgui;

import com.oryvexclient.OryvexClient;
import com.oryvexclient.modules.Category;
import com.oryvexclient.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OryvexClickGUI extends GuiScreen {
    private static final int PANEL_WIDTH = 120;
    private static final int HEADER_HEIGHT = 18;
    private static final int MODULE_HEIGHT = 16;
    private static final int CATEGORY_SPACING = 15;

    private final int BG_COLOR = new Color(17, 17, 27, 180).getRGB();
    private final int PANEL_BG = new Color(30, 30, 46, 255).getRGB();
    private final int HEADER_BG = new Color(45, 45, 65, 255).getRGB();
    private final int ACCENT = new Color(137, 180, 250, 255).getRGB();
    private final int TEXT_COLOR = new Color(205, 214, 244, 255).getRGB();
    private final int TEXT_SECONDARY = new Color(166, 173, 200, 255).getRGB();
    private final int MODULE_HOVER = new Color(65, 65, 85, 255).getRGB();

    private final List<Panel> panels = new ArrayList<>();
    private Module bindingModule = null;
    private Panel draggingPanel = null;
    private int dragX, dragY;

    public OryvexClickGUI() {
        int x = 20;
        for (Category cat : Category.values()) {
            panels.add(new Panel(cat, x, 20));
            x += PANEL_WIDTH + CATEGORY_SPACING;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(0, 0, width, height, BG_COLOR);
        for (Panel panel : panels) panel.draw(mouseX, mouseY);
        
        if (bindingModule != null) {
            drawCenteredString(mc.fontRendererObj, "Press a key for " + bindingModule.getName() + "...", width / 2, height - 40, 0xFFFF5555);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (bindingModule != null) return;
        for (Panel panel : panels) {
            if (panel.isMouseOverHeader(mouseX, mouseY)) {
                draggingPanel = panel;
                dragX = mouseX - panel.x;
                dragY = mouseY - panel.y;
                return;
            }
            if (panel.mouseClicked(mouseX, mouseY, mouseButton)) return;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        draggingPanel = null;
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (draggingPanel != null) {
            draggingPanel.x = mouseX - dragX;
            draggingPanel.y = mouseY - dragY;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (bindingModule != null) {
            if (keyCode == Keyboard.KEY_ESCAPE) keyCode = Keyboard.KEY_NONE;
            bindingModule.setKeybind(keyCode);
            OryvexClient.getInstance().getKeybindManager().updateBinds();
            OryvexClient.getInstance().getConfigManager().saveConfig();
            bindingModule = null;
            return;
        }
        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(null);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public boolean doesGuiPauseGame() { return false; }

    private class Panel {
        private final Category category;
        private int x, y;
        private boolean open = true;
        private final List<Module> modules = new ArrayList<>();

        Panel(Category category, int x, int y) {
            this.category = category;
            this.x = x;
            this.y = y;
            refreshModules();
        }

        void refreshModules() {
            modules.clear();
            for (Module m : OryvexClient.getInstance().getModuleManager().getModules()) {
                if (m.getCategory() == category) modules.add(m);
            }
        }

        boolean isMouseOverHeader(int mouseX, int mouseY) {
            return mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY >= y && mouseY <= y + HEADER_HEIGHT;
        }

        boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
            if (isMouseOverHeader(mouseX, mouseY)) {
                if (mouseButton == 0) open = !open;
                return true;
            }
            if (open) {
                int moduleY = y + HEADER_HEIGHT;
                for (Module module : modules) {
                    if (mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY >= moduleY && mouseY <= moduleY + MODULE_HEIGHT) {
                        if (mouseButton == 0) module.toggle();
                        else if (mouseButton == 1) bindingModule = module;
                        return true;
                    }
                    moduleY += MODULE_HEIGHT;
                }
            }
            return false;
        }

        void draw(int mouseX, int mouseY) {
            drawRect(x, y, x + PANEL_WIDTH, y + HEADER_HEIGHT, HEADER_BG);
            String title = category.name().substring(0, 1).toUpperCase() + category.name().substring(1).toLowerCase();
            mc.fontRendererObj.drawStringWithShadow(title, x + 5, y + 5, ACCENT);
            
            if (open) {
                int moduleY = y + HEADER_HEIGHT;
                drawRect(x, moduleY, x + PANEL_WIDTH, moduleY + modules.size() * MODULE_HEIGHT, PANEL_BG);
                for (Module module : modules) {
                    boolean hovered = mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY >= moduleY && mouseY <= moduleY + MODULE_HEIGHT;
                    int bgColor = hovered ? MODULE_HOVER : PANEL_BG;
                    drawRect(x, moduleY, x + PANEL_WIDTH, moduleY + MODULE_HEIGHT, bgColor);

                    int textColor = module.isToggled() ? ACCENT : TEXT_SECONDARY;
                    mc.fontRendererObj.drawStringWithShadow(module.getName(), x + 5, moduleY + 4, textColor);

                    String keyName = module.getKeybind() == Keyboard.KEY_NONE ? "NONE" : Keyboard.getKeyName(module.getKeybind());
                    int keyWidth = mc.fontRendererObj.getStringWidth(keyName);
                    mc.fontRendererObj.drawStringWithShadow(keyName, x + PANEL_WIDTH - keyWidth - 5, moduleY + 4, 0xFF888888);

                    moduleY += MODULE_HEIGHT;
                }
            }
        }
    }
}
