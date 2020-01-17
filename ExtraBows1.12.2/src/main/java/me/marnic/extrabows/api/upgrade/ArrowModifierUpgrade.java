package me.marnic.extrabows.api.upgrade;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Copyright (c) 29.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ArrowModifierUpgrade extends BasicUpgrade {
    public ArrowModifierUpgrade(String name) {
        super(name);
    }

    public ArrowModifierUpgrade(String name, int durability) {
        super(name, durability);
    }

    public void handleBlockHit(BlockPos pos, World world, EntityPlayer player, EntityArrow arrow, UpgradeList upgradeList) {

    }

    public void handleWaterHit(BlockPos pos, World world, EntityPlayer player, EntityArrow arrow, UpgradeList upgradeList) {

    }

    public void handleEntityHit(Entity entity, World world, EntityPlayer player, EntityArrow arrow, UpgradeList upgradeList) {

    }

    public void handleArrowCreate(EntityArrow arrow, EntityPlayer player, UpgradeList upgradeList) {

    }

    public void handleSetEffect(EntityArrow arrow, UpgradeList upgradeList) {

    }

    public void handleFlyingEvent(EntityArrow arrow, World world, UpgradeList upgradeList) {
    }

    public void handleEntityInit(EntityArrow arrow, UpgradeList upgradeList, EntityPlayer player) {
    }

    public void handleUpgradeInsert(ItemStack bow) {
    }

    public enum EventType {
        BLOCK_HIT, ENTITY_HIT, ARROW_CREATE, SET_EFFECT, WATER_HIT, ENTITY_INIT
    }
}
