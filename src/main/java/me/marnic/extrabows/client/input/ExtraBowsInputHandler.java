package me.marnic.extrabows.client.input;

import me.marnic.extrabows.common.main.Identification;
import me.marnic.extrabows.common.packet.ExtraBowsPacketHandler;
import me.marnic.extrabows.common.packet.PacketOpenUpgradeGUI;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.InputEvent;
import org.lwjgl.glfw.GLFW;

/**
 * Copyright (c) 30.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ExtraBowsInputHandler {

    public static KeyMapping OPEN_UPGRADE_INV = new KeyMapping("Key to open upgrade inventory", GLFW.GLFW_KEY_B, "key." + Identification.MODID + ".category");

    public static void handleKeyPressedEvent(InputEvent.Key e) {
        if (OPEN_UPGRADE_INV.consumeClick()) {
            handleUpgradeInvKeyPressed();
        }
    }

    public static void handleUpgradeInvKeyPressed() {
        ExtraBowsPacketHandler.INSTANCE.sendToServer(new PacketOpenUpgradeGUI(null));
    }
}
