package me.marnic.extrabows.api.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

/**
 * Copyright (c) 07.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class AlertUtil {
    public static void alert(PlayerEntity player, String msg, Formatting formatting) {
        player.addChatMessage(new TranslatableText(msg).setStyle(new Style().setColor(formatting)),true);
    }
}
