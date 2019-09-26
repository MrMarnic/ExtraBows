package me.marnic.extrabows.common.packet;

import me.marnic.extrabows.client.input.ExtraBowsInputHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBow;
import net.minecraft.world.World;
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
        Minecraft.getMinecraft().addScheduledTask(()-> {
            World world = Minecraft.getMinecraft().world;
            world.getEntityByID(message.getArrowId()).getTags().addAll(message.getTags());
        });
        return null;
    }
}
