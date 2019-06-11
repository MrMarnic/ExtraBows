package me.marnic.extrabows.mod.items.bows;

import me.marnic.extrabows.api.upgrade.Upgrades;
import me.marnic.extrabows.mod.config.ExtraBowsConfig;
import me.marnic.extrabows.mod.items.BasicBow;
import me.marnic.extrabows.mod.items.CustomBowSettings;
import net.minecraft.item.ItemBow;

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
