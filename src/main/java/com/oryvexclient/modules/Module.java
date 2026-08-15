package com.oryvexclient.modules;

import net.minecraft.client.Minecraft;

public abstract class Module {

    protected static final Minecraft mc = Minecraft.getMinecraft();

    private final String name;
    private String displayName;
    private int keybind;
    private boolean toggled;
    private final Category category;

    public Module(String name, int keybind, Category category) {
        this.name = name;
        this.displayName = name;
        this.keybind = keybind;
        this.category = category;
        this.toggled = false;
    }

    public void toggle() {
        toggled = !toggled;
        if (toggled) onEnable();
        else onDisable();
    }

    protected void onEnable() {}
    protected void onDisable() {}
    public void onUpdate() {}
    public void onRender() {}

    public String getName() { return name; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public int getKeybind() { return keybind; }
    public void setKeybind(int keybind) { this.keybind = keybind; }
    public boolean isToggled() { return toggled; }
    public void setToggled(boolean toggled) { this.toggled = toggled; }
    public Category getCategory() { return category; }
}
