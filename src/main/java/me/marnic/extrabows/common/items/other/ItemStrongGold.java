package me.marnic.extrabows.common.items.other;

import me.marnic.extrabows.api.item.BasicItem;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import net.minecraft.item.Item;

/**
 * Copyright (c) 09.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ItemStrongGold extends Item implements BasicItem {

    public ItemStrongGold() {
        super(new Properties().group(ExtraBowsObjects.CREATIVE_TAB));
        createItem("strong_gold");
    }

    @Override
    public Item getItem() {
        return this;
    }
}
