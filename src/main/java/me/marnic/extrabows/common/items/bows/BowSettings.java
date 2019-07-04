package me.marnic.extrabows.common.items.bows;

import me.marnic.extrabows.common.config.ExtraBowsConfig;
import me.marnic.extrabows.common.items.CustomBowSettings;

/**
 * Copyright (c) 03.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BowSettings {

    public static CustomBowSettings IRON_BOW;
    public static CustomBowSettings GOLDEN_BOW;
    public static CustomBowSettings DIAMOND_BOW;
    public static CustomBowSettings STONE_BOW;
    public static CustomBowSettings EMERALD_BOW;

    public static void init() {
        STONE_BOW = new CustomBowSettings("stone_bow", ExtraBowsConfig.STONE_BOW);
        IRON_BOW = new CustomBowSettings("iron_bow", ExtraBowsConfig.IRON_BOW);
        GOLDEN_BOW = new CustomBowSettings("golden_bow", ExtraBowsConfig.GOLD_BOW);
        DIAMOND_BOW = new CustomBowSettings("diamond_bow", ExtraBowsConfig.DIAMOND_BOW);
        EMERALD_BOW = new CustomBowSettings("emerald_bow", ExtraBowsConfig.EMERALD_BOW);
    }
}
