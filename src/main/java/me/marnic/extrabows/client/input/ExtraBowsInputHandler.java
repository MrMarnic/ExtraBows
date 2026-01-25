package me.marnic.extrabows.client.input;

import me.marnic.extrabows.common.container.BowUpgradeGuiContainer;
import me.marnic.extrabows.common.main.Identification;
import me.marnic.extrabows.common.packet.ExtraBowsPacketHandler;
import me.marnic.extrabows.common.packet.PacketOpenUpgradeGUI;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

/**
 * Copyright (c) 30.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ExtraBowsInputHandler {

    public static KeyMapping OPEN_UPGRADE_INV;

    public static void registerKeys() {
        OPEN_UPGRADE_INV = new KeyMapping("Key to open upgrade inventory", GLFW.GLFW_KEY_B, "key." + Identification.MODID + ".category");
        ClientRegistry.registerKeyBinding(OPEN_UPGRADE_INV);
    }

    public static void handleKeyPressedEvent(InputEvent.KeyInputEvent e) {
        if (OPEN_UPGRADE_INV.consumeClick()) {
            handleUpgradeInvKeyPressed();
        }
    }

    public static void handleUpgradeInvKeyPressed() {
        ExtraBowsPacketHandler.INSTANCE.sendToServer(new PacketOpenUpgradeGUI(null));
    }

    public static void handleUpgradeInvKeyPressedServer(ServerPlayer playerMP) {
        NetworkHooks.openGui(playerMP, new SimpleMenuProvider(new MenuConstructor() {
            @Override
            public @Nullable AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
                return new BowUpgradeGuiContainer(p_39954_, p_39955_, null);
            }
        },new TextComponent("Upgrade Inventory")));
    }
}
