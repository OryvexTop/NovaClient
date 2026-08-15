
package com.oryvexclient.modules.impl;
import com.oryvexclient.modules.Category;
import com.oryvexclient.modules.Module;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

public class NoFall extends Module {
    public NoFall() { super("NoFall", "Prevents fall damage.", Keyboard.KEY_NONE, Category.MOVEMENT); }
    @Override public void onUpdate() {
        if (mc.thePlayer.fallDistance > 2.5) mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
    }
}
