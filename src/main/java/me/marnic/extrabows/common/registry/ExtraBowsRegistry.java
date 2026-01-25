package me.marnic.extrabows.common.registry;

import me.marnic.extrabows.api.block.BasicBlock;
import me.marnic.extrabows.api.item.BasicItem;
import me.marnic.extrabows.api.item.ConfigLoad;
import me.marnic.extrabows.common.items.bows.ItemStoneBow;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import me.marnic.extrabows.common.main.Identification;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;

/**
 * Copyright (c) 12.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ExtraBowsRegistry {

    public static final ArrayList<ConfigLoad> CONFIG_LOAD = new ArrayList<>();

    public static void registerConfigItem(BasicItem item) {
        CONFIG_LOAD.add(item);
    }
}
