package me.marnic.extrabows.common.blockentities;

import me.marnic.extrabows.common.main.ExtraBowsObjects;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;

/**
 * Copyright (c) 07.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BlockEntityBridgeBlock extends BlockEntity implements Tickable {

    private int ticksToLive;

    public BlockEntityBridgeBlock() {
        super(ExtraBowsObjects.BRIDGE_BLOCK_TYPE);
    }



    @Override
    public CompoundTag toTag(CompoundTag compound) {
        compound.putInt("ticksToLive",ticksToLive);
        return super.toTag(compound);
    }

    @Override
    public void fromTag(CompoundTag compound) {
        //ticksToLive = compound.getInt("ticksToLive");
        super.fromTag(compound);
    }

    public BlockEntityBridgeBlock setTicksToLive(int ticksToLive) {
        this.ticksToLive = ticksToLive;
        return this;
    }

    @Override
    public void tick() {
        if(ticksToLive<=0) {
            world.clearBlockState(pos,false);
        }else {
            ticksToLive--;
        }
    }

    public int getTicksToLive() {
        return ticksToLive;
    }
}
