package me.marnic.extrabows.common.items.other;

import me.marnic.extrabows.api.registry.ExtraBowsRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

/**
 * Copyright (c) 09.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BlockStrongStone extends Block {

    public BlockStrongStone() {
        super(Settings.copy(Blocks.COBBLESTONE));
        ExtraBowsRegistry.register(this,"strong_stone");
    }
}
