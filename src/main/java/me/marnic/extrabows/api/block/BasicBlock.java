package me.marnic.extrabows.api.block;

import me.marnic.extrabows.api.item.IModelRegistry;
import me.marnic.extrabows.mod.events.ExtraBowsEventHandler;
import me.marnic.extrabows.mod.main.Identification;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;

/**
 * Copyright (c) 05.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BasicBlock extends Block implements IModelRegistry {
    public BasicBlock(String name,Material materialIn) {
        super(materialIn);
        setUnlocalizedName(name);
        setRegistryName(name);
        setResistance(10000000);
        ExtraBowsEventHandler.BLOCKS_TO_REGISTER.add(this);
        ExtraBowsEventHandler.MODELS_TO_REGISTER.add(this);
        ExtraBowsEventHandler.ITEMS_TO_REGISTER.add(new ItemBlock(this).setUnlocalizedName(name).setRegistryName(name));
    }

    @Override
    public void registerModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this),0,new ModelResourceLocation(getRegistryName(),"inventory"));
    }
}
