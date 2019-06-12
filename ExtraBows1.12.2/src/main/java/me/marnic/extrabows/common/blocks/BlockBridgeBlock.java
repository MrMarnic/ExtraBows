package me.marnic.extrabows.common.blocks;

import me.marnic.extrabows.api.block.BasicBlock;
import me.marnic.extrabows.api.util.AlertUtil;
import me.marnic.extrabows.common.blocks.tileentities.TileEntityBridgeBlock;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Copyright (c) 05.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BlockBridgeBlock extends BasicBlock implements ITileEntityProvider {
    public BlockBridgeBlock() {
        super("bridge_block", Material.IRON);
        setBlockUnbreakable();
        setSoundType(SoundType.METAL);
        setResistance(10000000);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBridgeBlock().setTicksToLive(1000000);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        worldIn.setBlockToAir(pos);
        if(placer instanceof EntityPlayer) {
            AlertUtil.alert((EntityPlayer)placer,"This block can not be placed manually!", TextFormatting.RED);
        }
    }

    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return EnumPushReaction.IGNORE;
    }
}
