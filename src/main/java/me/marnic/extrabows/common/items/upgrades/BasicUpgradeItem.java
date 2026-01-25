package me.marnic.extrabows.common.items.upgrades;

import me.marnic.extrabows.api.item.BasicItem;
import me.marnic.extrabows.api.upgrade.BasicUpgrade;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Copyright (c) 28.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BasicUpgradeItem extends Item implements BasicItem {

    private BasicUpgrade upgrade;
    private int maxDamage;

    public BasicUpgradeItem(String name, BasicUpgrade upgrade) {
        super(new Properties().stacksTo(1));
        createItem(name);
        this.upgrade = upgrade;
    }

    public BasicUpgrade getUpgrade() {
        return upgrade;
    }

    @Override
    public Item getItem() {
        return this;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (upgrade.hasDescription()) {
            tooltip.addAll(upgrade.getDescription());
        }
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    public void setMaxDamage(int maxDamage) {
        this.maxDamage = maxDamage;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return maxDamage;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return maxDamage > 0;
    }
}
