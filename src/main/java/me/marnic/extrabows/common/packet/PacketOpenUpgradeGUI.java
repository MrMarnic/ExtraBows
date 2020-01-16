package me.marnic.extrabows.common.packet;

import me.marnic.extrabows.client.input.ExtraBowsInputHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Copyright (c) 31.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class PacketOpenUpgradeGUI extends BasicPacket {
    public PacketOpenUpgradeGUI(PacketBuffer buffer) {
        super(buffer);
    }

    @Override
    void handle(Supplier<NetworkEvent.Context> context) {
        if(context.get().getDirection() == NetworkDirection.PLAY_TO_SERVER) {
            ServerPlayerEntity serverPlayer = context.get().getSender();

            if(serverPlayer.getHeldItemMainhand().getItem() instanceof BowItem) {
                serverPlayer.getServer().enqueue(new TickDelayedTask(0, () -> ExtraBowsInputHandler.handleUpgradeInvKeyPressedServer(serverPlayer)));
            }
        }
    }
}