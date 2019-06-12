package me.marnic.extrabows.api.upgrade;

import me.marnic.extrabows.api.util.*;
import me.marnic.extrabows.common.config.ExtraBowsConfig;
import me.marnic.extrabows.common.items.BasicBow;
import me.marnic.extrabows.common.upgrades.BridgeUpgrade;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * Copyright (c) 29.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class Upgrades {
    public static ArrowMultiplierUpgrade DOUBLE_UPGRADE;
    public static ArrowMultiplierUpgrade TRIPLE_UPGRADE;

    public static ArrowModifierUpgrade LIGHTNING_UPGRADE;
    public static ArrowModifierUpgrade FIRE_UPGRADE;
    public static ArrowModifierUpgrade ENDER_UPGRADE;
    public static ArrowModifierUpgrade FREEZE_UPGRADE;
    public static ArrowModifierUpgrade EXPLOSIVE_UPGRADE;
    public static ArrowModifierUpgrade WATER_UPGRADE;
    public static ArrowModifierUpgrade BRIDGE_UPGRADE;

    public static ArrowModifierUpgrade HEAL_FROM_DAMAGE;
    public static ArrowModifierUpgrade ARROW_COST;

    public static void init() {
        DOUBLE_UPGRADE = new ArrowMultiplierUpgrade("double_upgrade", ExtraBowsConfig.DURABILITY_DOUBLE_UPGRADE){
            @Override
            public void handleAction(BasicBow basicBow, World worldIn, ItemStack itemstack, EntityPlayer entityplayer, float f, ItemStack stack, boolean flag1, UpgradeList list) {

                if(itemstack.getCount()>=2 || flag1) {
                    EntityArrow entityarrow2 = ArrowUtil.createArrowComplete(worldIn,itemstack,entityplayer,basicBow,f,stack,flag1,0,2.5f,list);
                    worldIn.spawnEntity(entityarrow2);

                    EntityArrow entityarrow1 = ArrowUtil.createArrowComplete(worldIn,itemstack,entityplayer,basicBow,f,stack,flag1,0,-2.5f,list);
                    worldIn.spawnEntity(entityarrow1);
                }else {
                    EntityArrow entityarrow = ArrowUtil.createArrowComplete(worldIn,itemstack,entityplayer,basicBow,f,stack,flag1,0,0,list);

                    worldIn.spawnEntity(entityarrow);
                }
            }

            @Override
            public void shrinkStack(ItemStack stack) {
                stack.shrink(stack.getCount()>=2?2:1);
            }

            @Override
            public boolean canShoot(ItemStack arrow, EntityPlayer player) {
                return arrow.getCount()>=2;
            }

            @Override
            public List<String> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }
        };
        TRIPLE_UPGRADE = new ArrowMultiplierUpgrade("triple_upgrade",ExtraBowsConfig.DURABILITY_TRIPLE_UPGRADE){
            @Override
            public void handleAction(BasicBow basicBow, World worldIn, ItemStack itemstack, EntityPlayer entityplayer,float f,ItemStack stack,boolean flag1,UpgradeList list) {

                if(itemstack.getCount()>=3 ||flag1) {
                    EntityArrow entityarrow1 = ArrowUtil.createArrowComplete(worldIn,itemstack,entityplayer,basicBow,f,stack,flag1,0,2.5f,list);
                    worldIn.spawnEntity(entityarrow1);

                    EntityArrow entityarrow2 = ArrowUtil.createArrowComplete(worldIn,itemstack,entityplayer,basicBow,f,stack,flag1,0,0,list);
                    worldIn.spawnEntity(entityarrow2);

                    EntityArrow entityarrow3 = ArrowUtil.createArrowComplete(worldIn,itemstack,entityplayer,basicBow,f,stack,flag1,0,-2.5f,list);
                    worldIn.spawnEntity(entityarrow3);
                }else if(itemstack.getCount()==2) {
                    Upgrades.DOUBLE_UPGRADE.handleAction(basicBow, worldIn, itemstack, entityplayer, f, stack, flag1, list);
                }else {
                    EntityArrow entityarrow = ArrowUtil.createArrowComplete(worldIn,itemstack,entityplayer,basicBow,f,stack,flag1,0,0,list);

                    worldIn.spawnEntity(entityarrow);
                }
            }

            @Override
            public void shrinkStack(ItemStack stack) {
                stack.shrink(stack.getCount()>=3?3:stack.getCount()>=2?2:1);
            }

            @Override
            public boolean canShoot(ItemStack arrow, EntityPlayer player) {
                return arrow.getCount()>=3;
            }

            @Override
            public List<String> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }
        };

        LIGHTNING_UPGRADE = new ArrowModifierUpgrade("lightning_upgrade",ExtraBowsConfig.DURABILITY_LIGHTNING_UPGRADE) {
            @Override
            public void handleBlockHit(BlockPos pos, World world, EntityPlayer player, EntityArrow arrow, UpgradeList upgradeList) {
                world.addWeatherEffect(new EntityLightningBolt(world,pos.getX(),pos.getY(),pos.getZ(),false));
                arrow.setDead();
            }

            @Override
            public void handleEntityHit(Entity entity, World world, EntityPlayer player, EntityArrow arrow, UpgradeList upgradeList) {
                world.addWeatherEffect(new EntityLightningBolt(world,entity.posX,entity.posY,entity.posZ,false));
                arrow.setDead();
            }

            @Override
            public void handleArrowCreate(EntityArrow arrow, EntityPlayer player, UpgradeList upgradeList) {
                arrow.setFire(10000);
            }

            @Override
            public List<String> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }
        };

        FIRE_UPGRADE = new ArrowModifierUpgrade("fire_upgrade",ExtraBowsConfig.DURABILITY_FIRE_UPGRADE) {
            @Override
            public void handleBlockHit(BlockPos pos, World world, EntityPlayer player, EntityArrow arrow, UpgradeList upgradeList) {
                pos = pos.add(0,1,0);

                if(world.isAirBlock(pos)) {
                    world.setBlockState(pos, Blocks.FIRE.getDefaultState(),11);
                }
            }

            @Override
            public void handleEntityHit(Entity entity, World world, EntityPlayer player, EntityArrow arrow, UpgradeList upgradeList) {
                entity.setFire(5);
            }

            @Override
            public void handleArrowCreate(EntityArrow arrow, EntityPlayer player, UpgradeList upgradeList) {
                arrow.setFire(10000);
            }

            @Override
            public List<String> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this,1),UpgradeUtil.getTranslatedDescriptionForUpgrade(this,2));
            }

        };

        ENDER_UPGRADE = new ArrowModifierUpgrade("ender_upgrade",ExtraBowsConfig.DURABILITY_ENDER_UPGRADE) {
            @Override
            public void handleBlockHit(BlockPos pos, World world, EntityPlayer player, EntityArrow arrow, UpgradeList upgradeList) {
                player.setPositionAndUpdate(pos.getX(),pos.getY()+1,pos.getZ());
            }

            @Override
            public void handleEntityHit(Entity entity, World world, EntityPlayer player, EntityArrow arrow, UpgradeList upgradeList) {
                player.setPositionAndUpdate(entity.posX,entity.posY,entity.posZ);
            }

            @Override
            public List<String> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }
        };

        FREEZE_UPGRADE = new ArrowModifierUpgrade("freeze_upgrade",ExtraBowsConfig.DURABILITY_FREEZE_UPGRADE) {
            @Override
            public void handleBlockHit(BlockPos pos, World world, EntityPlayer player, EntityArrow arrow, UpgradeList upgradeList) {
                pos = pos.add(0,1,0);
                if(world.isAirBlock(pos) && world.isBlockFullCube(pos.add(0,-1,0))) {
                    world.setBlockState(pos,Blocks.SNOW_LAYER.getDefaultState());
                    if(RandomUtil.isChance(8,1)) {
                        EntitySnowman snowman = new EntitySnowman(world);
                        snowman.setPosition(pos.getX(),pos.getY(),pos.getZ());
                        world.spawnEntity(snowman);
                    }
                }
            }

            @Override
            public void handleEntityHit(Entity entity, World world, EntityPlayer player, EntityArrow arrow, UpgradeList upgradeList) {
                if(entity instanceof EntityLivingBase) {
                    ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS,200,1));
                }
            }

            @Override
            public void handleWaterHit(BlockPos pos, World world, EntityPlayer player, EntityArrow arrow, UpgradeList upgradeList) {
                if(world.getBlockState(pos).getBlock().equals(Blocks.WATER)) {
                    world.setBlockState(pos,Blocks.PACKED_ICE.getDefaultState());
                }
            }

            @Override
            public List<String> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }
        };

        EXPLOSIVE_UPGRADE = new ArrowModifierUpgrade("explosive_upgrade",ExtraBowsConfig.DURABILITY_EXPLOSIVE_UPGRADE) {

            @Override
            public void handleBlockHit(BlockPos pos, World world, EntityPlayer player, EntityArrow arrow, UpgradeList upgradeList) {
                world.createExplosion(player,pos.getX(), pos.getY(),pos.getZ(),3,true);
                arrow.setDead();
            }


            @Override
            public void handleEntityHit(Entity entity, World world, EntityPlayer player, EntityArrow arrow, UpgradeList upgradeList) {
                world.createExplosion(player,entity.posX, entity.posY,entity.posZ,3,true);
                arrow.setDead();
            }

            @Override
            public List<String> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }
        };

        HEAL_FROM_DAMAGE = new ArrowModifierUpgrade("heal_from_damage_upgrade",ExtraBowsConfig.DURABILITY_HEAL_FROM_DAMAGE_UPGRADE) {
            @Override
            public void handleEntityHit(Entity entity, World world, EntityPlayer player, EntityArrow arrow, UpgradeList upgradeList) {
                if(entity instanceof EntityLivingBase) {
                    if(RandomUtil.isChance(3,1)) {
                        player.heal(0.5f);
                    }
                }
            }

            @Override
            public List<String> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }
        };

        ARROW_COST = new ArrowModifierUpgrade("arrow_cost_upgrade",ExtraBowsConfig.DURABILITY_ARROW_COST_UPGRADE) {
            @Override
            public void handleArrowCreate(EntityArrow arrow, EntityPlayer player, UpgradeList upgradeList) {
                if(RandomUtil.isChance(8,1)) {
                    if(!player.addItemStackToInventory(new ItemStack(Items.ARROW,1))) {
                        player.dropItem(new ItemStack(Items.ARROW,1),true);
                    }
                }
            }

            @Override
            public List<String> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }
        };

        WATER_UPGRADE = new ArrowModifierUpgrade("water_upgrade",ExtraBowsConfig.DURABILITY_WATER_UPGRADE) {
            @Override
            public void handleBlockHit(BlockPos pos, World world, EntityPlayer player, EntityArrow arrow, UpgradeList upgradeList) {
                Iterable<BlockPos> list = UpgradeUtil.getBlocksInRadius(pos,3);

                for(BlockPos pos1:list) {
                    if(world.getBlockState(pos1).getBlock().equals(Blocks.FIRE)) {
                        world.setBlockToAir(pos1);
                    }
                }

                if (RandomUtil.isChance(6, 1)) {
                    world.setBlockState(pos.add(0,1,0),Blocks.FLOWING_WATER.getDefaultState());
                    TimerUtil.addTimeCommand(new TimeCommand( 20 * 5, () -> world.setBlockToAir(pos.add(0,1,0))));
                }
            }

            @Override
            public List<String> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this,1),UpgradeUtil.getTranslatedDescriptionForUpgrade(this,2));
            }
        };

        BRIDGE_UPGRADE = new BridgeUpgrade();
    }
}
