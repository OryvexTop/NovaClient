package com.oryvexclient.modules.impl;

import com.oryvexclient.modules.Category;
import com.oryvexclient.modules.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

import java.util.Comparator;
import java.util.List;

public class KillAura extends Module {
    private float range = 4.5f;
    private long lastAttackTime = 0;
    private long attackDelay = 150;

    public KillAura() {
        super("KillAura", "Attacks entities around you.", Keyboard.KEY_R, Category.COMBAT);
    }

    @Override
    public void onUpdate() {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        Entity target = findTarget();
        if (target == null) return;

        double[] rotations = getRotations(target);
        sendRotationPacket((float) rotations[0], (float) rotations[1]);

        if (System.currentTimeMillis() - lastAttackTime >= attackDelay) {
            mc.playerController.attackEntity(mc.thePlayer, target);
            mc.thePlayer.swingItem();
            lastAttackTime = System.currentTimeMillis();
        }
    }

    private void sendRotationPacket(float yaw, float pitch) {
        C03PacketPlayer.C05PacketPlayerLook packet = new C03PacketPlayer.C05PacketPlayerLook(yaw, pitch, mc.thePlayer.onGround);
        mc.getNetHandler().addToSendQueue(packet);
    }

    private Entity findTarget() {
        List<Entity> entities = mc.theWorld.getEntitiesWithinAABBExcludingEntity(
            mc.thePlayer, mc.thePlayer.getEntityBoundingBox().expand(range, range, range));
        
        return entities.stream()
            .filter(e -> e instanceof EntityLivingBase)
            .filter(e -> !(e instanceof EntityArmorStand))
            .filter(e -> e != mc.thePlayer)
            .filter(e -> ((EntityLivingBase) e).getHealth() > 0)
            .filter(e -> mc.thePlayer.getDistanceToEntity(e) <= range)
            .min(Comparator.comparingDouble(e -> e.getDistanceToEntity(mc.thePlayer)))
            .orElse(null);
    }

    private double[] getRotations(Entity entity) {
        double x = entity.posX - mc.thePlayer.posX;
        double y = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        double z = entity.posZ - mc.thePlayer.posZ;
        double dist = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0 / Math.PI) - 90.0F;
        float pitch = (float) -(Math.atan2(y, dist) * 180.0 / Math.PI);
        return new double[]{ yaw, pitch };
    }
}
