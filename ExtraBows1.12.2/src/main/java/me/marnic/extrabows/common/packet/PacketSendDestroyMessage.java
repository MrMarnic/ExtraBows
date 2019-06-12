package me.marnic.extrabows.common.packet;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * Copyright (c) 11.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class PacketSendDestroyMessage implements IMessage {

    private String text;

    public PacketSendDestroyMessage() {
    }

    public PacketSendDestroyMessage setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.text = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf,text);
    }

    public String getText() {
        return text;
    }
}
