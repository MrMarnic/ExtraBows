package me.marnic.extrabows.mod.packet;

import me.marnic.extrabows.mod.main.Identification;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Copyright (c) 31.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ExtraBowsPacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Identification.MODID);

    public static void init() {
        INSTANCE.registerMessage(ExtraBowsMessageHandlerPacketOpenGUI.class,PacketOpenUpgradeGUI.class,0, Side.SERVER);
        INSTANCE.registerMessage(ExtraBowsMessageHandlerPacketSendDestroyMessage.class,PacketSendDestroyMessage.class,1,Side.CLIENT);
    }
}
