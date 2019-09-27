package me.marnic.extrabows.common.packet;

import me.marnic.extrabows.common.main.ExtraBows;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Copyright (c) 31.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

public class ExtraBowsMessageHandlerPacketUpdateArrow implements IMessageHandler<PacketUpdateArrow, IMessage> {

    @Override
    public IMessage onMessage(PacketUpdateArrow message, MessageContext ctx) {
        ExtraBows.proxy.handleArrow(message,ctx);
        return null;
    }
}
