package me.marnic.extrabows.api.util;

import java.util.Random;

/**
 * Copyright (c) 31.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class RandomUtil {
    public static final Random RANDOM = new Random();

    public static boolean isChance(int max,int number) {
        return RANDOM.nextInt(max)==number;
    }
}
