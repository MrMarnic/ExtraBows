package me.marnic.extrabows.common.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Copyright (c) 03.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BasicPacket {

    public BasicPacket(FriendlyByteBuf buffer) {
    }

    void encode(FriendlyByteBuf buffer) {


    }

    void handle(Supplier<NetworkEvent.Context> context) {
    }
}