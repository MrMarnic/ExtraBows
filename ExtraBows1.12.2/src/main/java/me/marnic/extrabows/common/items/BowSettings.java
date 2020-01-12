package me.marnic.extrabows.common.items;

import me.marnic.extrabows.common.config.ExtraBowsConfig;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

/**
 * Copyright (c) 05.10.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BowSettings {
    public static CustomBowSettings STONE;
    public static CustomBowSettings IRON;
    public static CustomBowSettings GOLD;
    public static CustomBowSettings DIAMOND;
    public static CustomBowSettings EMERALD;

    public static void init() {
        STONE = new CustomBowSettings("stone_bow", ExtraBowsConfig.STONE_BOW).setType(Item.getItemFromBlock(Blocks.COBBLESTONE));
        IRON = new CustomBowSettings("iron_bow", ExtraBowsConfig.IRON_BOW).setType(Items.IRON_INGOT);
        GOLD = new CustomBowSettings("golden_bow", ExtraBowsConfig.GOLD_BOW).setType(Items.GOLD_INGOT);
        DIAMOND = new CustomBowSettings("diamond_bow", ExtraBowsConfig.DIAMOND_BOW).setType(Items.DIAMOND);
        EMERALD = new CustomBowSettings("emerald_bow", ExtraBowsConfig.EMERALD_BOW).setType(Items.EMERALD);
    }
}
