package me.marnic.extrabows.common.packet;

import io.netty.buffer.Unpooled;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.network.PacketConsumer;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.item.BowItem;
import net.minecraft.util.PacketByteBuf;

/**
 * Copyright (c) 08.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class PacketOpenUpgradeGUIConsumer implements PacketConsumer {

    public PacketOpenUpgradeGUIConsumer() {
    }

    @Override
    public void accept(PacketContext context, PacketByteBuf buffer) {
        if(context.getPacketEnvironment()== EnvType.SERVER) {
            if(context.getPlayer().getMainHandStack().getItem() instanceof BowItem) {
                ContainerProviderRegistry.INSTANCE.openContainer(ExtraBowsObjects.BOW_UPGRADE_CONTAINER_IDEN,context.getPlayer(),(p) -> new PacketByteBuf(Unpooled.buffer()));
            }
        }
    }
}
