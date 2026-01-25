package me.marnic.extrabows.common.upgrades;

import me.marnic.extrabows.api.upgrade.ArrowModifierUpgrade;
import me.marnic.extrabows.api.upgrade.UpgradeList;
import me.marnic.extrabows.api.upgrade.Upgrades;
import me.marnic.extrabows.api.util.TimeCommand;
import me.marnic.extrabows.api.util.TimerUtil;
import me.marnic.extrabows.api.util.UpgradeUtil;
import me.marnic.extrabows.common.blocks.BlockBridgeBlock;
import me.marnic.extrabows.common.blocks.tileentities.TileEntityBridgeBlock;
import me.marnic.extrabows.common.config.ExtraBowsConfig;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 02.06.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BridgeUpgrade extends ArrowModifierUpgrade {

    public static RegistryObject<BlockBridgeBlock> BUILDING_BLOCK = ExtraBowsObjects.BRIDGE_BLOCK;

    public static int ACTIVE_TIME = 20;

    public BridgeUpgrade() {
        super("bridge_upgrade", Upgrades.BRIDGE_UPGRADE_ITEM);
    }

    public static boolean placeBlock(BlockPos pos, BlockState state, Level level) {
        if (level.isEmptyBlock(pos)) {
            level.setBlockAndUpdate(pos, state);
            return true;
        } else if (level.getBlockState(pos).getBlock().equals(Blocks.WATER)) {
            level.setBlockAndUpdate(pos, state);
            ((TileEntityBridgeBlock) level.getBlockEntity(pos)).setFluid(Blocks.WATER);
            return true;
        } else if (level.getBlockState(pos).getBlock().equals(Blocks.LAVA)) {
            level.setBlockAndUpdate(pos, state);
            ((TileEntityBridgeBlock) level.getBlockEntity(pos)).setFluid(Blocks.LAVA);
            return true;
        }
        return false;
    }

    @Override
    public void initConfigOptions() {
        getItem().setMaxDamage(ExtraBowsConfig.DURABILITY_BRIDGE_UPGRADE.get());
    }

    @Override
    public List<Component> getDescription() {
        return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this, 1), UpgradeUtil.getTranslatedDescriptionForUpgrade(this, 2));
    }

    @Override
    public void handleEntityInit(AbstractArrow arrow, UpgradeList upgradeList, Player player) {
        BuildingUpgradeData data = new BuildingUpgradeData();
        data.facing = player.getDirection();
        upgradeList.getDataMap().put(this, data);
    }

    @Override
    public void handleFlyingEvent(AbstractArrow arrow, Level level, UpgradeList upgradeList) {
        BuildingUpgradeData data = (BuildingUpgradeData) upgradeList.getDataMap().get(this);

        BlockPos pos = new BlockPos(arrow.position()).offset(0, -3, 0);

        data.builder.needsFix(pos, data.blockPos.size() >= 1 ? data.blockPos.get(data.blockPos.size() - 1) : null, data.blockPos, data.blockPos.size(), level, data.facing);
        data.blockPos.add(new BlockSave(pos, data.blockPos.size()).setRemovable(level.isEmptyBlock(pos)));

        TimerUtil.addTimeCommand(new TimeCommand(data.blockPos.size() * 2, new Runnable() {
            BlockPos pos1 = new BlockPos(arrow.position()).offset(0, -3, 0);
            int size = data.blockPos.size();

            @Override
            public void run() {
                if (placeBlock(pos1, BUILDING_BLOCK.get().defaultBlockState(), level)) {
                    ((TileEntityBridgeBlock) level.getBlockEntity(pos1)).setTicksToLive(size + ACTIVE_TIME * 20);
                }
            }
        }));
    }

    private boolean fix(BlockPos next) {
        return isOffset(next.getX(), 1) || isOffset(next.getY(), 1) || isOffset(next.getZ(), 1);
    }

    private boolean isOffset(int number, int offset) {
        return number > offset | number < -offset;
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
        public void needsFix(BlockPos pos, BlockSave last, ArrayList<BlockSave> blocks, int id, Level Level, Direction facing) {
            if (blocks.size() >= 1) {
                BlockPos next = last.pos.subtract(pos);
                BlockPos nextAbs = new BlockPos(Math.abs(next.getX()), Math.abs(next.getY()), Math.abs(next.getZ()));

                if (fix(next)) {
                    int y_needed = nextAbs.getY();
                    int x_needed = nextAbs.getX();
                    int z_needed = nextAbs.getZ();
                    int neg_x = next.getX() < 0 ? -1 : 1;
                    int neg_y = next.getY() < 0 ? -1 : 1;
                    int neg_z = next.getZ() < 0 ? -1 : 1;

                    BlockPos begin = pos.offset(0, 0, 0);

                    int all = x_needed + y_needed + z_needed;

                    for (int i = 0; i < all; i++) {
                        if (y_needed > 0) {
                            begin = begin.offset(0, neg_y * 1, 0);
                        }

                        if (x_needed > 0) {
                            begin = begin.offset(1 * neg_x, 0, 0);
                        }

                        if (z_needed > 0) {
                            begin = begin.offset(0, 0, 1 * neg_z);
                        }
                        BlockPos finalBegin = begin;
                        TimerUtil.addTimeCommand(new TimeCommand((id) * 2, new Runnable() {
                            @Override
                            public void run() {
                                if (placeBlock(finalBegin, BUILDING_BLOCK.get().defaultBlockState(), Level)) {
                                    ((TileEntityBridgeBlock) Level.getBlockEntity(finalBegin)).setTicksToLive((id) + ACTIVE_TIME * 20);
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
}
