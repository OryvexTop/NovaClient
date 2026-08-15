
package com.oryvexclient.modules.impl;
import com.oryvexclient.modules.Category;
import com.oryvexclient.modules.Module;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;
import org.lwjgl.input.Keyboard;

public class ChestStealer extends Module {
    public ChestStealer() { super("ChestStealer", "Steals from chests.", Keyboard.KEY_NONE, Category.OTHER); }
    @Override public void onUpdate() {
        if (mc.currentScreen instanceof GuiChest) {
            GuiChest chest = (GuiChest) mc.currentScreen;
            IInventory inv = chest.inventorySlots.getSlot(0).inventory;
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                if (inv.getStackInSlot(i) != null) {
                    mc.playerController.windowClick(chest.inventorySlots.windowId, i, 0, 1, mc.thePlayer);
                    return;
                }
            }
        }
    }
}
