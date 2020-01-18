package me.marnic.extrabows.common.items.bows;

import net.minecraft.item.Item;

/**
 * Copyright (c) 07.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BowSettings {
    private String name;
    private int maxUses;
    private float velocityMul;
    private float damage;
    private float inaccuracy;
    private float time;
    private boolean register = true;
    private Item type;

    public BowSettings(String name, int maxUses, float velocityMul,float damage,float inaccuracy,float time) {
        this.name = name;
        this.maxUses = maxUses;
        this.velocityMul = velocityMul;
        this.damage = damage;
        this.inaccuracy = inaccuracy;
        this.time = time;
    }

    public BowSettings() {
        this.maxUses = 384;
        this.velocityMul = 3;
        this.damage = NORMAL_DAMAGE;
        this.inaccuracy = NORMAL_INACCURACY;
        this.time = NORMAL_TIME;
    }

    public BowSettings setType(Item type) {
        this.type = type;
        return this;
    }

    public static BowSettings DEFAULT = new BowSettings();

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

    public static final float NORMAL_DAMAGE = 0;
    public static final float NORMAL_INACCURACY = 1;
    public static final float NORMAL_TIME = 20f;

    public boolean isRegister() {
        return register;
    }

    public BowSettings setRegister(boolean register) {
        this.register = register;
        return this;
    }
}
