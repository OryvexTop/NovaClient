package com.oryvexclient.modules.impl;

import com.oryvexclient.modules.impl.modules.Category;
import com.oryvexclient.modules.impl.modules.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

import java.util.Comparator;
import java.util.List;

public class KillAura extends Module {

    private float range = 4.5f;
    private float rotationSpeed = 180f;
    private boolean onlyPlayers = true;
    private boolean throughWalls = false;
    private long lastAttackTime = 0;
    private long attackDelay = 150;

    public KillAura() {
        super("KillAura", Keyboard.KEY_R, Category.COMBAT);
    }

    @Override
    protected void onEnable() {
        lastAttackTime = 0;
    }

    @Override
    public void onUpdate() {
        if (mc.thePlayer == null || mc.theWorld == null) return;

        Entity target = findTarget();
        if (target == null) return;

        rotateToEntity(target);
        if (System.currentTimeMillis() - lastAttackTime >= attackDelay) {
            mc.playerController.attackEntity(mc.thePlayer, target);
            mc.thePlayer.swingItem();
            lastAttackTime = System.currentTimeMillis();
        }
    }

    private Entity findTarget() {
        List<Entity> entities = mc.theWorld.getEntitiesWithinAABBExcludingEntity(
            mc.thePlayer,
            new AxisAlignedBB(
                mc.thePlayer.posX - range, mc.thePlayer.posY - range, mc.thePlayer.posZ - range,
                mc.thePlayer.posX + range, mc.thePlayer.posY + range, mc.thePlayer.posZ + range
            )
        );
        if (entities.isEmpty()) return null;

        return entities.stream()
            .filter(e -> e instanceof EntityLivingBase)
            .filter(e -> e != mc.thePlayer)
            .filter(e -> e instanceof EntityLivingBase && ((EntityLivingBase) e).isEntityAlive())
            .filter(e -> !(e instanceof EntityPlayer) || onlyPlayers ? e instanceof EntityPlayer : true)
            .filter(e -> throughWalls || mc.thePlayer.canEntityBeSeen(e))
            .min(Comparator.comparingDouble(e -> e.getDistanceToEntity(mc.thePlayer)))
            .orElse(null);
    }

    private void rotateToEntity(Entity entity) {
        double[] rotations = getRotations(entity);
        mc.thePlayer.rotationYaw = (float) rotations[0];
        mc.thePlayer.rotationPitch = (float) rotations[1];
    }

    private double[] getRotations(Entity entity) {
        double x = entity.posX - mc.thePlayer.posX;
        double y = entity.posY + entity.getEyeHeight() - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        double z = entity.posZ - mc.thePlayer.posZ;
        double dist = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0 / Math.PI) - 90.0F;
        float pitch = (float) -(Math.atan2(y, dist) * 180.0 / Math.PI);
        return new double[]{ yaw, pitch };
    }
}
