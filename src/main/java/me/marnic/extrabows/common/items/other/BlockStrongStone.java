package me.marnic.extrabows.common.items.other;

import me.marnic.extrabows.api.block.BasicBlock;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;

import java.util.Arrays;
import java.util.List;

/**
 * Copyright (c) 09.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BlockStrongStone extends BasicBlock {

    public BlockStrongStone() {
        super("strong_stone", Properties.of().mapColor(MapColor.STONE).strength(2f,6f).sound(SoundType.STONE));
    }

    @Override
    public List<ItemStack> getDrops(BlockState p_287732_, LootParams.Builder p_287596_) {
        return Arrays.asList(new ItemStack(this.asItem()));
    }
}
