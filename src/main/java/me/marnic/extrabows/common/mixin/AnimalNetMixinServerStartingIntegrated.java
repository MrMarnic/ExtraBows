package me.marnic.extrabows.common.mixin;

import com.google.gson.JsonElement;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.datafixers.DataFixer;
import me.marnic.extrabows.api.util.StartingUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.UserCache;
import net.minecraft.world.level.LevelGeneratorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.net.Proxy;

/**
 * Copyright (c) 18.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
@Mixin(IntegratedServer.class)
public abstract class AnimalNetMixinServerStartingIntegrated extends MinecraftServer {

    public AnimalNetMixinServerStartingIntegrated(File file_1, Proxy proxy_1, DataFixer dataFixer_1, CommandManager commandManager_1, YggdrasilAuthenticationService yggdrasilAuthenticationService_1, MinecraftSessionService minecraftSessionService_1, GameProfileRepository gameProfileRepository_1, UserCache userCache_1, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory_1, String string_1) {
        super(file_1, proxy_1, dataFixer_1, commandManager_1, yggdrasilAuthenticationService_1, minecraftSessionService_1, gameProfileRepository_1, userCache_1, worldGenerationProgressListenerFactory_1, string_1);
    }

    @Inject(method = "loadWorld",at = @At("RETURN"))
    public void loadWorld(String string_1, String string_2, long long_1, LevelGeneratorType levelGeneratorType_1, JsonElement jsonElement_1, CallbackInfo info) {
        StartingUtil.handleServerStart(this);
    }
}
