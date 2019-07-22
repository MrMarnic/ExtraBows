package me.marnic.extrabows.common.items.other;

import me.marnic.extrabows.api.block.BasicBlock;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * Copyright (c) 09.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BlockStrongStone extends BasicBlock {

    public BlockStrongStone() {
        super("strong_stone", Material.ROCK);
        setCreativeTab(ExtraBowsObjects.CREATIVE_TAB);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 0);
        setHardness(2);
    }
}
