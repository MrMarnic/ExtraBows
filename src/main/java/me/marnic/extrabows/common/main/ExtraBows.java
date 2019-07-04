package me.marnic.extrabows.common.main;

import me.marnic.extrabows.api.upgrade.Upgrades;
import me.marnic.extrabows.common.config.ExtraBowsConfig;
import me.marnic.extrabows.common.events.ExtraBowsEventHandler;
import me.marnic.extrabows.common.packet.ExtraBowsPacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

/**
 * Copyright (c) 24.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
@Mod(Identification.MODID)
public class ExtraBows {


    public ExtraBows() {
        MinecraftForge.EVENT_BUS.register(new ExtraBowsEventHandler());

        ExtraBowsObjects.initTab();
        Upgrades.init();
        ExtraBowsObjects.init();
        ExtraBowsPacketHandler.init();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON,ExtraBowsConfig.SPEC);
    }
}
