package me.marnic.extrabows.api.item;

import me.marnic.extrabows.common.registry.ExtraBowsRegistry;
import net.minecraft.world.item.Item;

/**
 * Copyright (c) 24.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public interface BasicItem extends ConfigLoad {
    default void createItem(String name) {
        ExtraBowsRegistry.registerConfigItem(this);
    }

    Item getItem();
}
