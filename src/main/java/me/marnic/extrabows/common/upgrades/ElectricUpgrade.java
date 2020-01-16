package me.marnic.extrabows.common.upgrades;

import me.marnic.extrabows.api.upgrade.ArrowModifierUpgrade;
import me.marnic.extrabows.api.upgrade.UpgradeList;
import me.marnic.extrabows.common.config.ExtraBowsConfig;
import me.marnic.extrabows.common.items.bows.ItemElectricBow;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

/**
 * Copyright (c) 04.10.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

public class ElectricUpgrade extends ArrowModifierUpgrade {
    public ElectricUpgrade() {
        super("electric_upgrade", ExtraBowsConfig.DURABILITY_ENERGY_UPGRADE);
    }

    @Override
    public void handleEntityHit(Entity entity, World world, PlayerEntity player, AbstractArrowEntity arrow, UpgradeList upgradeList) {
        if (arrow.getTags().contains("electric")) {
            arrow.remove();
        }
    }

    @Override
    public void handleBlockHit(BlockPos pos, World world, PlayerEntity player, AbstractArrowEntity arrow, UpgradeList upgradeList) {
        if (arrow.getTags().contains("electric")) {
            arrow.remove();
        }
    }

    @Override
    public void handleEntityInit(AbstractArrowEntity arrow, UpgradeList upgradeList, PlayerEntity player) {
        if (arrow.getTags().contains("electric")) {
            arrow.pickupStatus = AbstractArrowEntity.PickupStatus.DISALLOWED;
        }
    }

    @Override
    public boolean canNotBeInserted(IItemHandler itemHandler, ItemStack bow) {
        return bow.getItem() instanceof ItemElectricBow;
    }
}
