package me.marnic.extrabows.client.input;

import io.netty.buffer.Unpooled;
import me.marnic.extrabows.api.util.IdentificationUtil;
import me.marnic.extrabows.common.main.Identification;
import me.marnic.extrabows.common.packet.ExtraBowsPacketHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import org.lwjgl.glfw.GLFW;

/**
 * Copyright (c) 08.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
@Environment(EnvType.CLIENT)
public class ExtraBowsInputHandler {
    public static FabricKeyBinding KEY_BINDING;

    public static void init() {
        KeyBindingRegistry.INSTANCE.addCategory("Extra Bows Keys");
        KEY_BINDING = FabricKeyBinding.Builder.create(IdentificationUtil.fromString("category"), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B,"Extra Bows Keys").build();
        KeyBindingRegistry.INSTANCE.register(KEY_BINDING);

        ClientTickCallback.EVENT.register((e) -> {
            if(KEY_BINDING.isPressed()) {
                handleUpgradeInvKeyPressed();
            }
        });


    }

    public static void handleUpgradeInvKeyPressed() {
        ClientSidePacketRegistry.INSTANCE.sendToServer(ExtraBowsPacketHandler.PACKET_OPEN_UPGRADE_GUI,new PacketByteBuf(Unpooled.buffer()));
    }
}
