package me.marnic.extrabows.common.blocks;

import me.marnic.extrabows.api.registry.ExtraBowsRegistry;
import me.marnic.extrabows.api.util.AlertUtil;
import me.marnic.extrabows.common.blockentities.BlockEntityBridgeBlock;
import me.marnic.extrabows.common.main.ExtraBows;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

/**
 * Copyright (c) 08.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BlockBridgeBlock extends Block implements BlockEntityProvider {


    public BlockBridgeBlock() {
        super(Settings.of(Material.METAL).strength(-1,100000000));
        ExtraBowsRegistry.register(this,"bridge_block");
    }

    @Override
    public BlockEntity createBlockEntity(BlockView var1) {
        return new BlockEntityBridgeBlock().setTicksToLive(10000000);
    }

    @Override
    public boolean hasBlockEntity() {
        return true;
    }

    @Override
    public void onPlaced(World worldIn, BlockPos pos, BlockState blockState_1, LivingEntity placer, ItemStack itemStack_1) {
        super.onPlaced(worldIn, pos, blockState_1, placer, itemStack_1);
        worldIn.removeBlock(pos,false);
        if(placer instanceof PlayerEntity) {
            AlertUtil.alert((PlayerEntity) placer,"This block can not be placed manually!", Formatting.RED);
        }
    }

    @Override
    public BlockSoundGroup getSoundGroup(BlockState blockState_1) {
        return BlockSoundGroup.METAL;
    }


    private int ticksToLive;

    @Override
    public void onBreak(World world_1, BlockPos blockPos_1, BlockState blockState_1, PlayerEntity playerEntity_1) {
        ticksToLive = ((BlockEntityBridgeBlock)world_1.getBlockEntity(blockPos_1)).getTicksToLive();
        super.onBreak(world_1, blockPos_1, blockState_1, playerEntity_1);
    }

    @Override
    public void onBroken(IWorld iWorld_1, BlockPos blockPos_1, BlockState blockState_1) {
        super.onBroken(iWorld_1, blockPos_1, blockState_1);
        World world = (World) iWorld_1;

        world.setBlockState(blockPos_1,ExtraBowsObjects.BRIDGE_BLOCK.getDefaultState());
        ((BlockEntityBridgeBlock)world.getBlockEntity(blockPos_1)).setTicksToLive(ticksToLive);
    }
}
