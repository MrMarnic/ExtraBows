package me.marnic.extrabows.api.upgrade;

import me.marnic.extrabows.api.util.*;
import me.marnic.extrabows.common.upgrades.BridgeUpgrade;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.explosion.Explosion;

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

    public static ArrowModifierUpgrade PUSH_UPGRADE;
    public static ArrowModifierUpgrade FLYING_UPGRADE;
    public static ArrowModifierUpgrade METEOR_UPGRADE;

    public static ArrowModifierUpgrade HEAL_FROM_DAMAGE;
    public static ArrowModifierUpgrade ARROW_COST;
    
    public static int DURABILITY_UPGRADE = 400;

    public static void init() {
        DOUBLE_UPGRADE = new ArrowMultiplierUpgrade("double_upgrade", DURABILITY_UPGRADE){
            @Override
            public void handleAction(BowItem basicBow, World worldIn, ItemStack itemstack, PlayerEntity entityplayer, float f, ItemStack stack, boolean flag1, UpgradeList list) {

                if(itemstack.getCount()>=2 || flag1) {
                    ProjectileEntity entityarrow2 = ArrowUtil.createArrowComplete(worldIn,itemstack,entityplayer,basicBow,f,stack,flag1,0,2.5f,list);
                    worldIn.spawnEntity(entityarrow2);

                    ProjectileEntity entityarrow1 = ArrowUtil.createArrowComplete(worldIn,itemstack,entityplayer,basicBow,f,stack,flag1,0,-2.5f,list);
                    worldIn.spawnEntity(entityarrow1);
                }else {
                    ProjectileEntity entityarrow = ArrowUtil.createArrowComplete(worldIn,itemstack,entityplayer,basicBow,f,stack,flag1,0,0,list);

                    worldIn.spawnEntity(entityarrow);
                }
            }

            @Override
            public void shrinkStack(ItemStack stack) {
                ExtraBowsUtil.shrinkStack(stack,stack.getCount()>=2?2:1);
            }

            @Override
            public boolean canShoot(ItemStack arrow, PlayerEntity player) {
                return arrow.getCount()>=2;
            }

            @Override
            public List<Text> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }
        };
        TRIPLE_UPGRADE = new ArrowMultiplierUpgrade("triple_upgrade",DURABILITY_UPGRADE){
            @Override
            public void handleAction(BowItem basicBow, World worldIn, ItemStack itemstack, PlayerEntity entityplayer, float f, ItemStack stack, boolean flag1, UpgradeList list) {

                if(itemstack.getCount()>=3 ||flag1) {
                    ProjectileEntity entityarrow1 = ArrowUtil.createArrowComplete(worldIn,itemstack,entityplayer,basicBow,f,stack,flag1,0,2.5f,list);
                    worldIn.spawnEntity(entityarrow1);

                    ProjectileEntity entityarrow2 = ArrowUtil.createArrowComplete(worldIn,itemstack,entityplayer,basicBow,f,stack,flag1,0,0,list);
                    worldIn.spawnEntity(entityarrow2);

                    ProjectileEntity entityarrow3 = ArrowUtil.createArrowComplete(worldIn,itemstack,entityplayer,basicBow,f,stack,flag1,0,-2.5f,list);
                    worldIn.spawnEntity(entityarrow3);
                }else if(itemstack.getCount()==2) {
                    Upgrades.DOUBLE_UPGRADE.handleAction(basicBow, worldIn, itemstack, entityplayer, f, stack, flag1, list);
                }else {
                    ProjectileEntity entityarrow = ArrowUtil.createArrowComplete(worldIn,itemstack,entityplayer,basicBow,f,stack,flag1,0,0,list);

                    worldIn.spawnEntity(entityarrow);
                }
            }

            @Override
            public void shrinkStack(ItemStack stack) {
                ExtraBowsUtil.shrinkStack(stack,stack.getCount()>=3?3:stack.getCount()>=2?2:1);
            }

            @Override
            public boolean canShoot(ItemStack arrow, PlayerEntity player) {
                return arrow.getCount()>=3;
            }

            @Override
            public List<Text> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }
        };

        LIGHTNING_UPGRADE = new ArrowModifierUpgrade("lightning_upgrade",DURABILITY_UPGRADE) {
            @Override
            public void handleBlockHit(BlockPos pos, World world, PlayerEntity player, ProjectileEntity arrow, UpgradeList upgradeList) {
                ((ServerWorld)world).addLightning(new LightningEntity(world,pos.getX(),pos.getY(),pos.getZ(),false));
                arrow.remove();
            }

            @Override
            public void handleEntityHit(Entity entity, World world, PlayerEntity player, ProjectileEntity arrow, UpgradeList upgradeList) {
                ((ServerWorld)world).addLightning(new LightningEntity(world,entity.getX(),entity.getY(),entity.getZ(),false));
                arrow.remove();
            }

            @Override
            public void handleArrowCreate(ProjectileEntity arrow, PlayerEntity player, UpgradeList upgradeList) {
                arrow.setOnFireFor(10000);
            }

            @Override
            public List<Text> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }
        };

        FIRE_UPGRADE = new ArrowModifierUpgrade("fire_upgrade",DURABILITY_UPGRADE) {
            @Override
            public void handleBlockHit(BlockPos pos, World world, PlayerEntity player, ProjectileEntity arrow, UpgradeList upgradeList) {
                if(world.isAir(pos)) {
                    BlockState blockstate1 = ((FireBlock) Blocks.FIRE).getStateForPosition(world, pos);
                    world.setBlockState(pos, blockstate1, 11);
                }
            }

            @Override
            public void handleEntityHit(Entity entity, World world, PlayerEntity player, ProjectileEntity arrow, UpgradeList upgradeList) {
                entity.setOnFireFor(5);
            }

            @Override
            public void handleArrowCreate(ProjectileEntity arrow, PlayerEntity player, UpgradeList upgradeList) {
                arrow.setOnFireFor(10000);
            }

            @Override
            public List<Text> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this,1), UpgradeUtil.getTranslatedDescriptionForUpgrade(this,2));
            }

        };

        ENDER_UPGRADE = new ArrowModifierUpgrade("ender_upgrade",DURABILITY_UPGRADE) {
            @Override
            public void handleBlockHit(BlockPos pos, World world, PlayerEntity player, ProjectileEntity arrow, UpgradeList upgradeList) {
                player.teleport(pos.getX(),pos.getY()+1,pos.getZ());
            }

            @Override
            public void handleEntityHit(Entity entity, World world, PlayerEntity player, ProjectileEntity arrow, UpgradeList upgradeList) {
                player.teleport(entity.getX(),entity.getY(),entity.getZ());
            }

            @Override
            public List<Text> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }
        };

        FREEZE_UPGRADE = new ArrowModifierUpgrade("freeze_upgrade",DURABILITY_UPGRADE) {
            @Override
            public void handleBlockHit(BlockPos pos, World world, PlayerEntity player, ProjectileEntity arrow, UpgradeList upgradeList) {
                pos = pos.add(0,0,0);

                if(world.isAir(pos)) {
                    world.setBlockState(pos, Blocks.SNOW.getDefaultState());
                    if(RandomUtil.isChance(8,1)) {
                        SnowGolemEntity snowman = new SnowGolemEntity(EntityType.SNOW_GOLEM,world);
                        snowman.setPosition(pos.getX(),pos.getY(),pos.getZ());
                        world.spawnEntity(snowman);
                    }
                }
            }

            @Override
            public void handleEntityHit(Entity entity, World world, PlayerEntity player, ProjectileEntity arrow, UpgradeList upgradeList) {
                if(entity instanceof LivingEntity) {

                    ((LivingEntity)entity).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,200,1));
                }
            }

            @Override
            public void handleWaterHit(BlockPos pos, World world, PlayerEntity player, ProjectileEntity arrow, UpgradeList upgradeList) {
                if(world.getBlockState(pos).getBlock().equals(Blocks.WATER)) {
                    world.setBlockState(pos, Blocks.PACKED_ICE.getDefaultState());
                }


                UpgradeUtil.getBlocksInRadius(pos,2).forEach((pos1) -> {
                    if(world.getBlockState(pos1).getBlock().equals(Blocks.WATER) || world.getBlockState(pos1).getBlock().equals(Blocks.WATER) ) {
                        if(world.isAir(pos1.up())) {
                            if(RandomUtil.isChance(2,1)) {
                                world.setBlockState(pos1, Blocks.PACKED_ICE.getDefaultState());
                            }
                            if(RandomUtil.isChance(2,1)) {
                                if(world.isAir(pos1.up().up())) {
                                    world.setBlockState(pos1.up(), Blocks.SNOW.getDefaultState());
                                }
                            }
                        }
                    }
                });
            }

            @Override
            public List<Text> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }
        };

        EXPLOSIVE_UPGRADE = new ArrowModifierUpgrade("explosive_upgrade",DURABILITY_UPGRADE) {

            @Override
            public void handleBlockHit(BlockPos pos, World world, PlayerEntity player, ProjectileEntity arrow, UpgradeList upgradeList) {
                world.createExplosion(player,pos.getX(), pos.getY(),pos.getZ(),3, Explosion.DestructionType.BREAK);
                arrow.remove();
            }


            @Override
            public void handleEntityHit(Entity entity, World world, PlayerEntity player, ProjectileEntity arrow, UpgradeList upgradeList) {
                world.createExplosion(player,entity.getX(), entity.getY(),entity.getZ(),3, Explosion.DestructionType.BREAK);
                arrow.remove();
            }

            @Override
            public List<Text> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }
        };

        HEAL_FROM_DAMAGE = new ArrowModifierUpgrade("heal_from_damage_upgrade",DURABILITY_UPGRADE) {
            @Override
            public void handleEntityHit(Entity entity, World world, PlayerEntity player, ProjectileEntity arrow, UpgradeList upgradeList) {
                if(entity instanceof LivingEntity) {
                    if(RandomUtil.isChance(3,1)) {
                        player.heal(0.5f);
                    }
                }
            }

            @Override
            public List<Text> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }
        };

        ARROW_COST = new ArrowModifierUpgrade("arrow_cost_upgrade",DURABILITY_UPGRADE) {
            @Override
            public void handleArrowCreate(ProjectileEntity arrow, PlayerEntity player, UpgradeList upgradeList) {
                if(RandomUtil.isChance(8,1)) {
                    if(!player.inventory.insertStack(new ItemStack(Items.ARROW,1))) {
                        player.dropItem(new ItemStack(Items.ARROW,1),true);
                    }
                }
            }

            @Override
            public List<Text> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }
        };

        WATER_UPGRADE = new ArrowModifierUpgrade("water_upgrade",DURABILITY_UPGRADE) {
            @Override
            public void handleBlockHit(BlockPos pos, World world, PlayerEntity player, ProjectileEntity arrow, UpgradeList upgradeList) {
                Iterable<BlockPos> list = UpgradeUtil.getBlocksInRadius(pos,3);

                list.forEach(pos1 -> {
                    if(world.getBlockState(pos1).getBlock().equals(Blocks.FIRE)) {
                        world.removeBlock(pos1,false);
                    }
                });

                if (RandomUtil.isChance(6, 1) && world.dimension.getType()!= DimensionType.THE_NETHER) {
                    world.setBlockState(pos, Blocks.WATER.getDefaultState());
                    TimerUtil.addTimeCommand(new TimeCommand( 20 * 5, () -> {
                        world.setBlockState(pos, Blocks.STONE.getDefaultState());
                        world.removeBlock(pos,false);
                    }));
                }
            }

            @Override
            public List<Text> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this,1), UpgradeUtil.getTranslatedDescriptionForUpgrade(this,2));
            }
        };

        FLYING_UPGRADE = new ArrowModifierUpgrade("flying_upgrade",DURABILITY_UPGRADE) {
            @Override
            public void handleEntityInit(ProjectileEntity arrow, UpgradeList upgradeList, PlayerEntity player) {
                player.startRiding(arrow);
            }

            @Override
            public void handleBlockHit(BlockPos pos, World world, PlayerEntity player, ProjectileEntity arrow, UpgradeList upgradeList) {
                if(arrow.hasPlayerRider()) {
                    player.damage(DamageSource.FALL,2);
                }
            }

            @Override
            public void handleEntityHit(Entity entity, World world, PlayerEntity player, ProjectileEntity arrow, UpgradeList upgradeList) {
                if(arrow.hasPlayerRider()) {
                    player.damage(DamageSource.FALL,2);
                }
            }

            @Override
            public List<Text> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }
        };

        PUSH_UPGRADE = new ArrowModifierUpgrade("push_upgrade",DURABILITY_UPGRADE) {
            @Override
            public void handleBlockHit(BlockPos pos, World world, PlayerEntity player, ProjectileEntity arrow, UpgradeList upgradeList) {

                world.getEntities(LivingEntity.class,UpgradeUtil.getRadiusBoundingBox(pos,6),null).forEach((livingEntity -> {

                    double deltaX = (livingEntity.getX()-arrow.getX());
                    double deltaZ = (livingEntity.getZ()-arrow.getX());

                    int posX = deltaX>0 ? 1:-1;
                    int posZ = deltaZ>0 ? 1:-1;

                    deltaX = Math.abs(deltaX);
                    deltaZ = Math.abs(deltaZ);

                    double highestValue = deltaX > deltaZ ? deltaX : deltaZ;

                    livingEntity.addVelocity(posX * (deltaX/highestValue),0.7f,posZ * (deltaZ/highestValue));
                }));
            }

            @Override
            public void handleEntityHit(Entity entity, World world, PlayerEntity player, ProjectileEntity arrow, UpgradeList upgradeList) {
                world.getEntities(LivingEntity.class,UpgradeUtil.getRadiusBoundingBox(entity.getBlockPos(),6),null).forEach((livingEntity -> {
                    double deltaX = (livingEntity.getX()-arrow.getX());
                    double deltaZ = (livingEntity.getZ()-arrow.getZ());

                    int posX = deltaX>0 ? 1:-1;
                    int posZ = deltaZ>0 ? 1:-1;

                    deltaX = Math.abs(deltaX);
                    deltaZ = Math.abs(deltaZ);

                    double highestValue = deltaX > deltaZ ? deltaX : deltaZ;

                    livingEntity.addVelocity(posX * (deltaX/highestValue),0.7f,posZ * (deltaZ/highestValue));
                }));
            }

            @Override
            public List<Text> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }
        };
        
        METEOR_UPGRADE = new ArrowModifierUpgrade("meteor_upgrade",DURABILITY_UPGRADE) {
            @Override
            public void handleBlockHit(BlockPos pos, World world, PlayerEntity player, ProjectileEntity arrow, UpgradeList upgradeList) {
                spawnMeteor(world,arrow);
            }

            @Override
            public void handleEntityHit(Entity entity, World world, PlayerEntity player, ProjectileEntity arrow, UpgradeList upgradeList) {
                spawnMeteor(world,arrow);
            }

            private void spawnMeteor(World world,ProjectileEntity arrow) {
                if(world.isAir(new BlockPos(arrow.getX(),arrow.getY() + 20,arrow.getZ()))) {
                    FireballEntity fireballEntity = new FireballEntity(EntityType.FIREBALL,world);
                    fireballEntity.explosionPower = 2;
                    fireballEntity.setPosition(arrow.getX(),arrow.getY() + 20,arrow.getZ());
                    fireballEntity.addVelocity(0,-2f,0);
                    world.spawnEntity(fireballEntity);
                }
            }

            @Override
            public List<Text> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }
        };

        BRIDGE_UPGRADE = new BridgeUpgrade();
    }
}
