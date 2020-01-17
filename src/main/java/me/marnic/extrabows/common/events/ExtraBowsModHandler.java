package me.marnic.extrabows.common.events;

import com.google.common.collect.ImmutableMap;
import me.marnic.extrabows.api.item.BasicItem;
import me.marnic.extrabows.client.gui.BowUpgradeGui;
import me.marnic.extrabows.client.input.ExtraBowsInputHandler;
import me.marnic.extrabows.common.items.BasicBow;
import me.marnic.extrabows.common.items.BowSettings;
import me.marnic.extrabows.common.items.CustomBowSettings;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import me.marnic.extrabows.common.main.Identification;
import me.marnic.extrabows.common.recipes.BasicBowRecipe;
import me.marnic.extrabows.common.recipes.BasicBowShapelessRecipe;
import me.marnic.extrabows.common.registry.ExtraBowsRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.core.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) 15.01.2020
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ExtraBowsModHandler {
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> e) {
        for (Item item : ExtraBowsRegistry.ITEMS_TO_REGISTER) {
            e.getRegistry().register(item);
        }


        //Not working completely: Needs fix onArrowLoose
        //BasicBow vanilla = (BasicBow) new BasicBow("minecraft:bow",new Item.Properties().group(ItemGroup.COMBAT),new CustomBowSettings("minecraft:bow"));
        //e.getRegistry().register(vanilla);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> e) {
        for (Block block : ExtraBowsRegistry.BLOCKS_TO_REGISTER) {
            e.getRegistry().register(block);
        }
    }

    @SubscribeEvent
    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> e) {
        for(TileEntityType type:ExtraBowsRegistry.TILE_ENTITY_TYPES_TO_REGISTER) {
            e.getRegistry().register(type);
        }
    }

    @SubscribeEvent
    public static void registerContainer(RegistryEvent.Register<ContainerType<?>> e) {
        for(ContainerType type:ExtraBowsRegistry.CONTAINER_TYPES_TO_REGISTER) {
            e.getRegistry().register(type);
        }
    }

    @SubscribeEvent
    public static void configLoad(ModConfig.Loading e) {
        BowSettings.init();

        for(BasicItem item:ExtraBowsRegistry.CONFIG_LOAD) {
            item.initConfigOptions();
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent e) {
        ExtraBowsInputHandler.registerKeys();
        ScreenManager.registerFactory(ExtraBowsObjects.UPGRADE_INVENTORY_CONTAINER_TYPE, BowUpgradeGui::new);
    }
}
