package me.marnic.extrabows.common.items.other.bowUpgradeKits;

import me.marnic.extrabows.api.item.BasicItem;
import me.marnic.extrabows.common.items.CustomBowSettings;
import net.minecraft.item.Item;

/**
 * Copyright (c) 06.10.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BasicBowUpgradeKitItem extends Item implements BasicItem {

    private CustomBowSettings settings;

    public BasicBowUpgradeKitItem(String name, CustomBowSettings settings) {
        createItem(name);
        this.settings = settings;
    }

    public CustomBowSettings getSettings() {
        return settings;
    }

    @Override
    public Item getItem() {
        return this;
    }
}
