package me.marnic.extrabows.api.item;

import me.marnic.extrabows.mod.events.ExtraBowsEventHandler;
import me.marnic.extrabows.mod.main.ExtraBowsObjects;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

/**
 * Copyright (c) 24.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public interface BasicItem extends IModelRegistry{
    default void createItem(String name) {
        getItem().setRegistryName(name);
        getItem().setUnlocalizedName(name);
        ExtraBowsEventHandler.ITEMS_TO_REGISTER.add(getItem());
        ExtraBowsEventHandler.MODELS_TO_REGISTER.add(this);
        getItem().setCreativeTab(ExtraBowsObjects.CREATIVE_TAB);
    }

    Item getItem();

    @Override
    default void registerModel() {
        ModelLoader.setCustomModelResourceLocation(getItem(),0,new ModelResourceLocation(getItem().getRegistryName(),"inventory"));
    }
}
