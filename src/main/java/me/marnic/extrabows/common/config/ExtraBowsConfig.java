package me.marnic.extrabows.common.config;

import me.marnic.extrabows.common.items.CustomBowSettings;
import net.minecraftforge.common.ForgeConfigSpec;

/**
 * Copyright (c) 11.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ExtraBowsConfig {
    public static ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();


    public static BowConfig STONE_BOW = new BowConfig("STONE_BOW",434,3f,1f, CustomBowSettings.NORMAL_INACCURACY,18f,BUILDER);
    public static BowConfig IRON_BOW = new BowConfig("IRON_BOW",534,3.5f,2f, CustomBowSettings.NORMAL_INACCURACY,16f,BUILDER);
    public static BowConfig GOLD_BOW = new BowConfig("GOLD_BOW",300,4.25f,0, 0.5f,10f,BUILDER);
    public static BowConfig DIAMOND_BOW = new BowConfig("DIAMOND_BOW",750,3.75f,5f, CustomBowSettings.NORMAL_INACCURACY,15f,BUILDER);
    public static BowConfig EMERALD_BOW = new BowConfig("EMERALD_BOW",1500,4f,8f, CustomBowSettings.NORMAL_INACCURACY,15f,BUILDER);

    public static ForgeConfigSpec.IntValue DURABILITY_DOUBLE_UPGRADE = BUILDER.defineInRange("DURABILITY_DOUBLE_UPGRADE",400,1,Integer.MAX_VALUE);
    public static ForgeConfigSpec.IntValue DURABILITY_TRIPLE_UPGRADE = BUILDER.defineInRange("DURABILITY_TRIPLE_UPGRADE",400,1,Integer.MAX_VALUE);
    public static ForgeConfigSpec.IntValue DURABILITY_ARROW_COST_UPGRADE = BUILDER.defineInRange("DURABILITY_ARROW_COST_UPGRADE",400,1,Integer.MAX_VALUE);
    public static ForgeConfigSpec.IntValue DURABILITY_HEAL_FROM_DAMAGE_UPGRADE = BUILDER.defineInRange("DURABILITY_HEAL_FROM_DAMAGE_UPGRADE",400,1,Integer.MAX_VALUE);
    public static ForgeConfigSpec.IntValue DURABILITY_FIRE_UPGRADE = BUILDER.defineInRange("DURABILITY_FIRE_UPGRADE",400,1,Integer.MAX_VALUE);
    public static ForgeConfigSpec.IntValue DURABILITY_FREEZE_UPGRADE = BUILDER.defineInRange("DURABILITY_FREEZE_UPGRADE",400,1,Integer.MAX_VALUE);
    public static ForgeConfigSpec.IntValue DURABILITY_WATER_UPGRADE = BUILDER.defineInRange("DURABILITY_WATER_UPGRADE",400,1,Integer.MAX_VALUE);
    public static ForgeConfigSpec.IntValue DURABILITY_BRIDGE_UPGRADE = BUILDER.defineInRange("DURABILITY_BRIDGE_UPGRADE",400,1,Integer.MAX_VALUE);
    public static ForgeConfigSpec.IntValue DURABILITY_ENDER_UPGRADE = BUILDER.defineInRange("DURABILITY_ENDER_UPGRADE",400,1,Integer.MAX_VALUE);
    public static ForgeConfigSpec.IntValue DURABILITY_LIGHTNING_UPGRADE = BUILDER.defineInRange("DURABILITY_LIGHTNING_UPGRADE",400,1,Integer.MAX_VALUE);
    public static ForgeConfigSpec.IntValue DURABILITY_EXPLOSIVE_UPGRADE = BUILDER.defineInRange("DURABILITY_EXPLOSIVE_UPGRADE",400,1,Integer.MAX_VALUE);
    public static ForgeConfigSpec.IntValue DURABILITY_PUSH_UPGRADE = BUILDER.defineInRange("DURABILITY_PUSH_UPGRADE",400,1,Integer.MAX_VALUE);
    public static ForgeConfigSpec.IntValue DURABILITY_FLY_UPGRADE = BUILDER.defineInRange("DURABILITY_FLY_UPGRADE",400,1,Integer.MAX_VALUE);
    public static ForgeConfigSpec.IntValue DURABILITY_METEOR_UPGRADE = BUILDER.defineInRange("DURABILITY_METEOR_UPGRADE",400,1,Integer.MAX_VALUE);
    public static ForgeConfigSpec.IntValue DURABILITY_ENERGY_UPGRADE = BUILDER.defineInRange("DURABILITY_ENERGY_UPGRADE",400,1,Integer.MAX_VALUE);

    public static ForgeConfigSpec SPEC = BUILDER.build();

    public static class BowConfig{

        public ForgeConfigSpec.ConfigValue<Integer> durability;
        public ForgeConfigSpec.DoubleValue velocityMultiplier;
        public ForgeConfigSpec.DoubleValue inaccuracy;
        public ForgeConfigSpec.DoubleValue time;
        public ForgeConfigSpec.DoubleValue damage;

        public BowConfig(String name,int durability,float velocityMultiplier,float damage,float inaccuracy,float time,ForgeConfigSpec.Builder builder){

            builder.push(name);

            this.durability = builder.define("durability",durability);
            this.velocityMultiplier = builder.defineInRange("velocityMultiplier",velocityMultiplier,0,Double.MAX_VALUE);
            this.inaccuracy = builder.defineInRange("inaccuracy",inaccuracy,0,Double.MAX_VALUE);
            this.time = builder.defineInRange("time",time,0,Double.MAX_VALUE);
            this.damage = builder.defineInRange("damage",damage,0,Double.MAX_VALUE);
            builder.pop();
        }
    }
}