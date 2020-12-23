package me.marnic.extrabows.common.events;

import me.marnic.extrabows.api.item.ConfigLoad;
import me.marnic.extrabows.client.gui.BowUpgradeGui;
import me.marnic.extrabows.client.input.ExtraBowsInputHandler;
import me.marnic.extrabows.common.items.BowSettings;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import me.marnic.extrabows.common.registry.ExtraBowsRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

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
        for (TileEntityType type : ExtraBowsRegistry.TILE_ENTITY_TYPES_TO_REGISTER) {
            e.getRegistry().register(type);
        }
    }

    @SubscribeEvent
    public static void registerContainer(RegistryEvent.Register<ContainerType<?>> e) {
        for (ContainerType type : ExtraBowsRegistry.CONTAINER_TYPES_TO_REGISTER) {
            e.getRegistry().register(type);
        }
    }

    @SubscribeEvent
    public static void configLoad(ModConfig.Loading e) {
        BowSettings.init();
        for (ConfigLoad item : ExtraBowsRegistry.CONFIG_LOAD) {
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
