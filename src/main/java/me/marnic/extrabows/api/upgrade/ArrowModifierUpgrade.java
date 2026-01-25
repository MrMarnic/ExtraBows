package me.marnic.extrabows.api.upgrade;

import me.marnic.extrabows.common.items.upgrades.BasicUpgradeItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;

/**
 * Copyright (c) 29.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ArrowModifierUpgrade extends BasicUpgrade {
    public ArrowModifierUpgrade(String name, RegistryObject<BasicUpgradeItem> item) {
        super(name,item);
    }

    public void handleBlockHit(BlockPos pos, Level Level, Player player, AbstractArrow arrow, UpgradeList upgradeList) {

    }

    public void handleWaterHit(BlockPos pos, Level Level, Player player, AbstractArrow arrow, UpgradeList upgradeList) {

    }

    public void handleEntityHit(Entity entity, Level Level, Player player, AbstractArrow arrow, UpgradeList upgradeList) {

    }

    public void handleArrowCreate(AbstractArrow arrow, Player player, UpgradeList upgradeList) {

    }

    public void handleSetEffect(AbstractArrow arrow, UpgradeList upgradeList) {

    }

    public void handleFlyingEvent(AbstractArrow arrow, Level Level, UpgradeList upgradeList) {
    }

    public void handleEntityInit(AbstractArrow arrow, UpgradeList upgradeList, Player player) {
    }

    public void handleUpgradeInsert(ItemStack bow) {
    }

    public enum EventType {
        BLOCK_HIT, ENTITY_HIT, ARROW_CREATE, SET_EFFECT, WATER_HIT, ENTITY_INIT
    }
}
