package me.marnic.extrabows.client.gui.slot;

import me.marnic.extrabows.api.upgrade.BasicUpgrade;
import me.marnic.extrabows.api.util.UpgradeUtil;
import me.marnic.extrabows.common.items.upgrades.BasicUpgradeItem;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

/**
 * Copyright (c) 31.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class UpgradeSlot extends Slot {

    private boolean modifierSlot;

    public UpgradeSlot(Inventory itemHandler, int index, int xPosition, int yPosition, boolean modifierSlot) {
        super(itemHandler, index, xPosition, yPosition);
        this.modifierSlot = modifierSlot;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        if(modifierSlot) {
            if(!doesContainSameUpgrade(UpgradeUtil.getUpgradeFromStack(stack))) {
                return UpgradeUtil.isModifierUpgrade(stack);
            }
            return false;
        }else {
            return UpgradeUtil.isMultiplierUpgrade(stack);
        }
    }

    private boolean doesContainSameUpgrade(BasicUpgrade upgrade) {
        for(int i = 0;i<inventory.getInvSize();i++) {
            if(inventory.getInvStack(i).getItem() instanceof BasicUpgradeItem) {
                BasicUpgrade up = UpgradeUtil.getUpgradeFromStack(inventory.getInvStack(i));
                if(up.equals(upgrade)) {
                    return true;
                }
            }
        }
        return false;
    }
}
