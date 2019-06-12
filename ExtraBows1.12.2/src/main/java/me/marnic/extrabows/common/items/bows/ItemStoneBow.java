package me.marnic.extrabows.common.items.bows;

import me.marnic.extrabows.common.config.ExtraBowsConfig;
import me.marnic.extrabows.common.items.BasicBow;
import me.marnic.extrabows.common.items.CustomBowSettings;

/**
 * Copyright (c) 24.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ItemStoneBow extends BasicBow {
    public ItemStoneBow() {
        super(new CustomBowSettings("stone_bow", ExtraBowsConfig.STONE_BOW));
    }
}
