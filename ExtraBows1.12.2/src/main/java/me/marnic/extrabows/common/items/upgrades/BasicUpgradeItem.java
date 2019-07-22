package me.marnic.extrabows.common.items.upgrades;

import me.marnic.extrabows.api.item.BasicItem;
import me.marnic.extrabows.api.upgrade.BasicUpgrade;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Copyright (c) 28.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BasicUpgradeItem extends Item implements BasicItem {

    private BasicUpgrade upgrade;

    public BasicUpgradeItem(String name, BasicUpgrade upgrade) {
        createItem(name);
        this.upgrade = upgrade;
        setMaxStackSize(1);
    }

    public BasicUpgrade getUpgrade() {
        return upgrade;
    }

    @Override
    public Item getItem() {
        return this;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (upgrade.hasDescription()) {
            tooltip.addAll(upgrade.getDescription());
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
