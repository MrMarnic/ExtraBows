package me.marnic.extrabows.common.proxy;

import me.marnic.extrabows.client.input.ExtraBowsInputHandler;
import me.marnic.extrabows.common.packet.PacketUpdateArrow;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Copyright (c) 30.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void handleInit() {
        ExtraBowsInputHandler.registerKeys();
    }

    @Override
    public void handleArrow(PacketUpdateArrow message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            World world = Minecraft.getMinecraft().world;
            world.getEntityByID(message.getArrowId()).getTags().addAll(message.getTags());
        });
    }

    @Override
    public Side getSide() {
        return Side.CLIENT;
    }
}
