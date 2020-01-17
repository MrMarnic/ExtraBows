package me.marnic.extrabows.common.blocks;

import me.marnic.extrabows.api.block.BasicBlock;
import me.marnic.extrabows.api.util.AlertUtil;
import me.marnic.extrabows.common.blocks.tileentities.TileEntityBridgeBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Copyright (c) 05.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BlockBridgeBlock extends BasicBlock {
    public BlockBridgeBlock() {
        super("bridge_block", Properties.create(Material.IRON).hardnessAndResistance(-1,10000000).sound(SoundType.METAL));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityBridgeBlock().setTicksToLive(1000000);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        worldIn.removeBlock(pos,false);
        if (placer instanceof PlayerEntity) {
            AlertUtil.alert((PlayerEntity) placer, "This block can not be placed manually!", TextFormatting.RED);
        }
    }

    @Override
    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }
}
