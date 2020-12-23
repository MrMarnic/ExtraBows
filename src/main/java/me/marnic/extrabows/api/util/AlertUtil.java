package me.marnic.extrabows.api.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Copyright (c) 07.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class AlertUtil {
    public static void alert(PlayerEntity player, String msg, TextFormatting formatting) {
        player.sendStatusMessage(new TranslationTextComponent(msg).func_230530_a_(Style.field_240709_b_.func_240712_a_(formatting)), true);
    }
}
