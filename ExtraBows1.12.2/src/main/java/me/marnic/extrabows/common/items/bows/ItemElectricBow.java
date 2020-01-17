package me.marnic.extrabows.common.items.bows;

import me.marnic.extrabows.api.energy.ExtraBowsEnergy;
import me.marnic.extrabows.common.config.ExtraBowsConfig;
import me.marnic.extrabows.common.items.BasicBow;
import me.marnic.extrabows.common.items.BowSettings;
import me.marnic.extrabows.common.items.CustomBowSettings;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Copyright (c) 04.10.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ItemElectricBow extends BasicBow {

    public ItemElectricBow() {
        super(new CustomBowSettings("electric_bow").copy(BowSettings.IRON));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new BowCapability(new ExtraBowsEnergy(CustomBowSettings.ENERGY_BOW, CustomBowSettings.ENERGY_RECEIVE, CustomBowSettings.ENERGY_COST_PER_ARROW * 3),stack);
    }

    @Override
    public void onArrowCreate(EntityArrow arrow) {
        if (arrow.getTags().contains("electric")) {
            arrow.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
        }
    }

    @Override
    public void onArrowHit(EntityArrow arrow) {
        if (arrow.getTags().contains("electric")) {
            arrow.setDead();
        }
    }
}
