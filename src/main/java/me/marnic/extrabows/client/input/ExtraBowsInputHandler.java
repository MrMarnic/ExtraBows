package me.marnic.extrabows.client.input;

import me.marnic.extrabows.common.container.BowUpgradeGuiContainer;
import me.marnic.extrabows.common.main.Identification;
import me.marnic.extrabows.common.packet.ExtraBowsPacketHandler;
import me.marnic.extrabows.common.packet.PacketOpenUpgradeGUI;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.network.NetworkHooks;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;

/**
 * Copyright (c) 30.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ExtraBowsInputHandler {

    public static KeyBinding OPEN_UPGRADE_INV;

    public static void registerKeys() {
        OPEN_UPGRADE_INV = new KeyBinding("Key to open upgrade inventory", GLFW.GLFW_KEY_B, "key." + Identification.MODID + ".category");
        ClientRegistry.registerKeyBinding(OPEN_UPGRADE_INV);
    }

    public static void handleKeyPressedEvent(InputEvent.KeyInputEvent e) {
        if (OPEN_UPGRADE_INV.isKeyDown()) {
            handleUpgradeInvKeyPressed();
        }
    }

    public static void handleUpgradeInvKeyPressed() {
        ExtraBowsPacketHandler.INSTANCE.sendToServer(new PacketOpenUpgradeGUI(null));
    }

    public static void handleUpgradeInvKeyPressedServer(ServerPlayerEntity playerMP) {
        NetworkHooks.openGui(playerMP, new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new StringTextComponent("Upgrade Inventory");
            }

            @Nullable
            @Override
            public Container createMenu(int windowId, PlayerInventory inventory, PlayerEntity player) {
                return new BowUpgradeGuiContainer(windowId, inventory, null);
            }
        });
    }
}
