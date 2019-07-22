package me.marnic.extrabows.client.input;

import me.marnic.extrabows.common.main.ExtraBows;
import me.marnic.extrabows.common.main.Identification;
import me.marnic.extrabows.common.packet.ExtraBowsPacketHandler;
import me.marnic.extrabows.common.packet.PacketOpenUpgradeGUI;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

/**
 * Copyright (c) 30.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ExtraBowsInputHandler {

    public static KeyBinding OPEN_UPGRADE_INV;

    public static void registerKeys() {
        OPEN_UPGRADE_INV = new KeyBinding("Key to open upgrade inventory", Keyboard.KEY_B, "key." + Identification.MODID + ".category");
        ClientRegistry.registerKeyBinding(OPEN_UPGRADE_INV);
    }

    public static void handleKeyPressedEvent(InputEvent.KeyInputEvent e) {
        if (OPEN_UPGRADE_INV.isKeyDown()) {
            handleUpgradeInvKeyPressed();
        }
    }

    public static void handleUpgradeInvKeyPressed() {
        ExtraBowsPacketHandler.INSTANCE.sendToServer(new PacketOpenUpgradeGUI());
    }

    public static void handleUpgradeInvKeyPressedServer(EntityPlayerMP playerMP) {
        playerMP.openGui(ExtraBows.instance, 0, playerMP.world, 0, 0, 0);
    }
}
