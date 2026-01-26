package me.marnic.extrabows.common.blocks.tileentities;

import me.marnic.extrabows.common.main.ExtraBowsObjects;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Copyright (c) 06.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class TileEntityBridgeBlock extends BlockEntity {

    private int ticksToLive;
    private Block toPlace;

    public TileEntityBridgeBlock(BlockPos pos, BlockState state) {
        super(ExtraBowsObjects.BRIDGE_BLOCK_TILE_ENTITY.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag p_187471_) {
        p_187471_.putInt("ticksToLive", ticksToLive);
        super.saveAdditional(p_187471_);
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        ticksToLive = nbt.getInt("ticksToLive");
        super.deserializeNBT(nbt);
    }

    public TileEntityBridgeBlock setTicksToLive(int ticksToLive) {
        this.ticksToLive = ticksToLive;
        return this;
    }

    public void setFluid(Block state) {
        this.toPlace = state;
    }

    public int getTicksToLive() {
        return ticksToLive;
    }

    public Block getToPlace() {
        return toPlace;
    }
}
