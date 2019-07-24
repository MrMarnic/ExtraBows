package me.marnic.extrabows.common.container;

import me.marnic.extrabows.api.upgrade.InventoryUtil;
import me.marnic.extrabows.api.util.UpgradeUtil;
import me.marnic.extrabows.client.gui.slot.UpgradeSlot;
import net.minecraft.container.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.PacketByteBuf;

/**
 * Copyright (c) 08.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BowUpgradeContainer extends Container {

    private Inventory handler;
    private static int SIZE = 4;

    public BowUpgradeContainer(int windowId, PlayerInventory inventory, PacketByteBuf buf) {
        super(null, windowId);

        ItemStack stack = inventory.player.getMainHandStack();

        handler = InventoryUtil.getFromStack(stack,SIZE);
        this.addSlot(new UpgradeSlot(handler, 0, 51, 37,false));

        this.addSlot(new UpgradeSlot(handler,1,73,37,true));
        this.addSlot(new UpgradeSlot(handler,2,91,37,true));
        this.addSlot(new UpgradeSlot(handler,3,109,37,true));


        int int_4;
        for(int_4 = 0; int_4 < 3; ++int_4) {
            for(int int_3 = 0; int_3 < 9; ++int_3) {
                this.addSlot(new Slot(inventory, int_3 + int_4 * 9 + 9, 8 + int_3 * 18, 84 + int_4 * 18));
            }
        }

        for(int_4 = 0; int_4 < 9; ++int_4) {
            this.addSlot(new Slot(inventory, int_4, 8 + int_4 * 18, 142));
        }
    }

    @Override
    public boolean canUse(PlayerEntity var1) {
        return true;
    }

    @Override
    public void close(PlayerEntity playerEntity_1) {
        super.close(playerEntity_1);
        InventoryUtil.saveInvToItemStack(handler,playerEntity_1.getMainHandStack(),playerEntity_1.world);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity playerEntity_1, int index) {
        Slot slot = getSlot(index);
        ItemStack stack = slot.getStack();

        if(slot != null & slot.hasStack()) {
            if(index > 3) {
                if(index >= 31) {
                    insertItem(stack,0,30,false);
                }else {
                    if(!insertItem(stack,0,3,false)) {
                        insertItem(stack,31,39,false);
                    }
                }
            }else {
                insertItem(stack,4,39,true);
                slot.onStackChanged(stack,stack.copy());
            }

            slot.onTakeItem(playerEntity_1,stack);
        }
        return ItemStack.EMPTY;
    }
}
