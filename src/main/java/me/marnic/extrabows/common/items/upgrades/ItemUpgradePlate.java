package me.marnic.extrabows.common.items.upgrades;

import me.marnic.extrabows.api.item.BasicItem;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import net.minecraft.item.Item;

/**
 * Copyright (c) 05.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ItemUpgradePlate extends Item implements BasicItem {

    public ItemUpgradePlate() {
        super(new Properties().group(ExtraBowsObjects.CREATIVE_TAB));
        createItem("upgrade_plate");
    }

    @Override
    public Item getItem() {
        return this;
    }
}
