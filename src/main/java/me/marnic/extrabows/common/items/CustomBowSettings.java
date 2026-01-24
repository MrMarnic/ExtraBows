package me.marnic.extrabows.common.items;

import me.marnic.extrabows.common.config.ExtraBowsConfig;
import net.minecraft.item.Item;

/**
 * Copyright (c) 24.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class CustomBowSettings {
    public static final float NORMAL_DAMAGE = 0;
    public static final float NORMAL_INACCURACY = 1;
    public static final float NORMAL_TIME = 20f;
    public static final int ENERGY_COST_PER_ARROW = 100;
    public static final int ENERGY_BOW = 40000;
    public static final int ENERGY_BOW_UPGRADE = (int) (ENERGY_BOW * 0.75);
    public static final int ENERGY_RECEIVE = 50;
    private String name;
    private int maxUses;
    private float velocityMul;
    private float damage;
    private float inaccuracy;
    private float time;
    private Item type;

    public CustomBowSettings(String name, int maxUses, float velocityMul, float damage, float inaccuracy, float time) {
        this.name = name;
        this.maxUses = maxUses;
        this.velocityMul = velocityMul;
        this.damage = damage;
        this.inaccuracy = inaccuracy;
        this.time = time;
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
        this(name, config.durability.get(), new Float(config.velocityMultiplier.get()), new Float(config.damage.get()), new Float(config.inaccuracy.get()), new Float(config.time.get()));
    }

    public CustomBowSettings copy(CustomBowSettings settings) {
        this.maxUses = settings.maxUses;
        this.velocityMul = settings.velocityMul;
        this.damage = settings.damage;
        this.inaccuracy = settings.inaccuracy;
        this.time = settings.time;
        return this;
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

    public Item getType() {
        return type;
    }

    public CustomBowSettings setType(Item type) {
        this.type = type;
        return this;
    }
}
