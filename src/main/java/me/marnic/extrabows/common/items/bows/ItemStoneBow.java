package me.marnic.extrabows.common.items.bows;

import me.marnic.extrabows.common.items.BasicBow;
import me.marnic.extrabows.common.items.BowSettings;

/**
 * Copyright (c) 24.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ItemStoneBow extends BasicBow {
    public ItemStoneBow() {
        super("stone_bow");
    }

    @Override
    public void initConfigOptions() {
        setSettings(BowSettings.STONE);
        super.initConfigOptions();
    }
}
