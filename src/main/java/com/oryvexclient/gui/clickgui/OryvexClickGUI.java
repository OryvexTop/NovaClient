
package com.oryvexclient.gui.clickgui;
import com.oryvexclient.OryvexClient;
import com.oryvexclient.modules.Category;
import com.oryvexclient.modules.Module;
import com.oryvexclient.utils.*;
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
    
    private Module selectedModule = null;
    private int selX, selY;
    private NumberSetting draggingSlider = null;

    public OryvexClickGUI() {
        int x = 20;
        for (Category cat : Category.values()) { panels.add(new Panel(cat, x, 20)); x += PANEL_WIDTH + CATEGORY_SPACING; }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(0, 0, width, height, BG_COLOR);
        for (Panel panel : panels) panel.draw(mouseX, mouseY);
        
        if (selectedModule != null) drawSettingsPanel(mouseX, mouseY);
        
        if (bindingModule != null) drawCenteredString(mc.fontRendererObj, "Press a key for " + bindingModule.getName() + "...", width / 2, height - 40, 0xFFFF5555);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawSettingsPanel(int mouseX, int mouseY) {
        int w = 120;
        int h = 20 + selectedModule.getSettings().size() * 20 + 20;
        
        // Clamp to screen
        if (selX + w > width) selX = width - w - 5;
        if (selY + h > height) selY = height - h - 5;

        drawRect(selX, selY, selX + w, selY + h, PANEL_BG);
        drawRect(selX, selY, selX + w, selY + 18, HEADER_BG);
        drawRect(selX, selY + 17, selX + w, selY + 18, ACCENT); // Accent line
        mc.fontRendererObj.drawStringWithShadow(selectedModule.getName(), selX + 5, selY + 5, ACCENT);
        
        int sy = selY + 20;
        for (Setting s : selectedModule.getSettings()) {
            boolean hovered = mouseX >= selX && mouseX <= selX + w && mouseY >= sy && mouseY <= sy + 18;
            drawRect(selX, sy, selX + w, sy + 18, hovered ? MODULE_HOVER : PANEL_BG);
            
            if (s instanceof BooleanSetting) {
                BooleanSetting bs = (BooleanSetting) s;
                mc.fontRendererObj.drawStringWithShadow(s.getName(), selX + 5, sy + 5, TEXT_COLOR);
                int boxColor = bs.getValue() ? ACCENT : TEXT_SECONDARY;
                drawRect(selX + w - 16, sy + 4, selX + w - 6, sy + 14, boxColor);
            } else if (s instanceof NumberSetting) {
                NumberSetting ns = (NumberSetting) s;
                mc.fontRendererObj.drawStringWithShadow(s.getName(), selX + 5, sy + 5, TEXT_COLOR);
                String val = String.format("%.1f", ns.getValue());
                mc.fontRendererObj.drawStringWithShadow(val, selX + w - mc.fontRendererObj.getStringWidth(val) - 5, sy + 5, TEXT_SECONDARY);
                // Slider bar
                float pct = (float) ((ns.getValue() - ns.getMin()) / (ns.getMax() - ns.getMin()));
                drawRect(selX + 2, sy + 16, selX + w - 2, sy + 17, TEXT_SECONDARY);
                drawRect(selX + 2, sy + 16, selX + 2 + (int)((w - 4) * pct), sy + 17, ACCENT);
            } else if (s instanceof ModeSetting) {
                ModeSetting ms = (ModeSetting) s;
                mc.fontRendererObj.drawStringWithShadow(s.getName(), selX + 5, sy + 5, TEXT_COLOR);
                mc.fontRendererObj.drawStringWithShadow(ms.getValue(), selX + w - mc.fontRendererObj.getStringWidth(ms.getValue()) - 5, sy + 5, ACCENT);
            }
            sy += 20;
        }
        
        // Bind button
        boolean bindHovered = mouseX >= selX && mouseX <= selX + w && mouseY >= sy && mouseY <= sy + 18;
        drawRect(selX, sy, selX + w, sy + 18, bindHovered ? MODULE_HOVER : PANEL_BG);
        String bindStr = bindingModule == selectedModule ? "..." : Keyboard.getKeyName(selectedModule.getKeybind());
        mc.fontRendererObj.drawStringWithShadow("Bind: " + bindStr, selX + 5, sy + 5, TEXT_COLOR);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (bindingModule != null) return;
        
        // Handle settings panel clicks
        if (selectedModule != null) {
            int w = 120;
            int h = 20 + selectedModule.getSettings().size() * 20 + 20;
            if (mouseX >= selX && mouseX <= selX + w && mouseY >= selY && mouseY <= selY + h) {
                int sy = selY + 20;
                for (Setting s : selectedModule.getSettings()) {
                    if (mouseY >= sy && mouseY <= sy + 18) {
                        if (s instanceof BooleanSetting) ((BooleanSetting)s).toggle();
                        else if (s instanceof ModeSetting) ((ModeSetting)s).cycle();
                        else if (s instanceof NumberSetting) draggingSlider = (NumberSetting) s;
                        OryvexClient.getInstance().getConfigManager().saveConfig();
                        return;
                    }
                    sy += 20;
                }
                if (mouseY >= sy && mouseY <= sy + 18) { bindingModule = selectedModule; return; }
                return; // Clicked inside panel but not on a setting
            } else {
                selectedModule = null; // Clicked outside, close settings
            }
        }

        for (Panel panel : panels) {
            if (panel.isMouseOverHeader(mouseX, mouseY)) { draggingPanel = panel; dragX = mouseX - panel.x; dragY = mouseY - panel.y; return; }
            if (panel.mouseClicked(mouseX, mouseY, mouseButton)) return;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) { draggingPanel = null; draggingSlider = null; super.mouseReleased(mouseX, mouseY, state); }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (draggingPanel != null) { draggingPanel.x = mouseX - dragX; draggingPanel.y = mouseY - dragY; }
        if (draggingSlider != null) {
            double pct = (double)(mouseX - selX) / 120.0;
            pct = Math.max(0, Math.min(1, pct));
            double newVal = draggingSlider.getMin() + (draggingSlider.getMax() - draggingSlider.getMin()) * pct;
            draggingSlider.setValue(newVal);
            OryvexClient.getInstance().getConfigManager().saveConfig();
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
        if (keyCode == Keyboard.KEY_ESCAPE) { mc.displayGuiScreen(null); return; }
        super.keyTyped(typedChar, keyCode);
    }

    @Override public boolean doesGuiPauseGame() { return false; }

    private class Panel {
        private final Category category;
        private int x, y;
        private boolean open = true;
        private final List<Module> modules = new ArrayList<>();
        private final Animation heightAnim = new Animation(0, 0.2f);

        Panel(Category category, int x, int y) {
            this.category = category; this.x = x; this.y = y; refreshModules();
        }

        void refreshModules() {
            modules.clear();
            for (Module m : OryvexClient.getInstance().getModuleManager().getModules()) {
                if (m.getCategory() == category) modules.add(m);
            }
        }

        boolean isMouseOverHeader(int mouseX, int mouseY) { return mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY >= y && mouseY <= y + HEADER_HEIGHT; }

        boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
            if (isMouseOverHeader(mouseX, mouseY)) { if (mouseButton == 0) open = !open; return true; }
            if (open) {
                int moduleY = y + HEADER_HEIGHT;
                for (Module module : modules) {
                    if (mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY >= moduleY && mouseY <= moduleY + MODULE_HEIGHT) {
                        if (mouseButton == 0) module.toggle();
                        else if (mouseButton == 1) {
                            selectedModule = module;
                            selX = x + PANEL_WIDTH;
                            selY = moduleY;
                        }
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
            drawRect(x, y + HEADER_HEIGHT - 1, x + PANEL_WIDTH, y + HEADER_HEIGHT, ACCENT); // Accent line
            
            heightAnim.setTarget(open ? modules.size() * MODULE_HEIGHT : 0);
            float h = (float) heightAnim.getValue(); // FIX: explicit cast to float
            if (h > 0) {
                drawRect(x, y + HEADER_HEIGHT, x + PANEL_WIDTH, y + HEADER_HEIGHT + (int)h, PANEL_BG);
                int moduleY = y + HEADER_HEIGHT;
                for (Module module : modules) {
                    if (moduleY - (y + HEADER_HEIGHT) >= h) break;
                    boolean hovered = mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY >= moduleY && mouseY <= moduleY + MODULE_HEIGHT;
                    int bgColor = hovered ? MODULE_HOVER : PANEL_BG;
                    drawRect(x, moduleY, x + PANEL_WIDTH, moduleY + MODULE_HEIGHT, bgColor);
                    int textColor = module.isToggled() ? ACCENT : TEXT_SECONDARY;
                    mc.fontRendererObj.drawStringWithShadow(module.getName(), x + 5, moduleY + 4, textColor);
                    
                    // Small indicator for settings
                    if (!module.getSettings().isEmpty()) {
                        mc.fontRendererObj.drawStringWithShadow(">", x + PANEL_WIDTH - 10, moduleY + 4, 0xFF888888);
                    }
                    moduleY += MODULE_HEIGHT;
                }
            }
        }
    }
}
