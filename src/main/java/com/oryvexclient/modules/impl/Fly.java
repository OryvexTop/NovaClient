
package com.oryvexclient.modules.impl;
import com.oryvexclient.modules.Category;
import com.oryvexclient.modules.Module;
import org.lwjgl.input.Keyboard;

public class Fly extends Module {
    public Fly() { super("Fly", "Creative fly.", Keyboard.KEY_NONE, Category.MOVEMENT); }
    @Override public void onEnable() { mc.thePlayer.capabilities.isFlying = true; super.onEnable(); }
    @Override public void onDisable() { mc.thePlayer.capabilities.isFlying = false; super.onDisable(); }
}
