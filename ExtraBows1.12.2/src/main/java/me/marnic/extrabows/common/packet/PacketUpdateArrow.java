package me.marnic.extrabows.common.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 25.09.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class PacketUpdateArrow implements IMessage {

    private EntityArrow arrow;
    private int arrowId;
    private List<String> tags;

    public PacketUpdateArrow() {
        this.tags = new ArrayList<>();
    }

    public PacketUpdateArrow(EntityArrow arrow) {
        this.arrow = arrow;
        this.tags = new ArrayList<>();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        this.arrowId = buffer.readVarInt();
        int size = buffer.readVarInt();

        for (int i = 0; i < size; i++) {
            tags.add(buffer.readString(256));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        int size = arrow.getTags().size();

        buffer.writeVarInt(arrow.getEntityId());
        buffer.writeVarInt(size);
        for (String tag : arrow.getTags()) {
            buffer.writeString(tag);
        }
    }

    public int getArrowId() {
        return arrowId;
    }

    public List<String> getTags() {
        return tags;
    }
}
