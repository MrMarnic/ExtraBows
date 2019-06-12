package me.marnic.extrabows.common.registry;

import me.marnic.extrabows.api.block.BasicBlock;
import me.marnic.extrabows.api.item.BasicItem;
import me.marnic.extrabows.api.item.IModelRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import java.util.ArrayList;

/**
 * Copyright (c) 12.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ExtraBowsRegistry {
    public static final ArrayList<Item> ITEMS_TO_REGISTER = new ArrayList<>();
    public static final ArrayList<Block> BLOCKS_TO_REGISTER = new ArrayList<>();
    public static final ArrayList<IModelRegistry> MODELS_TO_REGISTER = new ArrayList<>();

    public static void register(BasicItem item) {
        ITEMS_TO_REGISTER.add(item.getItem());
        MODELS_TO_REGISTER.add(item);
    }

    public static void register(BasicBlock block) {
        BLOCKS_TO_REGISTER.add(block);
        MODELS_TO_REGISTER.add(block);
        ITEMS_TO_REGISTER.add(new ItemBlock(block).setUnlocalizedName(block.getUnlocalizedName()).setRegistryName(block.getRegistryName()));
    }
}
