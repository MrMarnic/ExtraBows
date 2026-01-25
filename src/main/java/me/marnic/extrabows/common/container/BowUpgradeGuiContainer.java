package me.marnic.extrabows.common.container;

import me.marnic.extrabows.api.util.UpgradeUtil;
import me.marnic.extrabows.client.gui.slot.UpgradeSlot;
import me.marnic.extrabows.common.items.BasicBow;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

/**
 * Copyright (c) 30.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BowUpgradeGuiContainer extends AbstractContainerMenu {

    private static int SIZE = 4;
    private IItemHandler handler;

    public BowUpgradeGuiContainer(int id, Inventory inv) {
        this(id,inv,null);
    }

    public BowUpgradeGuiContainer(int windowId, Inventory inventory, ContainerData extraData) {
        super(ExtraBowsObjects.UPGRADE_INVENTORY_CONTAINER_TYPE.get(), windowId);

        Player player = inventory.player;

        ItemStack bowStack = player.getMainHandItem();

        handler = UpgradeUtil.getHandlerForItemStack(bowStack);
        this.addSlot(new UpgradeSlot(handler, 0, 51, 37, false, bowStack, player));

        this.addSlot(new UpgradeSlot(handler, 1, 73, 37, true, bowStack, player));
        this.addSlot(new UpgradeSlot(handler, 2, 91, 37, true, bowStack, player));
        this.addSlot(new UpgradeSlot(handler, 3, 109, 37, true, bowStack, player));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(player.getInventory(), j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(player.getInventory(), k, 8 + k * 18, 142) {

                @Override
                public boolean mayPickup(Player playerIn) {
                    return !(getItem().getItem() instanceof BasicBow);
                }
            });
        }
    }

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        UpgradeUtil.saveUpgradesToStack(playerIn.getMainHandItem(), (ItemStackHandler) handler);
    }

    @Override
    public boolean stillValid(Player p_75145_1_) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index < SIZE) {
                if (!this.moveItemStackTo(itemstack1, SIZE, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, SIZE, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }
}
