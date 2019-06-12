package me.marnic.extrabows.client.gui.slot;

import me.marnic.extrabows.api.upgrade.BasicUpgrade;
import me.marnic.extrabows.api.util.UpgradeUtil;
import me.marnic.extrabows.common.items.upgrades.BasicUpgradeItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

/**
 * Copyright (c) 31.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class UpgradeSlot extends SlotItemHandler {

    private boolean modifierSlot;

    public UpgradeSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition,boolean modifierSlot) {
        super(itemHandler, index, xPosition, yPosition);
        this.modifierSlot = modifierSlot;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
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
        for(int i = 0;i<getItemHandler().getSlots();i++) {
            if(getItemHandler().getStackInSlot(i).getItem() instanceof BasicUpgradeItem) {
                BasicUpgrade up = UpgradeUtil.getUpgradeFromStack(getItemHandler().getStackInSlot(i));
                if(up.equals(upgrade)) {
                    return true;
                }
            }
        }
        return false;
    }
}
