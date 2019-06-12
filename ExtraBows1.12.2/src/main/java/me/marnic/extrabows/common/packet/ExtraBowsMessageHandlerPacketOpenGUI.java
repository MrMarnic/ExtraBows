package me.marnic.extrabows.common.packet;

import me.marnic.extrabows.client.input.ExtraBowsInputHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBow;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Copyright (c) 31.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ExtraBowsMessageHandlerPacketOpenGUI implements IMessageHandler<PacketOpenUpgradeGUI, IMessage> {
    @Override
    public IMessage onMessage(PacketOpenUpgradeGUI message, MessageContext ctx) {
        EntityPlayerMP serverPlayer = ctx.getServerHandler().player;

        if(serverPlayer.getHeldItemMainhand().getItem() instanceof ItemBow) {
            serverPlayer.getServerWorld().addScheduledTask(() -> {
                ExtraBowsInputHandler.handleUpgradeInvKeyPressedServer(serverPlayer);
            });
        }
        return null;
    }
}
