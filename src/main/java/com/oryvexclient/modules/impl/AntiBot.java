
package com.oryvexclient.modules.impl;
import com.oryvexclient.modules.Category;
import com.oryvexclient.modules.Module;
import org.lwjgl.input.Keyboard;

public class AntiBot extends Module {
    public AntiBot() { super("AntiBot", "Filters bots.", Keyboard.KEY_NONE, Category.COMBAT); }
}
