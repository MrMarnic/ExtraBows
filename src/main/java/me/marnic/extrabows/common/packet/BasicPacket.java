package me.marnic.extrabows.common.packet;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Copyright (c) 03.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BasicPacket {

    public BasicPacket(PacketBuffer buffer) {
    }

    void encode(PacketBuffer buffer) {


    }

    void handle(Supplier<NetworkEvent.Context> context) {
    }
}