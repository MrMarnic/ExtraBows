package me.marnic.extrabows.mod.packet;

import me.marnic.extrabows.mod.input.ExtraBowsInputHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBow;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Copyright (c) 31.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ExtraBowsMessageHandlerPacketSendDestroyMessage implements IMessageHandler<PacketSendDestroyMessage, IMessage> {
    @Override
    public IMessage onMessage(PacketSendDestroyMessage message, MessageContext ctx) {
        if(ctx.side== Side.CLIENT) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString("The upgrade " + new TextComponentTranslation(message.getText()).setStyle(new Style().setColor(TextFormatting.RED)).getFormattedText() + " was §cdestroyed§r while using!"));
        }
        return null;
    }
}
