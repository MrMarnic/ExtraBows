package me.marnic.extrabows.mod.items.bows;

import me.marnic.extrabows.mod.config.ExtraBowsConfig;
import me.marnic.extrabows.mod.items.BasicBow;
import me.marnic.extrabows.mod.items.CustomBowSettings;

/**
 * Copyright (c) 24.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ItemIronBow extends BasicBow {
    public ItemIronBow() {
        super(new CustomBowSettings("iron_bow", ExtraBowsConfig.IRON_BOW));
    }
}
