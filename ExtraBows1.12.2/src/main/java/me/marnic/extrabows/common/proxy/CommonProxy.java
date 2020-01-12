package me.marnic.extrabows.common.proxy;

import me.marnic.extrabows.common.packet.PacketUpdateArrow;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Copyright (c) 11.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class CommonProxy {
    public void handleInit() {

    }

    public void handlePostInit() {
    }

    public void handleArrow(PacketUpdateArrow message, MessageContext ctx) {

    }

    public Side getSide() {
        return Side.SERVER;
    }
}
