package me.marnic.extrabows.api.upgrade;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
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

    public void handleBlockHit(BlockPos pos, World world, PlayerEntity player, AbstractArrowEntity arrow, UpgradeList upgradeList) {

    }

    public void handleWaterHit(BlockPos pos, World world, PlayerEntity player, AbstractArrowEntity arrow, UpgradeList upgradeList) {

    }

    public void handleEntityHit(Entity entity, World world, PlayerEntity player, AbstractArrowEntity arrow, UpgradeList upgradeList) {

    }

    public void handleArrowCreate(AbstractArrowEntity arrow, PlayerEntity player, UpgradeList upgradeList) {

    }

    public void handleSetEffect(AbstractArrowEntity arrow, UpgradeList upgradeList) {

    }

    public void handleFlyingEvent(AbstractArrowEntity arrow, World world, UpgradeList upgradeList) {
    }

    public void handleEntityInit(AbstractArrowEntity arrow, UpgradeList upgradeList, PlayerEntity player) {
    }

    public void handleUpgradeInsert(ItemStack bow) {
    }

    public enum EventType {
        BLOCK_HIT, ENTITY_HIT, ARROW_CREATE, SET_EFFECT, WATER_HIT, ENTITY_INIT
    }
}
