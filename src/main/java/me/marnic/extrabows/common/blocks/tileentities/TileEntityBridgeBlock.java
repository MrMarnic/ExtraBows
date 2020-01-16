package me.marnic.extrabows.common.blocks.tileentities;

import me.marnic.extrabows.common.main.ExtraBowsObjects;
import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * Copyright (c) 06.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class TileEntityBridgeBlock extends TileEntity implements ITickableTileEntity {

    private int ticksToLive;
    private Block toPlace;

    public TileEntityBridgeBlock() {
        super(ExtraBowsObjects.BRIDGE_BLOCK_TILE_ENTITY);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("ticksToLive", ticksToLive);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        ticksToLive = compound.getInt("ticksToLive");
        super.read(compound);
    }

    public TileEntityBridgeBlock setTicksToLive(int ticksToLive) {
        this.ticksToLive = ticksToLive;
        return this;
    }

    @Override
    public void tick() {
        if (ticksToLive <= 0) {
            world.removeBlock(pos,false);
            if (toPlace != null) {
                world.setBlockState(pos, toPlace.getDefaultState());
            }
        } else {
            ticksToLive--;
        }
    }

    public void setFluid(Block state) {
        this.toPlace = state;
    }
}
