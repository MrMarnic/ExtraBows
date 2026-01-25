package me.marnic.extrabows.common.packet;

import me.marnic.extrabows.client.input.ExtraBowsInputHandler;
import me.marnic.extrabows.common.items.BasicBow;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Copyright (c) 31.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class PacketOpenUpgradeGUI extends BasicPacket {
    public PacketOpenUpgradeGUI(FriendlyByteBuf buffer) {
        super(buffer);
    }

    @Override
    void handle(Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection() == NetworkDirection.PLAY_TO_SERVER) {
            ServerPlayer serverPlayer = context.get().getSender();

            if (serverPlayer.getMainHandItem().getItem() instanceof BasicBow) {
                //serverPlayer.getServer().addTickable(new TickDelayedTask(0, () -> ExtraBowsInputHandler.handleUpgradeInvKeyPressedServer(serverPlayer)));
                ExtraBowsInputHandler.handleUpgradeInvKeyPressedServer(serverPlayer);
            }
        }
    }
}