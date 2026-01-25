package me.marnic.extrabows.client.input;

import me.marnic.extrabows.common.main.Identification;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Identification.MODID, value = Dist.CLIENT)
public class ExtraBowsClientEvents {

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent e) {
        e.register(ExtraBowsInputHandler.OPEN_UPGRADE_INV);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void keyInput(InputEvent.Key e) {
        ExtraBowsInputHandler.handleKeyPressedEvent(e);
    }
}
