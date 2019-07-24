package me.marnic.extrabows.api.item;

import me.marnic.extrabows.common.main.ExtraBowsObjects;
import me.marnic.extrabows.common.registry.ExtraBowsRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

/**
 * Copyright (c) 24.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public interface BasicItem{
    default void createItem(String name) {
        getItem().setRegistryName(name);
        ExtraBowsRegistry.register(this);
    }

    default void initConfigOptions() {

    }

    Item getItem();
}
