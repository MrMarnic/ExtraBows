package me.marnic.extrabows.common.container;

import me.marnic.extrabows.api.util.UpgradeUtil;
import me.marnic.extrabows.client.gui.slot.UpgradeSlot;
import me.marnic.extrabows.common.items.BasicBow;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Copyright (c) 30.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BowUpgradeGuiContainer extends Container {

    private static int SIZE = 4;
    private IItemHandler handler;

    public BowUpgradeGuiContainer(int windowId, PlayerInventory inventory, PacketBuffer extraData) {
        super(ExtraBowsObjects.UPGRADE_INVENTORY_CONTAINER_TYPE, windowId);

        PlayerEntity player = inventory.player;

        ItemStack bowStack = player.getHeldItemMainhand();

        handler = UpgradeUtil.getHandlerForItemStack(bowStack);
        this.addSlot(new UpgradeSlot(handler, 0, 51, 37, false, bowStack, player));

        this.addSlot(new UpgradeSlot(handler, 1, 73, 37, true, bowStack, player));
        this.addSlot(new UpgradeSlot(handler, 2, 91, 37, true, bowStack, player));
        this.addSlot(new UpgradeSlot(handler, 3, 109, 37, true, bowStack, player));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(player.inventory, k, 8 + k * 18, 142) {
                @Override
                public boolean canTakeStack(PlayerEntity playerIn) {
                    return !(getStack().getItem() instanceof BasicBow);
                }
            });
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        UpgradeUtil.saveUpgradesToStack(playerIn.getHeldItemMainhand(), (ItemStackHandler) handler);
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
