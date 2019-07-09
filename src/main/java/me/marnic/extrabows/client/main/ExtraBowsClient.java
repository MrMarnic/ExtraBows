package me.marnic.extrabows.client.main;

import me.marnic.extrabows.client.gui.ExtraBowsGuiHandler;
import me.marnic.extrabows.client.input.ExtraBowsInputHandler;
import me.marnic.extrabows.common.packet.ExtraBowsPacketHandler;
import net.fabricmc.api.ClientModInitializer;

/**
 * Copyright (c) 09.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ExtraBowsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ExtraBowsInputHandler.init();
        ExtraBowsPacketHandler.initClient();
        ExtraBowsGuiHandler.init();
    }
}
