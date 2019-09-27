package me.marnic.extrabows.common.upgrades;

import me.marnic.extrabows.api.upgrade.ArrowModifierUpgrade;
import me.marnic.extrabows.api.upgrade.UpgradeList;
import me.marnic.extrabows.api.upgrade.Upgrades;
import me.marnic.extrabows.api.util.TimeCommand;
import me.marnic.extrabows.api.util.TimerUtil;
import me.marnic.extrabows.api.util.UpgradeUtil;
import me.marnic.extrabows.common.blockentities.BlockEntityBridgeBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 02.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BridgeUpgrade extends ArrowModifierUpgrade {

    public static Block BUILDING_BLOCK;

    public static int ACTIVE_TIME = 20;

    public BridgeUpgrade() {
        super("bridge_upgrade", Upgrades.DURABILITY_UPGRADE);
    }

    @Override
    public List<Text> getDescription() {
        return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this,1), UpgradeUtil.getTranslatedDescriptionForUpgrade(this,2));
    }

    @Override
    public void handleEntityInit(ProjectileEntity arrow, UpgradeList upgradeList, PlayerEntity player) {
            BuildingUpgradeData data = new BuildingUpgradeData();

            data.facing = player.getHorizontalFacing();
            upgradeList.getDataMap().put(this, data);
    }

    @Override
    public void handleFlyingEvent(ProjectileEntity arrow, World world, UpgradeList upgradeList) {

        BuildingUpgradeData data = (BuildingUpgradeData) upgradeList.getDataMap().get(this);

        BlockPos pos = arrow.getBlockPos().add(0, -3, 0);

        data.builder.needsFix(pos, data.blockPos.size()>=1?data.blockPos.get(data.blockPos.size()-1):null,data.blockPos, data.blockPos.size(), world,data.facing);
        data.blockPos.add(new BlockSave(pos, data.blockPos.size()).setRemovable(world.isAir(pos)));

        TimerUtil.addTimeCommand(new TimeCommand(data.blockPos.size() * 2, new Runnable() {
            BlockPos pos1 = arrow.getBlockPos().add(0, -3, 0);
            int size = data.blockPos.size();

            @Override
            public void run() {
                if(placeBlock(pos1,BUILDING_BLOCK.getDefaultState(),world)) {
                    ((BlockEntityBridgeBlock)world.getBlockEntity(pos1)).setTicksToLive(size + ACTIVE_TIME * 20);
                }
            }
        }));
    }

    public static boolean placeBlock(BlockPos pos, BlockState state, World world) {
        if(world.isAir(pos)) {
            world.setBlockState(pos,state);
            return true;
        }else if(world.getBlockState(pos).getBlock().equals(Blocks.WATER)) {
            world.setBlockState(pos,state);
            ((BlockEntityBridgeBlock)world.getBlockEntity(pos)).setFluid(Blocks.WATER);
            return true;
        }else if(world.getBlockState(pos).getBlock().equals(Blocks.LAVA)) {
            world.setBlockState(pos,state);
            ((BlockEntityBridgeBlock)world.getBlockEntity(pos)).setFluid(Blocks.LAVA);
            return true;
        }
        return false;
    }

    class BuildingUpgradeData {
        public ArrayList<BlockSave> blockPos;
        public BridgeBuilder builder;
        public Direction facing;

        public BuildingUpgradeData() {
            this.blockPos = new ArrayList<>();
            this.builder = new BridgeBuilder();
        }
    }

    class BlockSave {
        public BlockPos pos;
        public int id;
        public boolean canBeRemoved = true;

        public BlockSave(BlockPos pos, int id) {
            this.pos = pos;
            this.id = id;
        }

        public BlockSave setRemovable(boolean value) {
            this.canBeRemoved = value;
            return this;
        }
    }

    class BridgeBuilder {
        public void needsFix(BlockPos pos, BlockSave last, ArrayList<BlockSave> blocks, int id, World world, Direction facing) {
            if (blocks.size() >= 1) {
                BlockPos next = last.pos.subtract(pos);
                BlockPos nextAbs = new BlockPos(Math.abs(next.getX()), Math.abs(next.getY()), Math.abs(next.getZ()));

                if (fix(next)) {
                        int y_needed = nextAbs.getY();
                        int x_needed = nextAbs.getX();
                        int z_needed = nextAbs.getZ();
                        int neg_x = next.getX()<0?-1:1;
                        int neg_y = next.getY()<0?-1:1;
                        int neg_z = next.getZ()<0?-1:1;

                        BlockPos begin = pos.add(0,0,0);

                        int all = x_needed + y_needed + z_needed;

                        for(int i = 0;i<all;i++) {
                            if(y_needed>0) {
                                begin=begin.add(0,neg_y*1,0);
                            }

                            if(x_needed>0) {
                                begin=begin.add(1*neg_x,0,0);
                            }

                            if(z_needed>0) {
                                begin=begin.add(0,0,1*neg_z);
                            }
                            BlockPos finalBegin = begin;
                            TimerUtil.addTimeCommand(new TimeCommand((id)*2, new Runnable() {
                                @Override
                                public void run() {
                                    if(placeBlock(finalBegin,BUILDING_BLOCK.getDefaultState(),world)) {
                                        ((BlockEntityBridgeBlock)world.getBlockEntity(finalBegin)).setTicksToLive((id) + ACTIVE_TIME * 20);
                                    }
                                }
                            }));

                            x_needed--;
                            y_needed--;
                            z_needed--;
                        }
                }
            }
        }
    }

    private boolean fix(BlockPos next) {
        return isOffset(next.getX(), 1) || isOffset(next.getY(), 1) || isOffset(next.getZ(), 1);
    }

    private boolean isOffset(int number, int offset) {
        return number > offset | number < -offset;
    }
}
