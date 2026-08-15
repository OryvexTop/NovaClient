
package com.oryvexclient.modules.impl;
import com.oryvexclient.modules.Category;
import com.oryvexclient.modules.Module;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class AutoClicker extends Module {
    private long lastClick = 0;
    public AutoClicker() { super("AutoClicker", "Clicks automatically.", Keyboard.KEY_NONE, Category.COMBAT); }
    @Override public void onUpdate() {
        if (System.currentTimeMillis() - lastClick > 100 && mc.gameSettings.keyBindAttack.isKeyDown()) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
            KeyBinding.onTick(mc.gameSettings.keyBindAttack.getKeyCode());
            lastClick = System.currentTimeMillis();
        }
    }
}
