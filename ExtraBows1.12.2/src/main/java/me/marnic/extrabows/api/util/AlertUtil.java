package me.marnic.extrabows.api.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

/**
 * Copyright (c) 07.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class AlertUtil {
    public static void alert(EntityPlayer player, String msg,TextFormatting formatting) {
        player.sendStatusMessage(new TextComponentString(msg).setStyle(new Style().setColor(formatting)),true);
    }
}
