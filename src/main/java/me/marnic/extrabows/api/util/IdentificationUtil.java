package me.marnic.extrabows.api.util;

import me.marnic.extrabows.common.main.Identification;
import net.minecraft.util.Identifier;

/**
 * Copyright (c) 08.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class IdentificationUtil {
    public static Identifier fromString(String name) {
        return new Identifier(Identification.MODID,name);
    }
}
