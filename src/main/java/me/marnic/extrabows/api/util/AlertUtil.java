package me.marnic.extrabows.api.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;

/**
 * Copyright (c) 07.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class AlertUtil {
    public static void alert(Player player, String msg, ChatFormatting formatting) {
        player.displayClientMessage(new TranslatableComponent(msg).withStyle(Style.EMPTY.applyFormat(formatting)), true);
    }
}
