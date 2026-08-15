
package com.oryvexclient.modules.impl;
import com.oryvexclient.modules.Category;
import com.oryvexclient.modules.Module;
import org.lwjgl.input.Keyboard;

public class Fullbright extends Module {
    public Fullbright() { super("Fullbright", "Max gamma.", Keyboard.KEY_NONE, Category.RENDER); }
    @Override public void onEnable() { mc.gameSettings.gammaSetting = 1000f; super.onEnable(); }
    @Override public void onDisable() { mc.gameSettings.gammaSetting = 1.0f; super.onDisable(); }
}
