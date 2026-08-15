package com.oryvexclient.gui.clickgui;

import com.oryvexclient.OryvexClient;
import com.oryvexclient.modules.Category;
import com.oryvexclient.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OryvexClickGUI extends GuiScreen {

    private static final int PANEL_WIDTH = 100;
    private static final int PANEL_HEIGHT = 18;
    private static final int CATEGORY_SPACING = 8;
    private static final int BACKGROUND_COLOR = 0xFF0F0F0F;
    private static final int HEADER_COLOR = 0xFF1A1A1A;
    private static final int MODULE_BG = 0xFF232323;
    private static final int MODULE_HOVER_BG = 0xFF2E2E2E;
    private static final int ACCENT = 0x00AAFF;

    private final Minecraft mc = Minecraft.getMinecraft();
    private final List<CategoryPanel> panels = new ArrayList<>();
    private Module waitingForKeyModule = null;

    private int dragX, dragY;
    private CategoryPanel draggingPanel = null;

    public OryvexClickGUI() {
        int x = 10;
        for (Category cat : Category.values()) {
            panels.add(new CategoryPanel(cat, x, 10));
            x += PANEL_WIDTH + CATEGORY_SPACING;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        for (CategoryPanel panel : panels) {
            panel.draw(mouseX, mouseY);
        }
        if (waitingForKeyModule != null) {
            drawCenteredString(mc.fontRendererObj, "Press a key for " + waitingForKeyModule.getName() + "...", width / 2, height / 2 - 10, 0xFFFF00);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (waitingForKeyModule != null) {
            int key = Keyboard.getEventKey();
            if (key == Keyboard.KEY_ESCAPE) key = Keyboard.KEY_NONE;
            waitingForKeyModule.setKeybind(key);
            OryvexClient.getInstance().getKeybindManager().registerBind(waitingForKeyModule);
            OryvexClient.getInstance().getConfigManager().saveConfig();
            waitingForKeyModule = null;
            return;
        }

        for (CategoryPanel panel : panels) {
            if (panel.isMouseOverHeader(mouseX, mouseY)) {
                draggingPanel = panel;
                dragX = mouseX - panel.x;
                dragY = mouseY - panel.y;
                return;
            }
            if (panel.mouseClicked(mouseX, mouseY, mouseButton)) {
                return;
            }
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
        if (waitingForKeyModule != null) {
            if (keyCode == Keyboard.KEY_ESCAPE) keyCode = Keyboard.KEY_NONE;
            waitingForKeyModule.setKeybind(keyCode);
            OryvexClient.getInstance().getKeybindManager().registerBind(waitingForKeyModule);
            OryvexClient.getInstance().getConfigManager().saveConfig();
            waitingForKeyModule = null;
            return;
        }
        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(null);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void setWaitingForKey(Module module) {
        waitingForKeyModule = module;
    }

    private class CategoryPanel {
        private final Category category;
        private int x, y;
        private boolean open = true;
        private final List<Module> modules = new ArrayList<>();

        CategoryPanel(Category category, int x, int y) {
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
            return mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY >= y && mouseY <= y + PANEL_HEIGHT;
        }

        boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
            if (isMouseOverHeader(mouseX, mouseY)) {
                if (mouseButton == 0) {
                    open = !open;
                }
                return true;
            }
            if (open) {
                int moduleY = y + PANEL_HEIGHT + 2;
                for (Module module : modules) {
                    int rowHeight = 16;
                    if (mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY >= moduleY && mouseY <= moduleY + rowHeight) {
                        if (mouseButton == 0) {
                            module.toggle();
                        } else if (mouseButton == 1) {
                            setWaitingForKey(module);
                        }
                        return true;
                    }
                    moduleY += rowHeight;
                }
            }
            return false;
        }

        void draw(int mouseX, int mouseY) {
            // Header
            drawRect(x, y, x + PANEL_WIDTH, y + PANEL_HEIGHT, HEADER_COLOR);
            String title = category.name().substring(0, 1).toUpperCase() + category.name().substring(1).toLowerCase();
            mc.fontRendererObj.drawStringWithShadow(title, x + 5, y + 5, ACCENT);
            String arrow = open ? "-" : "+";
            mc.fontRendererObj.drawStringWithShadow(arrow, x + PANEL_WIDTH - 10, y + 5, 0xFFFFFF);

            if (open) {
                int moduleY = y + PANEL_HEIGHT + 2;
                for (Module module : modules) {
                    int rowHeight = 16;
                    boolean hovered = mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY >= moduleY && mouseY <= moduleY + rowHeight;
                    int bgColor = hovered ? MODULE_HOVER_BG : MODULE_BG;
                    drawRect(x, moduleY, x + PANEL_WIDTH, moduleY + rowHeight, bgColor);

                    int textColor = module.isToggled() ? 0x00FF00 : 0xFF5555;
                    mc.fontRendererObj.drawStringWithShadow(module.getName(), x + 5, moduleY + 4, textColor);

                    String keyName = module.getKeybind() == Keyboard.KEY_NONE ? "NONE" : Keyboard.getKeyName(module.getKeybind());
                    int keyWidth = mc.fontRendererObj.getStringWidth(keyName);
                    mc.fontRendererObj.drawStringWithShadow(keyName, x + PANEL_WIDTH - keyWidth - 5, moduleY + 4, 0xCCCCCC);

                    moduleY += rowHeight;
                }
            }
        }
    }
}
