package me.marnic.extrabows.common.blocks.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

/**
 * Copyright (c) 06.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class TileEntityBridgeBlock extends TileEntity implements ITickable {

    private int ticksToLive;

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("ticksToLive",ticksToLive);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        ticksToLive = compound.getInteger("ticksToLive");
        super.readFromNBT(compound);
    }

    public TileEntityBridgeBlock setTicksToLive(int ticksToLive) {
        this.ticksToLive = ticksToLive;
        return this;
    }

    @Override
    public void update() {
        if(ticksToLive<=0) {
            world.setBlockToAir(pos);
        }else {
            ticksToLive--;
        }
    }
}
