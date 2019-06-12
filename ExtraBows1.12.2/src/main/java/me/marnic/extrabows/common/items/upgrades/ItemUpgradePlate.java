package me.marnic.extrabows.common.items.upgrades;

import me.marnic.extrabows.api.item.BasicItem;
import net.minecraft.item.Item;

/**
 * Copyright (c) 05.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ItemUpgradePlate extends Item implements BasicItem {

    public ItemUpgradePlate() {
        createItem("upgrade_plate");
    }

    @Override
    public Item getItem() {
        return this;
    }
}
