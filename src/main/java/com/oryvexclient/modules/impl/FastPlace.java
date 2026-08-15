
package com.oryvexclient.modules.impl;
import com.oryvexclient.modules.Category;
import com.oryvexclient.modules.Module;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import java.lang.reflect.Field;

public class FastPlace extends Module {
    public FastPlace() { super("FastPlace", "Places blocks instantly.", Keyboard.KEY_NONE, Category.OTHER); }
    @Override public void onUpdate() {
        try {
            Field f = Minecraft.class.getDeclaredField("rightClickDelayTimer");
            f.setAccessible(true);
            f.set(mc, 0);
        } catch (Exception ignored) {}
    }
}
