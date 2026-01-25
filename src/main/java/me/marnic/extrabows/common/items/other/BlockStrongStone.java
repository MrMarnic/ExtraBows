package me.marnic.extrabows.common.items.other;

import me.marnic.extrabows.api.block.BasicBlock;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

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
        super("strong_stone", Properties.of(Material.STONE).strength(2f,6f).sound(SoundType.STONE));
    }

    @Override
    public @NotNull List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return Arrays.asList(new ItemStack(this.asItem()));
    }
}
