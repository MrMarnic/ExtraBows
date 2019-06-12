package me.marnic.extrabows.mod.items.other;

import me.marnic.extrabows.api.item.BasicItem;
import me.marnic.extrabows.mod.main.ExtraBowsObjects;
import net.minecraft.item.Item;

/**
 * Copyright (c) 09.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ItemStrongDiamond extends Item implements BasicItem {

    public ItemStrongDiamond() {
        createItem("strong_diamond");
        setCreativeTab(ExtraBowsObjects.CREATIVE_TAB);
    }

    @Override
    public Item getItem() {
        return this;
    }
}
