package me.marnic.extrabows.api.registry;

import me.marnic.extrabows.api.util.IdentificationUtil;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;

/**
 * Copyright (c) 07.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ExtraBowsRegistry {
    public static ArrayList<Registrable<Item>> ITEMS_TO_REGISTER = new ArrayList<>();
    public static ArrayList<Registrable<Block>> BLOCKS_TO_REGISTER = new ArrayList<>();

    public static void register(Item item,String name) {
        ITEMS_TO_REGISTER.add(new Registrable<>(name,item));
    }

    public static void register(Block block,String name) {
        BLOCKS_TO_REGISTER.add(new Registrable<>(name,block));
        ITEMS_TO_REGISTER.add(new Registrable<>(name,new BlockItem(block,new Item.Settings().group(ExtraBowsObjects.CREATIVE_TAB))));
    }

    public static void registerAll() {
        for(Registrable<Item> item:ITEMS_TO_REGISTER) {
            Registry.register(Registry.ITEM, item.getIdentifier(),item.getObject());
        }
        for(Registrable<Block> block:BLOCKS_TO_REGISTER) {
            Registry.register(Registry.BLOCK,block.getIdentifier(),block.getObject());
        }
    }
    
    private static class Registrable<T> {
        private String name;
        private T object;

        public Registrable(String name, T object) {
            this.name = name;
            this.object = object;
        }

        public String getRegistryName() {
            return name;
        }

        public T getObject() {
            return object;
        }

        public Identifier getIdentifier() {
            return IdentificationUtil.fromString(name);
        }
    }
}
