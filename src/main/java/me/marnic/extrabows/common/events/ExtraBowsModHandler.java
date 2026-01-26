package me.marnic.extrabows.common.events;

import me.marnic.extrabows.api.item.ConfigLoad;
import me.marnic.extrabows.client.gui.BowUpgradeGui;
import me.marnic.extrabows.common.items.BowSettings;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import me.marnic.extrabows.common.registry.ExtraBowsRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Copyright (c) 15.01.2020
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ExtraBowsModHandler {

    @SubscribeEvent
    public static void configLoad(ModConfigEvent.Loading e) {
        BowSettings.init();
        for (ConfigLoad item : ExtraBowsRegistry.CONFIG_LOAD) {
            item.initConfigOptions();
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent e) {
        e.enqueueWork(
                // Assume RegistryObject<MenuType<MyMenu>> MY_MENU
                // Assume MyContainerScreen<MyMenu> which takes in three parameters
                () -> MenuScreens.register(ExtraBowsObjects.UPGRADE_INVENTORY_CONTAINER_TYPE.get(), BowUpgradeGui::new)
        );
    }
}
