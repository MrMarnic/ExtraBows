package me.marnic.extrabows.common.items.bows;

import me.marnic.extrabows.common.items.BasicBow;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

/**
 * Copyright (c) 24.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ItemStoneBow extends BasicBow {
    public ItemStoneBow() {
        super(ExtraBowsObjects.AllBowSettings.STONE_BOW);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world_1, PlayerEntity playerEntity_1, Hand hand_1) {
        return super.use(world_1, playerEntity_1, hand_1);
    }
}