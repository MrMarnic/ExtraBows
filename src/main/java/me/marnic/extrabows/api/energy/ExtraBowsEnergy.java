package me.marnic.extrabows.api.energy;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

/**
 * Copyright (c) 05.10.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ExtraBowsEnergy extends EnergyStorage implements INBTSerializable<CompoundNBT> {

    public ExtraBowsEnergy(int capacity) {
        super(capacity);
    }

    public ExtraBowsEnergy(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public ExtraBowsEnergy(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public ExtraBowsEnergy(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return super.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("energy", this.energy);
        tag.putInt("capacity", this.capacity);
        tag.putInt("maxReceive", this.maxReceive);
        tag.putInt("maxExtract", this.maxExtract);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.energy = nbt.getInt("energy");
        this.capacity = nbt.getInt("capacity");
        this.maxReceive = nbt.getInt("maxReceive");
        this.maxExtract = nbt.getInt("maxExtract");
    }
}
