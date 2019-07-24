package me.marnic.extrabows.common.items.upgrades;

import me.marnic.extrabows.api.registry.ExtraBowsRegistry;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import net.minecraft.item.Item;

/**
 * Copyright (c) 05.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ItemUpgradePlate extends Item {

    public ItemUpgradePlate() {
        super(new Settings().group(ExtraBowsObjects.CREATIVE_TAB));
        ExtraBowsRegistry.register(this,"upgrade_plate");
    }
}
