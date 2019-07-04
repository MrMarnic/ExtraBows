package me.marnic.extrabows.common.container;

import me.marnic.extrabows.api.util.UpgradeUtil;
import me.marnic.extrabows.client.gui.slot.*;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.*;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

/**
 * Copyright (c) 30.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BowUpgradeGuiContainer extends Container {

    private IItemHandler handler;
    private static int SIZE = 4;

    public BowUpgradeGuiContainer(int windowId, PlayerInventory inventory, PacketBuffer extraData) {
        super(ExtraBowsObjects.UPGRADE_INVENTORY_CONTAINER_TYPE, windowId);
        ItemStack stack = inventory.player.getHeldItemMainhand();

        handler = UpgradeUtil.getHandlerForItemStackNEW(stack);
        this.addSlot(new UpgradeSlot(handler, 0, 51, 37,false));

        this.addSlot(new UpgradeSlot(handler,1,73,37,true));
        this.addSlot(new UpgradeSlot(handler,2,91,37,true));
        this.addSlot(new UpgradeSlot(handler,3,109,37,true));

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        UpgradeUtil.saveUpgradesToStackNEW(playerIn.getHeldItemMainhand(),(ItemStackHandler)handler);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
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
