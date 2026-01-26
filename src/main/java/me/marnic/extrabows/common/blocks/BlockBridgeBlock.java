package me.marnic.extrabows.common.blocks;

import me.marnic.extrabows.api.util.AlertUtil;
import me.marnic.extrabows.common.blocks.tileentities.TileEntityBridgeBlock;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import org.jetbrains.annotations.Nullable;

/**
 * Copyright (c) 05.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BlockBridgeBlock extends BaseEntityBlock {
    public BlockBridgeBlock() {
        super(Properties.of().mapColor(MapColor.WOOD).strength(-1, 10000000).sound(SoundType.METAL));
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        worldIn.removeBlock(pos, false);
        if (placer instanceof Player) {
            AlertUtil.alert((Player) placer, "This block can not be placed manually!", ChatFormatting.RED);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new TileEntityBridgeBlock(p_153215_,p_153216_).setTicksToLive(1000000);
    }

    @Override
    public boolean isPathfindable(BlockState p_60475_, BlockGetter p_60476_, BlockPos p_60477_, PathComputationType p_60478_) {
        return false;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return createTickerHelper(p_153214_, ExtraBowsObjects.BRIDGE_BLOCK_TILE_ENTITY.get(), BlockBridgeBlock::tick);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TileEntityBridgeBlock tileEntityBridgeBlock) {
        if (tileEntityBridgeBlock.getTicksToLive() <= 0) {
            level.removeBlock(pos,false);
            if (tileEntityBridgeBlock.getToPlace() != null) {
                level.setBlockAndUpdate(pos, tileEntityBridgeBlock.getToPlace().defaultBlockState());
            }
        } else {
            tileEntityBridgeBlock.setTicksToLive(tileEntityBridgeBlock.getTicksToLive()-1);
        }
    }
}
