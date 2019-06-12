package me.marnic.extrabows.common.main;

import me.marnic.extrabows.api.upgrade.Upgrades;
import me.marnic.extrabows.client.gui.ExtraBowsGUIHandler;
import me.marnic.extrabows.client.input.ExtraBowsInputHandler;
import me.marnic.extrabows.common.packet.ExtraBowsPacketHandler;
import me.marnic.extrabows.common.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * Copyright (c) 24.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
@Mod(modid = Identification.MODID,name = Identification.NAME,version = Identification.VERSION)
public class ExtraBows {


    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent e) {
        ExtraBowsObjects.initTab();
        Upgrades.init();
        ExtraBowsObjects.init();
        ExtraBowsPacketHandler.init();
    }

    @Mod.EventHandler
    public static void init(FMLInitializationEvent e) {
        proxy.handleInit();
    }

    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent e) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance,new ExtraBowsGUIHandler());
        proxy.handlePostInit();
    }

    @SidedProxy(clientSide = Identification.CLIENT_PROXY_PATH,serverSide = Identification.SERVER_PROXY_PATH)
    public static CommonProxy proxy;

    @Mod.Instance
    public static ExtraBows instance;
}
