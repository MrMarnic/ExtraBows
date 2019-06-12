package me.marnic.extrabows.mod.config;

import me.marnic.extrabows.mod.items.CustomBowSettings;
import me.marnic.extrabows.mod.main.Identification;
import net.minecraftforge.common.config.Config;

/**
 * Copyright (c) 11.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

@Config(modid = Identification.MODID)
public class ExtraBowsConfig {
    public static BowConfig STONE_BOW = new BowConfig(434,3f,1f, CustomBowSettings.NORMAL_INACCURACY,18f);
    public static BowConfig IRON_BOW = new BowConfig(534,3.5f,2f, CustomBowSettings.NORMAL_INACCURACY,16f);
    public static BowConfig GOLD_BOW = new BowConfig(300,4.25f,0, 0.5f,10f);
    public static BowConfig DIAMOND_BOW = new BowConfig(750,3.75f,5f, CustomBowSettings.NORMAL_INACCURACY,15f);
    public static BowConfig EMERALD_BOW = new BowConfig(1500,4f,8f, CustomBowSettings.NORMAL_INACCURACY,15f);

    public static int DURABILITY_DOUBLE_UPGRADE = 400;
    public static int DURABILITY_TRIPLE_UPGRADE = 400;
    public static int DURABILITY_ARROW_COST_UPGRADE = 400;
    public static int DURABILITY_HEAL_FROM_DAMAGE_UPGRADE = 400;
    public static int DURABILITY_FIRE_UPGRADE = 400;
    public static int DURABILITY_FREEZE_UPGRADE = 400;
    public static int DURABILITY_WATER_UPGRADE = 400;
    public static int DURABILITY_BRIDGE_UPGRADE = 400;
    public static int DURABILITY_ENDER_UPGRADE = 400;
    public static int DURABILITY_LIGHTNING_UPGRADE = 400;
    public static int DURABILITY_EXPLOSIVE_UPGRADE = 400;
        
    
    public static class BowConfig {

        public int durability;
        public float velocityMultiplier;
        public float inaccuracy;
        public float time;
        public float damage;

        public BowConfig(int durability,float velocityMultiplier,float damage,float inaccuracy,float time) {
            this.durability = durability;
            this.velocityMultiplier = velocityMultiplier;
            this.inaccuracy = inaccuracy;
            this.time = time;
            this.damage = damage;
        }
    }
}
