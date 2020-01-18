package me.marnic.extrabows.api.upgrade;

import net.minecraft.item.ItemStack;

/**
 * Copyright (c) 07.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ExtraBowsUtil {
    public static void shrinkStack(ItemStack stack,int amount) {
        stack.setCount(stack.getCount()-amount);
    }
}
