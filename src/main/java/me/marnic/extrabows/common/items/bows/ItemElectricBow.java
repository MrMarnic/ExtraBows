package me.marnic.extrabows.common.items.bows;

import me.marnic.extrabows.api.energy.ExtraBowsEnergy;
import me.marnic.extrabows.common.items.BasicBow;
import me.marnic.extrabows.common.items.BowSettings;
import me.marnic.extrabows.common.items.CustomBowSettings;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

/**
 * Copyright (c) 04.10.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ItemElectricBow extends BasicBow {

    public ItemElectricBow() {
        super("electric_bow");
    }

    @Override
    public void initConfigOptions() {
        setSettings(BowSettings.ELECTRIC);
        super.initConfigOptions();
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new BowCapability(new ExtraBowsEnergy(CustomBowSettings.ENERGY_BOW, CustomBowSettings.ENERGY_RECEIVE, CustomBowSettings.ENERGY_COST_PER_ARROW * 3), stack);
    }

    @Override
    public void onArrowCreate(AbstractArrow arrow) {
        if (arrow.getTags().contains("electric")) {
            arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
        }
    }

    @Override
    public void onArrowHit(AbstractArrow arrow) {
        if (arrow.getTags().contains("electric")) {
            arrow.remove(Entity.RemovalReason.DISCARDED);
        }
    }
}
