package me.marnic.extrabows.common.upgrades;

import me.marnic.extrabows.api.upgrade.ArrowModifierUpgrade;
import me.marnic.extrabows.api.upgrade.UpgradeList;
import me.marnic.extrabows.api.upgrade.Upgrades;
import me.marnic.extrabows.common.config.ExtraBowsConfig;
import me.marnic.extrabows.common.items.bows.ItemElectricBow;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;

/**
 * Copyright (c) 04.10.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

public class ElectricUpgrade extends ArrowModifierUpgrade {
    public ElectricUpgrade() {
        super("electric_upgrade", Upgrades.ELECTRIC_UPGRADE_ITEM);
    }

    @Override
    public void initConfigOptions() {
        getItem().setMaxDamage(ExtraBowsConfig.DURABILITY_ENERGY_UPGRADE.get());
    }

    @Override
    public void handleEntityHit(Entity entity, Level Level, Player player, AbstractArrow arrow, UpgradeList upgradeList) {
        if (arrow.getTags().contains("electric")) {
            arrow.remove(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    public void handleBlockHit(BlockPos pos, Level Level, Player player, AbstractArrow arrow, UpgradeList upgradeList) {
        if (arrow.getTags().contains("electric")) {
            arrow.remove(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    public void handleEntityInit(AbstractArrow arrow, UpgradeList upgradeList, Player player) {
        if (arrow.getTags().contains("electric")) {
            arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
        }
    }

    @Override
    public boolean canNotBeInserted(IItemHandler itemHandler, ItemStack bow) {
        return bow.getItem() instanceof ItemElectricBow;
    }
}
