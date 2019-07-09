package me.marnic.extrabows.common.packet;

import me.marnic.extrabows.api.util.IdentificationUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.util.Identifier;

/**
 * Copyright (c) 08.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ExtraBowsPacketHandler {

    public static Identifier PACKET_OPEN_UPGRADE_GUI = IdentificationUtil.fromString("packet_open_upgrade_gui");

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        ClientSidePacketRegistry.INSTANCE.register(PACKET_OPEN_UPGRADE_GUI,new PacketOpenUpgradeGUIConsumer());
        System.out.println("CLIENT");
    }

    public static void init() {
        ServerSidePacketRegistry.INSTANCE.register(PACKET_OPEN_UPGRADE_GUI,new PacketOpenUpgradeGUIConsumer());
        System.out.println("SERVER");
    }
}