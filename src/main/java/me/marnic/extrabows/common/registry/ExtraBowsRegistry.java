package me.marnic.extrabows.common.registry;

import me.marnic.extrabows.api.block.BasicBlock;
import me.marnic.extrabows.api.item.BasicItem;
import me.marnic.extrabows.api.item.ConfigLoad;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;

import java.util.ArrayList;

/**
 * Copyright (c) 12.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ExtraBowsRegistry {
    public static final ArrayList<Item> ITEMS_TO_REGISTER = new ArrayList<>();
    public static final ArrayList<Block> BLOCKS_TO_REGISTER = new ArrayList<>();
    public static final ArrayList<TileEntityType> TILE_ENTITY_TYPES_TO_REGISTER = new ArrayList<>();
    public static final ArrayList<ContainerType> CONTAINER_TYPES_TO_REGISTER = new ArrayList<>();
    public static final ArrayList<ConfigLoad> CONFIG_LOAD = new ArrayList<>();

    public static void register(BasicItem item) {
        ITEMS_TO_REGISTER.add(item.getItem());
        CONFIG_LOAD.add(item);
    }

    public static void register(BasicBlock block) {
        BLOCKS_TO_REGISTER.add(block);
        ITEMS_TO_REGISTER.add(new BlockItem(block, new Item.Properties().group(ExtraBowsObjects.CREATIVE_TAB)).setRegistryName(block.getRegistryName()));
    }

    public static void register(TileEntityType type) {
        TILE_ENTITY_TYPES_TO_REGISTER.add(type);
    }

    public static void register(ContainerType type) {
        CONTAINER_TYPES_TO_REGISTER.add(type);
    }
}
