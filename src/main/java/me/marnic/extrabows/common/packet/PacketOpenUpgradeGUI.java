package me.marnic.extrabows.common.packet;

import me.marnic.extrabows.client.input.ExtraBowsInputHandler;
import me.marnic.extrabows.common.container.BowUpgradeGuiContainer;
import me.marnic.extrabows.common.items.BasicBow;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

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
                handleUpgradeInvKeyPressedServer(serverPlayer);
            }
        }
    }

    public static void handleUpgradeInvKeyPressedServer(ServerPlayer playerMP) {
        NetworkHooks.openScreen(playerMP, new SimpleMenuProvider(new MenuConstructor() {
            @Override
            public @Nullable AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
                return new BowUpgradeGuiContainer(p_39954_, p_39955_, null);
            }
        }, Component.literal("Upgrade Inventory")));
    }
}