
package com.oryvexclient.modules.impl;
import com.oryvexclient.modules.Category;
import com.oryvexclient.modules.Module;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class FastBreak extends Module {
    public FastBreak() { super("FastBreak", "Breaks blocks faster.", Keyboard.KEY_NONE, Category.WORLD); }
    @SubscribeEvent
    public void onBreakSpeed(PlayerEvent.BreakSpeed e) { e.newSpeed = e.originalSpeed * 3.0f; }
}
