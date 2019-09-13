package me.marnic.extrabows.common.items;

import me.marnic.extrabows.common.config.ExtraBowsConfig;
import net.minecraft.item.IItemTier;

/**
 * Copyright (c) 24.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class CustomBowSettings {
    private String name;
    private int maxUses;
    private float velocityMul;
    private float damage;
    private float inaccuracy;
    private float time;

    public CustomBowSettings(String name, int maxUses, double velocityMul, double damage, double inaccuracy, double time) {
        this.name = name;
        this.maxUses = maxUses;
        this.velocityMul = (float) velocityMul;
        this.damage = (float) damage;
        this.inaccuracy = (float) inaccuracy;
        this.time = (float)time;
        onInit();
    }

    public CustomBowSettings(String name) {
        this.name = name;
        this.maxUses = 384;
        this.velocityMul = 3;
        this.damage = NORMAL_DAMAGE;
        this.inaccuracy = NORMAL_INACCURACY;
        this.time = NORMAL_TIME;
        onInit();
    }

    public CustomBowSettings(String name, ExtraBowsConfig.BowConfig config) {
        this(name,config.durability.get(),config.velocityMultiplier.get(),config.damage.get(),config.inaccuracy.get(),config.time.get());
    }

    public void onInit() {

    }

    public String getName() {
        return name;
    }

    public int getMaxUses() {
        return maxUses;
    }

    public float getVelocityMul() {
        return velocityMul;
    }

    public float getDamage() {
        return damage;
    }

    public float getInaccuracy() {
        return inaccuracy;
    }

    public float getTime() {
        return time;
    }

    public static final float NORMAL_DAMAGE = 0;
    public static final float NORMAL_INACCURACY = 1;
    public static final float NORMAL_TIME = 20f;
}
