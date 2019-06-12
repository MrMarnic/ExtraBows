package me.marnic.extrabows.mod.items.bows;

import me.marnic.extrabows.mod.config.ExtraBowsConfig;
import me.marnic.extrabows.mod.items.BasicBow;
import me.marnic.extrabows.mod.items.CustomBowSettings;

/**
 * Copyright (c) 24.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ItemDiamondBow extends BasicBow {
    public ItemDiamondBow() {
        super(new CustomBowSettings("diamond_bow", ExtraBowsConfig.DIAMOND_BOW));
    }
}
