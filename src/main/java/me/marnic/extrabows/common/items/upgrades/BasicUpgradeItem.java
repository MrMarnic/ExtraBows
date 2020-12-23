package me.marnic.extrabows.common.items.upgrades;

import me.marnic.extrabows.api.item.BasicItem;
import me.marnic.extrabows.api.upgrade.BasicUpgrade;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
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
    private int maxDamage;

    public BasicUpgradeItem(String name, BasicUpgrade upgrade) {
        super(new Properties().group(ExtraBowsObjects.CREATIVE_TAB).maxStackSize(1));
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
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (upgrade.hasDescription()) {
            tooltip.addAll(upgrade.getDescription());
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public void setMaxDamage(int maxDamage) {
        this.maxDamage = maxDamage;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return maxDamage;
    }

    @Override
    public boolean isDamageable() {
        return maxDamage > 0;
    }
}
