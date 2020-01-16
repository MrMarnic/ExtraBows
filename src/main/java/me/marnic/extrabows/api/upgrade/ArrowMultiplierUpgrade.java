package me.marnic.extrabows.api.upgrade;

import me.marnic.extrabows.api.energy.ExtraBowsEnergy;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * Copyright (c) 28.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ArrowMultiplierUpgrade extends BasicUpgrade {
    public ArrowMultiplierUpgrade(String name) {
        super(name);
    }

    public ArrowMultiplierUpgrade(String name, int durability) {
        super(name, durability);
    }

    public boolean canShoot(ItemStack arrow, PlayerEntity player) {
        return true;
    }

    public void shrinkStack(ItemStack stack) {
    }

    public void removeEnergy(ItemStack stack, ExtraBowsEnergy extraBowsEnergy) {

    }
}
