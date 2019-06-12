package me.marnic.extrabows.common.items;

import me.marnic.extrabows.common.config.ExtraBowsConfig;
import net.minecraft.potion.PotionEffect;

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

    private PotionEffect effect;

    public CustomBowSettings(String name, int maxUses, float velocityMul,float damage,float inaccuracy,float time) {
        this.name = name;
        this.maxUses = maxUses;
        this.velocityMul = velocityMul;
        this.damage = damage;
        this.inaccuracy = inaccuracy;
        this.time = time;
        onInit();
    }

    public void setEffect(PotionEffect effect) {
        this.effect = effect;
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
        this(name,config.durability,config.velocityMultiplier,config.damage,config.inaccuracy,config.time);
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

    public PotionEffect getEffect() {
        return effect;
    }

    public boolean hasEffect() {
        return effect!=null;
    }

    public static final float NORMAL_DAMAGE = 0;
    public static final float NORMAL_INACCURACY = 1;
    public static final float NORMAL_TIME = 20f;
}
