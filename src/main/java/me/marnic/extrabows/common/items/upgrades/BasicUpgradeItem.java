package me.marnic.extrabows.common.items.upgrades;

import me.marnic.extrabows.api.registry.ExtraBowsRegistry;
import me.marnic.extrabows.api.upgrade.BasicUpgrade;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

/**
 * Copyright (c) 28.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BasicUpgradeItem extends Item {

    private BasicUpgrade upgrade;

    public BasicUpgradeItem(String name, Settings properties, BasicUpgrade upgrade) {
        super(properties.group(ExtraBowsObjects.CREATIVE_TAB));
        this.upgrade = upgrade;
        ExtraBowsRegistry.register(this,name);
    }

    public BasicUpgrade getUpgrade() {
        return upgrade;
    }

    @Override
    public void appendTooltip(ItemStack itemStack_1, World world_1, List<Text> list_1, TooltipContext tooltipContext_1) {
        if(upgrade.hasDescription()) {
            list_1.addAll(upgrade.getDescription());
        }
        super.appendTooltip(itemStack_1, world_1, list_1, tooltipContext_1);
    }
}
