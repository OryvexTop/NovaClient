
package com.oryvexclient.modules.impl;
import com.oryvexclient.modules.Category;
import com.oryvexclient.modules.Module;
import com.oryvexclient.utils.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import java.util.Comparator;

public class KillAura extends Module {
    private final NumberSetting range = new NumberSetting("Range", 4.5, 3.0, 6.0, 0.1);
    public KillAura() { 
        super("KillAura", "Attacks entities.", Keyboard.KEY_R, Category.COMBAT); 
        addSetting(range);
    }
    @Override public void onUpdate() {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        Entity target = mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().expand(range.getValue(), range.getValue(), range.getValue())).stream()
            .filter(e -> e instanceof EntityLivingBase && !(e instanceof EntityArmorStand) && e != mc.thePlayer && ((EntityLivingBase)e).getHealth() > 0)
            .min(Comparator.comparingDouble(e -> e.getDistanceToEntity(mc.thePlayer))).orElse(null);
        if (target == null) return;
        double x = target.posX - mc.thePlayer.posX;
        double y = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        double z = target.posZ - mc.thePlayer.posZ;
        double dist = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0 / Math.PI) - 90.0F;
        float pitch = (float) -(Math.atan2(y, dist) * 180.0 / Math.PI);
        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(yaw, pitch, mc.thePlayer.onGround));
        if (mc.thePlayer.getDistanceToEntity(target) <= range.getValue()) {
            mc.playerController.attackEntity(mc.thePlayer, target);
            mc.thePlayer.swingItem();
        }
    }
}
