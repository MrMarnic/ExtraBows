package me.marnic.extrabows.common.items.bows;

import me.marnic.extrabows.api.energy.ExtraBowsEnergy;
import me.marnic.extrabows.common.config.ExtraBowsConfig;
import me.marnic.extrabows.common.items.BasicBow;
import me.marnic.extrabows.common.items.BowSettings;
import me.marnic.extrabows.common.items.CustomBowSettings;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
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
        super("electric_bow");
    }

    @Override
    public void initConfigOptions() {
        setSettings(BowSettings.ELECTRIC);
        super.initConfigOptions();
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new BowCapability(new ExtraBowsEnergy(CustomBowSettings.ENERGY_BOW, CustomBowSettings.ENERGY_RECEIVE, CustomBowSettings.ENERGY_COST_PER_ARROW * 3),stack);
    }

    @Override
    public void onArrowCreate(AbstractArrowEntity arrow) {
        if (arrow.getTags().contains("electric")) {
            arrow.pickupStatus = AbstractArrowEntity.PickupStatus.DISALLOWED;
        }
    }

    @Override
    public void onArrowHit(AbstractArrowEntity arrow) {
        if (arrow.getTags().contains("electric")) {
            arrow.remove();
        }
    }
}
