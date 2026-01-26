package me.marnic.extrabows.common.registry;

import me.marnic.extrabows.api.item.BasicItem;
import me.marnic.extrabows.api.item.ConfigLoad;

import java.util.ArrayList;

/**
 * Copyright (c) 12.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ExtraBowsRegistry {

    public static final ArrayList<ConfigLoad> CONFIG_LOAD = new ArrayList<>();

    public static void registerConfigItem(BasicItem item) {
        CONFIG_LOAD.add(item);
    }
}
