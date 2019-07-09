package me.marnic.extrabows.common.main;

import me.marnic.extrabows.api.registry.ExtraBowsRegistry;
import me.marnic.extrabows.api.util.TimerUtil;
import me.marnic.extrabows.client.gui.ExtraBowsGuiHandler;
import me.marnic.extrabows.client.input.ExtraBowsInputHandler;
import me.marnic.extrabows.common.packet.ExtraBowsPacketHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.minecraft.server.world.ServerTickScheduler;

/**
 * Copyright (c) 07.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ExtraBows implements ModInitializer {
    @Override
    public void onInitialize() {
        ExtraBowsObjects.init();
        ExtraBowsRegistry.registerAll();

        ServerTickCallback.EVENT.register((world) -> {
            TimerUtil.handleTickEvent();
        });

        ExtraBowsPacketHandler.init();
    }
}
