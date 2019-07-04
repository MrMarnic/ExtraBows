package me.marnic.extrabows.common.packet;

import me.marnic.extrabows.common.main.Identification;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

/**
 * Copyright (c) 31.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ExtraBowsPacketHandler {
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Identification.MODID,"main_channel"), () -> "1", (s) -> true,(s) -> true);

    public static void init() {
        INSTANCE.registerMessage(0,PacketOpenUpgradeGUI.class,PacketOpenUpgradeGUI::encode,PacketOpenUpgradeGUI::new,PacketOpenUpgradeGUI::handle);
    }
}
