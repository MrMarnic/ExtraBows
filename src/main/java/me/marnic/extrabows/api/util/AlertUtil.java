package me.marnic.extrabows.api.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Copyright (c) 07.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class AlertUtil {
    public static void alert(PlayerEntity player, String msg, TextFormatting formatting) {
        player.sendStatusMessage(new TranslationTextComponent(msg).setStyle(new Style().setColor(formatting)), true);
    }
}
