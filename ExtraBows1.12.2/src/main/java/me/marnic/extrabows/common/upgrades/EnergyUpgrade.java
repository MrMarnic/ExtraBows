package me.marnic.extrabows.common.upgrades;

import me.marnic.extrabows.api.energy.ExtraBowsEnergy;
import me.marnic.extrabows.api.upgrade.ArrowModifierUpgrade;
import me.marnic.extrabows.api.upgrade.UpgradeList;
import me.marnic.extrabows.common.config.ExtraBowsConfig;
import me.marnic.extrabows.common.items.bows.ItemEnergyBow;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.IItemHandler;

/**
 * Copyright (c) 04.10.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

public class EnergyUpgrade extends ArrowModifierUpgrade {
    public EnergyUpgrade() {
        super("energy_upgrade", ExtraBowsConfig.DURABILITY_ENERGY_UPGRADE);
    }

    @Override
    public void handleUpgradeInsert(ItemStack bow) {
        ExtraBowsEnergy extraBowsEnergy = (ExtraBowsEnergy) bow.getCapability(CapabilityEnergy.ENERGY, null);
    }

    @Override
    public void handleEntityHit(Entity entity, World world, EntityPlayer player, EntityArrow arrow, UpgradeList upgradeList) {
        if (arrow.getTags().contains("electric")) {
            arrow.setDead();
        }
    }

    @Override
    public void handleBlockHit(BlockPos pos, World world, EntityPlayer player, EntityArrow arrow, UpgradeList upgradeList) {
        if (arrow.getTags().contains("electric")) {
            arrow.setDead();
        }
    }

    @Override
    public void handleEntityInit(EntityArrow arrow, UpgradeList upgradeList, EntityPlayer player) {
        if (arrow.getTags().contains("electric")) {
            arrow.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
        }
    }

    @Override
    public boolean canNotBeInserted(IItemHandler itemHandler, ItemStack bow) {
        return bow.getItem() instanceof ItemEnergyBow;
    }
}
