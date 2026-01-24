package me.marnic.extrabows.api.block;

import me.marnic.extrabows.common.registry.ExtraBowsRegistry;
import net.minecraft.block.Block;

/**
 * Copyright (c) 05.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BasicBlock extends Block {
    public BasicBlock(String name, Properties properties) {
        super(properties);
        setRegistryName(name);
        ExtraBowsRegistry.register(this);
    }
}
