
package com.oryvexclient.modules.impl;
import com.oryvexclient.modules.Category;
import com.oryvexclient.modules.Module;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

public class Scaffold extends Module {
    public Scaffold() { super("Scaffold", "Places blocks under you.", Keyboard.KEY_NONE, Category.WORLD); }
    @Override public void onUpdate() {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        BlockPos below = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
        Block blockBelow = mc.theWorld.getBlockState(below).getBlock();
        if (blockBelow.getMaterial() == Material.air) {
            int slot = getBlockSlot();
            if (slot != -1) placeBlock(below, slot);
        }
    }
    private int getBlockSlot() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemBlock) {
                Block block = ((ItemBlock) stack.getItem()).getBlock();
                if (block.getMaterial() != Material.air && block != Blocks.chest && block != Blocks.ender_chest) return i;
            }
        }
        return -1;
    }
    private void placeBlock(BlockPos pos, int slot) {
        int oldSlot = mc.thePlayer.inventory.currentItem;
        mc.thePlayer.inventory.currentItem = slot;
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos neighbor = pos.offset(facing);
            Block neighborBlock = mc.theWorld.getBlockState(neighbor).getBlock();
            if (neighborBlock.getMaterial() != Material.air) {
                Vec3 hitVec = new Vec3(neighbor.getX() + 0.5 + facing.getFrontOffsetX() * 0.5, neighbor.getY() + 0.5 + facing.getFrontOffsetY() * 0.5, neighbor.getZ() + 0.5 + facing.getFrontOffsetZ() * 0.5);
                if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), neighbor, facing.getOpposite(), hitVec)) {
                    mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
                    break;
                }
            }
        }
        mc.thePlayer.inventory.currentItem = oldSlot;
    }
}
