package me.marnic.extrabows.common.items.other;

import me.marnic.extrabows.api.registry.ExtraBowsRegistry;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import net.minecraft.item.Item;

/**
 * Copyright (c) 09.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ItemStrongGold extends Item {

    public ItemStrongGold() {
        super(new Settings().group(ExtraBowsObjects.CREATIVE_TAB));
        ExtraBowsRegistry.register(this,"strong_gold");
    }
}
