
package com.oryvexclient.modules;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import com.oryvexclient.utils.Setting;
import java.util.ArrayList;
import java.util.List;

public abstract class Module {
    protected static final Minecraft mc = Minecraft.getMinecraft();
    private final String name;
    private final String description;
    private int keybind;
    private boolean toggled;
    private final Category category;
    protected List<Setting> settings = new ArrayList<>();

    public Module(String name, String description, int keybind, Category category) {
        this.name = name; this.description = description; this.keybind = keybind; this.category = category; this.toggled = false;
    }

    public void toggle() { toggled = !toggled; if (toggled) onEnable(); else onDisable(); }
    
    protected void onEnable() { MinecraftForge.EVENT_BUS.register(this); }
    protected void onDisable() { MinecraftForge.EVENT_BUS.unregister(this); }
    public void onUpdate() {}

    public void addSetting(Setting setting) { settings.add(setting); }
    public List<Setting> getSettings() { return settings; }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getKeybind() { return keybind; }
    public void setKeybind(int keybind) { this.keybind = keybind; }
    public boolean isToggled() { return toggled; }
    public void setToggled(boolean toggled) { this.toggled = toggled; if (toggled) onEnable(); else onDisable(); }
    public Category getCategory() { return category; }
}
