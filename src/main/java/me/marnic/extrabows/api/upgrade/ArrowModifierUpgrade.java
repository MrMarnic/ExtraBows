package me.marnic.extrabows.api.upgrade;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
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

    public void handleBlockHit(BlockPos pos, World world, PlayerEntity player, ArrowEntity arrow, UpgradeList upgradeList) {

    }

    public void handleWaterHit(BlockPos pos, World world, PlayerEntity player, ArrowEntity arrow, UpgradeList upgradeList) {

    }

    public void handleEntityHit(Entity entity, World world, PlayerEntity player, ArrowEntity arrow, UpgradeList upgradeList) {

    }

    public void handleArrowCreate(ArrowEntity arrow, PlayerEntity player, UpgradeList upgradeList) {

    }

    public void handleSetEffect(ArrowEntity arrow, UpgradeList upgradeList) {

    }

    public void handleFlyingEvent(ArrowEntity arrow, World world, UpgradeList upgradeList) {
    }

    public void handleEntityInit(ArrowEntity arrow, UpgradeList upgradeList, PlayerEntity player) {
    }

    public enum EventType {
        BLOCK_HIT,ENTITY_HIT,ARROW_CREATE,SET_EFFECT,WATER_HIT,ENTITY_INIT
    }
}
