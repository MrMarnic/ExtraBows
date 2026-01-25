package me.marnic.extrabows.api.upgrade;

import me.marnic.extrabows.api.energy.ExtraBowsEnergy;
import me.marnic.extrabows.common.items.upgrades.BasicUpgradeItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

/**
 * Copyright (c) 28.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ArrowMultiplierUpgrade extends BasicUpgrade {
    public ArrowMultiplierUpgrade(String name, RegistryObject<BasicUpgradeItem> item) {
        super(name, item);
    }

    public boolean canShoot(ItemStack arrow, Player player) {
        return true;
    }

    public void shrinkStack(ItemStack stack) {
    }

    public void removeEnergy(ItemStack stack, ExtraBowsEnergy extraBowsEnergy) {

    }
}
