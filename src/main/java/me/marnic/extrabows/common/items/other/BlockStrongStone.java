package me.marnic.extrabows.common.items.other;

import me.marnic.extrabows.api.block.BasicBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright (c) 09.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BlockStrongStone extends BasicBlock {

    public BlockStrongStone() {
        super("strong_stone", Properties.create(Material.ROCK).harvestLevel(0).hardnessAndResistance(2).sound(SoundType.STONE));
    }

    @Nullable
    @Override
    public ToolType getHarvestTool(BlockState state) {
        return ToolType.PICKAXE;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return Arrays.asList(new ItemStack(this.asItem()));
    }
}
