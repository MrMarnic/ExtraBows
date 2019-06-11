package me.marnic.extrabows.mod.items.other;

import me.marnic.extrabows.api.block.BasicBlock;
import me.marnic.extrabows.api.item.BasicItem;
import me.marnic.extrabows.mod.main.ExtraBowsObjects;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

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
        setHarvestLevel("pickaxe",0);
        setHardness(2);
    }
}
