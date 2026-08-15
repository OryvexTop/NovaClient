package com.oryvexclient.modules.impl;

import com.oryvexclient.modules.Category;
import com.oryvexclient.modules.Module;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.List;

public class Scaffold extends Module {

    private final List<Block> blacklist = Arrays.asList(
        Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava,
        Blocks.flowing_lava, Blocks.bedrock, Blocks.chest,
        Blocks.ender_chest, Blocks.trapped_chest
    );

    public Scaffold() {
        super("Scaffold", Keyboard.KEY_NONE, Category.WORLD);
    }

    @Override
    public void onUpdate() {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        BlockPos below = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
        Block blockBelow = mc.theWorld.getBlockState(below).getBlock();
        if (isValid(blockBelow)) {
            int slot = getBlockSlot();
            if (slot != -1) {
                placeBlock(below, slot);
            }
        }
    }

    private boolean isValid(Block block) {
        return block == Blocks.air || blacklist.contains(block);
    }

    private int getBlockSlot() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemBlock) {
                Block block = ((ItemBlock) stack.getItem()).getBlock();
                if (!blacklist.contains(block)) return i;
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
            if (!isValid(neighborBlock)) {
                float[] hit = getHitVec(pos, facing);
                mc.playerController.onPlayerRightClick(
                    mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(),
                    neighbor, facing.getOpposite(), new Vec3(hit[0], hit[1], hit[2])
                );
                break;
            }
        }

        mc.thePlayer.inventory.currentItem = oldSlot;
    }

    private float[] getHitVec(BlockPos pos, EnumFacing facing) {
        return new float[] {
            pos.getX() + 0.5f + facing.getFrontOffsetX() * 0.5f,
            pos.getY() + 0.5f + facing.getFrontOffsetY() * 0.5f,
            pos.getZ() + 0.5f + facing.getFrontOffsetZ() * 0.5f
        };
    }
}
