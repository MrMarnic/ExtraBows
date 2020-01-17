package me.marnic.extrabows.common.items.other.bowUpgradeKits;

import me.marnic.extrabows.api.item.BasicItem;
import me.marnic.extrabows.common.items.CustomBowSettings;
import me.marnic.extrabows.common.main.ExtraBows;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import net.minecraft.item.Item;

/**
 * Copyright (c) 06.10.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BasicBowUpgradeKitItem extends Item implements BasicItem {

    private CustomBowSettings settings;

    public BasicBowUpgradeKitItem(String name, CustomBowSettings settings) {
        super(new Properties().group(ExtraBowsObjects.CREATIVE_TAB));
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
