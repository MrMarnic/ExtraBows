package me.marnic.extrabows.common.container;

import me.marnic.extrabows.api.util.UpgradeUtil;
import me.marnic.extrabows.client.gui.slot.UpgradeSlot;
import me.marnic.extrabows.common.items.BasicBow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;

/**
 * Copyright (c) 30.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BowUpgradeGuiContainer extends Container {

    private static int SIZE = 4;
    private IItemHandler handler;

    public BowUpgradeGuiContainer(EntityPlayer player) {

        ItemStack bowStack = player.getHeldItemMainhand();

        handler = UpgradeUtil.getHandlerForItemStackNEW(bowStack);
        this.addSlotToContainer(new UpgradeSlot(handler, 0, 51, 37, false, bowStack, player));

        this.addSlotToContainer(new UpgradeSlot(handler, 1, 73, 37, true, bowStack, player));
        this.addSlotToContainer(new UpgradeSlot(handler, 2, 91, 37, true, bowStack, player));
        this.addSlotToContainer(new UpgradeSlot(handler, 3, 109, 37, true, bowStack, player));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(player.inventory, k, 8 + k * 18, 142) {
                @Override
                public boolean canTakeStack(EntityPlayer playerIn) {
                    return !(getStack().getItem() instanceof BasicBow);
                }
            });
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        //UpgradeUtil.saveUpgradesToStackNEW(playerIn.getHeldItemMainhand(), (ItemStackHandler) handler);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < SIZE) {
                if (!this.mergeItemStack(itemstack1, SIZE, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, SIZE, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }
}
