package me.marnic.extrabows.client.gui;

import me.marnic.extrabows.common.main.ExtraBowsObjects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;

/**
 * Copyright (c) 08.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
@Environment(EnvType.CLIENT)
public class ExtraBowsGuiHandler {
    public static void init() {
        ScreenProviderRegistry.INSTANCE.registerFactory(ExtraBowsObjects.BOW_UPGRADE_CONTAINER_IDEN,((syncId, identifier, player, buf) -> new BowUpgradeGui(syncId,player,buf)));
    }
}
