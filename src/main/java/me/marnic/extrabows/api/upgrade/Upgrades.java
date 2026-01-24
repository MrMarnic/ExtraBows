package me.marnic.extrabows.api.upgrade;

import me.marnic.extrabows.api.energy.ExtraBowsEnergy;
import me.marnic.extrabows.api.util.*;
import me.marnic.extrabows.common.config.ExtraBowsConfig;
import me.marnic.extrabows.common.items.BasicBow;
import me.marnic.extrabows.common.items.CustomBowSettings;
import me.marnic.extrabows.common.upgrades.BridgeUpgrade;
import me.marnic.extrabows.common.upgrades.ElectricUpgrade;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Blocks;
import net.minecraft.command.impl.SummonCommand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.DimensionType;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.stream.Stream;

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

    public static ArrowModifierUpgrade ELECTRIC_UPGRADE;

    public static void init() {
        DOUBLE_UPGRADE = new ArrowMultiplierUpgrade("double_upgrade") {
            @Override
            public void handleAction(BasicBow basicBow, World worldIn, ItemStack bow, PlayerEntity entityplayer, float f, ItemStack arrow, boolean flag1, UpgradeList list, boolean isLoaded) {

                if (arrow.getCount() >= 2 || flag1 || isLoaded) {
                    AbstractArrowEntity entityarrow2 = ArrowUtil.createArrowComplete(worldIn, bow, arrow, entityplayer, basicBow, f, flag1, 0, 2.5f, list, isLoaded);
                    worldIn.addFreshEntity(entityarrow2);

                    if(bow.isEmpty()) {
                        return;
                    }

                    AbstractArrowEntity entityarrow1 = ArrowUtil.createArrowComplete(worldIn, bow, arrow, entityplayer, basicBow, f, flag1, 0, -2.5f, list, isLoaded);
                    worldIn.addFreshEntity(entityarrow1);
                } else {
                    AbstractArrowEntity entityarrow = ArrowUtil.createArrowComplete(worldIn, bow, arrow, entityplayer, basicBow, f, flag1, 0, 0, list, isLoaded);
                    worldIn.addFreshEntity(entityarrow);
                }
            }

            @Override
            public void shrinkStack(ItemStack stack) {
                stack.shrink(stack.getCount() >= 2 ? 2 : 1);
            }

            @Override
            public void removeEnergy(ItemStack stack, ExtraBowsEnergy extraBowsEnergy) {
                extraBowsEnergy.extractEnergy(2 * CustomBowSettings.ENERGY_COST_PER_ARROW, false);
            }

            @Override
            public boolean canShoot(ItemStack arrow, PlayerEntity player) {
                return arrow.getCount() >= 2;
            }

            @Override
            public List<ITextComponent> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }

            @Override
            public void initConfigOptions() {
                getItem().setMaxDamage(ExtraBowsConfig.DURABILITY_DOUBLE_UPGRADE.get());
            }
        };
        TRIPLE_UPGRADE = new ArrowMultiplierUpgrade("triple_upgrade") {
            @Override
            public void handleAction(BasicBow basicBow, World worldIn, ItemStack bow, PlayerEntity entityplayer, float f, ItemStack arrow, boolean flag1, UpgradeList list, boolean isLoaded) {
                if (arrow.getCount() >= 3 || flag1 || isLoaded) {
                    AbstractArrowEntity entityarrow1 = ArrowUtil.createArrowComplete(worldIn, bow, arrow, entityplayer, basicBow, f, flag1, 0, 2.5f, list, isLoaded);
                    worldIn.addFreshEntity(entityarrow1);
                    if(bow.isEmpty()) {
                        return;
                    }
                    AbstractArrowEntity entityarrow2 = ArrowUtil.createArrowComplete(worldIn, bow, arrow, entityplayer, basicBow, f, flag1, 0, 0, list, isLoaded);
                    worldIn.addFreshEntity(entityarrow2);
                    if(bow.isEmpty()) {
                        return;
                    }
                    AbstractArrowEntity entityarrow3 = ArrowUtil.createArrowComplete(worldIn, bow, arrow, entityplayer, basicBow, f, flag1, 0, -2.5f, list, isLoaded);
                    worldIn.addFreshEntity(entityarrow3);
                } else if (arrow.getCount() == 2) {
                    Upgrades.DOUBLE_UPGRADE.handleAction(basicBow, worldIn, bow, entityplayer, f, arrow, flag1, list, isLoaded);
                } else {
                    AbstractArrowEntity entityarrow = ArrowUtil.createArrowComplete(worldIn, bow, arrow, entityplayer, basicBow, f, flag1, 0, 0, list, isLoaded);

                    worldIn.addFreshEntity(entityarrow);
                }
            }

            @Override
            public void shrinkStack(ItemStack stack) {
                stack.shrink(stack.getCount() >= 3 ? 3 : stack.getCount() >= 2 ? 2 : 1);
            }

            @Override
            public void removeEnergy(ItemStack stack, ExtraBowsEnergy extraBowsEnergy) {
                extraBowsEnergy.extractEnergy(3 * CustomBowSettings.ENERGY_COST_PER_ARROW, false);
            }

            @Override
            public boolean canShoot(ItemStack arrow, PlayerEntity player) {
                return arrow.getCount() >= 3;
            }

            @Override
            public List<ITextComponent> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }

            @Override
            public void initConfigOptions() {
                getItem().setMaxDamage(ExtraBowsConfig.DURABILITY_TRIPLE_UPGRADE.get());
            }
        };

        LIGHTNING_UPGRADE = new ArrowModifierUpgrade("lightning_upgrade") {
            @Override
            public void handleBlockHit(BlockPos pos, World world, PlayerEntity player, AbstractArrowEntity arrow, UpgradeList upgradeList) {
                LightningBoltEntity lightningBoltEntity = new LightningBoltEntity(EntityType.LIGHTNING_BOLT,world);
                lightningBoltEntity.setPos(pos.getX(), pos.getY(), pos.getZ());
                ((ServerWorld) world).addFreshEntity(lightningBoltEntity);
                arrow.remove();
            }

            @Override
            public void handleEntityHit(Entity entity, World world, PlayerEntity player, AbstractArrowEntity arrow, UpgradeList upgradeList) {
                LightningBoltEntity lightningBoltEntity = new LightningBoltEntity(EntityType.LIGHTNING_BOLT,world);
                lightningBoltEntity.setPos(entity.getX(), entity.getY(), entity.getZ());
                ((ServerWorld) world).addFreshEntity(lightningBoltEntity);
                arrow.remove();
            }

            @Override
            public void handleArrowCreate(AbstractArrowEntity arrow, PlayerEntity player, UpgradeList upgradeList) {
                arrow.setSecondsOnFire(10000);
            }

            @Override
            public List<ITextComponent> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }

            @Override
            public void initConfigOptions() {
                getItem().setMaxDamage(ExtraBowsConfig.DURABILITY_LIGHTNING_UPGRADE.get());
            }
        };

        FIRE_UPGRADE = new ArrowModifierUpgrade("fire_upgrade") {
            @Override
            public void handleBlockHit(BlockPos pos, World world, PlayerEntity player, AbstractArrowEntity arrow, UpgradeList upgradeList) {
                pos = pos.offset(0, 1, 0);

                if (world.isEmptyBlock(pos)) {
                    world.setBlock(pos, Blocks.FIRE.defaultBlockState(), 11);
                }
            }

            @Override
            public void handleEntityHit(Entity entity, World world, PlayerEntity player, AbstractArrowEntity arrow, UpgradeList upgradeList) {
                entity.setSecondsOnFire(5);
            }

            @Override
            public void handleArrowCreate(AbstractArrowEntity arrow, PlayerEntity player, UpgradeList upgradeList) {
                arrow.setSecondsOnFire(10000);
            }

            @Override
            public List<ITextComponent> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this, 1), UpgradeUtil.getTranslatedDescriptionForUpgrade(this, 2));
            }

            @Override
            public void initConfigOptions() {
                getItem().setMaxDamage(ExtraBowsConfig.DURABILITY_FIRE_UPGRADE.get());
            }
        };

        ENDER_UPGRADE = new ArrowModifierUpgrade("ender_upgrade") {
            @Override
            public void handleBlockHit(BlockPos pos, World world, PlayerEntity player, AbstractArrowEntity arrow, UpgradeList upgradeList) {
                player.moveTo(pos.getX(), pos.getY() + 1, pos.getZ());
            }

            @Override
            public void handleEntityHit(Entity entity, World world, PlayerEntity player, AbstractArrowEntity arrow, UpgradeList upgradeList) {
                player.moveTo(entity.getX(), entity.getY(), entity.getZ());
            }

            @Override
            public List<ITextComponent> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }

            @Override
            public void initConfigOptions() {
                getItem().setMaxDamage(ExtraBowsConfig.DURABILITY_ENDER_UPGRADE.get());
            }
        };

        FREEZE_UPGRADE = new ArrowModifierUpgrade("freeze_upgrade") {
            @Override
            public void handleBlockHit(BlockPos pos, World world, PlayerEntity player, AbstractArrowEntity arrow, UpgradeList upgradeList) {
                pos = pos.offset(0, 1, 0);

                if (world.isEmptyBlock(pos)) {
                    world.setBlockAndUpdate(pos, Blocks.SNOW.defaultBlockState());
                    if (RandomUtil.isChance(8, 1)) {
                        SnowGolemEntity snowman = new SnowGolemEntity(EntityType.SNOW_GOLEM, world);
                        snowman.setPos(pos.getX(), pos.getY(), pos.getZ());
                        world.addFreshEntity(snowman);
                    }
                }

                int y = pos.getY();

                UpgradeUtil.getBlocksInRadius(pos, 2).forEach((pos1) -> {
                    if (world.isEmptyBlock(pos1) && RandomUtil.isChance(2, 1) && pos1.getY() == y) {
                        world.setBlockAndUpdate(pos1, Blocks.SNOW.defaultBlockState());
                    }
                });
            }

            @Override
            public void handleEntityHit(Entity entity, World world, PlayerEntity player, AbstractArrowEntity arrow, UpgradeList upgradeList) {
                if (entity instanceof LivingEntity) {
                    ((LivingEntity) entity).addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 200, 1));
                }
            }

            @Override
            public void handleWaterHit(BlockPos pos, World world, PlayerEntity player, AbstractArrowEntity arrow, UpgradeList upgradeList) {
                if (world.getBlockState(pos).getBlock().equals(Blocks.WATER)) {
                    world.setBlockAndUpdate(pos, Blocks.PACKED_ICE.defaultBlockState());
                }

                UpgradeUtil.getBlocksInRadius(pos, 2).forEach((pos1) -> {
                    if (world.getBlockState(pos1).getBlock().equals(Blocks.WATER) || world.getBlockState(pos1).getBlock().equals(Blocks.WATER)) {
                        if (world.isEmptyBlock(pos1.above())) {
                            if (RandomUtil.isChance(2, 1)) {
                                world.setBlockAndUpdate(pos1, Blocks.PACKED_ICE.defaultBlockState());
                            }
                            if (RandomUtil.isChance(2, 1)) {
                                if (world.isEmptyBlock(pos1.above().above())) {
                                    world.setBlockAndUpdate(pos1.above(), Blocks.SNOW.defaultBlockState());
                                }
                            }
                        }
                    }
                });
            }

            @Override
            public List<ITextComponent> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }

            @Override
            public void initConfigOptions() {
                getItem().setMaxDamage(ExtraBowsConfig.DURABILITY_FREEZE_UPGRADE.get());
            }
        };

        EXPLOSIVE_UPGRADE = new ArrowModifierUpgrade("explosive_upgrade") {

            @Override
            public void handleBlockHit(BlockPos pos, World world, PlayerEntity player, AbstractArrowEntity arrow, UpgradeList upgradeList) {
                world.explode(player, pos.getX(), pos.getY(), pos.getZ(), 3, Explosion.Mode.DESTROY);
                arrow.remove();
            }


            @Override
            public void handleEntityHit(Entity entity, World world, PlayerEntity player, AbstractArrowEntity arrow, UpgradeList upgradeList) {
                world.explode(player, entity.getX(), entity.getY(), entity.getZ(), 3, Explosion.Mode.DESTROY);
                arrow.remove();
            }

            @Override
            public List<ITextComponent> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }

            @Override
            public void initConfigOptions() {
                getItem().setMaxDamage(ExtraBowsConfig.DURABILITY_EXPLOSIVE_UPGRADE.get());
            }
        };

        HEAL_FROM_DAMAGE = new ArrowModifierUpgrade("heal_from_damage_upgrade") {
            @Override
            public void handleEntityHit(Entity entity, World world, PlayerEntity player, AbstractArrowEntity arrow, UpgradeList upgradeList) {
                if (entity instanceof LivingEntity) {
                    if (RandomUtil.isChance(3, 1)) {
                        player.heal(0.5f);
                    }
                }
            }

            @Override
            public List<ITextComponent> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }

            @Override
            public void initConfigOptions() {
                getItem().setMaxDamage(ExtraBowsConfig.DURABILITY_HEAL_FROM_DAMAGE_UPGRADE.get());
            }
        };

        ARROW_COST = new ArrowModifierUpgrade("arrow_cost_upgrade") {
            @Override
            public void handleArrowCreate(AbstractArrowEntity arrow, PlayerEntity player, UpgradeList upgradeList) {
                if (RandomUtil.isChance(8, 1)) {
                    if (!player.addItem(new ItemStack(Items.ARROW, 1))) {
                        player.drop(new ItemStack(Items.ARROW, 1), true);
                    }
                }
            }

            @Override
            public List<ITextComponent> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }

            @Override
            public void initConfigOptions() {
                getItem().setMaxDamage(ExtraBowsConfig.DURABILITY_ARROW_COST_UPGRADE.get());
            }
        };

        WATER_UPGRADE = new ArrowModifierUpgrade("water_upgrade") {
            @Override
            public void handleBlockHit(BlockPos pos, World world, PlayerEntity player, AbstractArrowEntity arrow, UpgradeList upgradeList) {
                Stream<BlockPos> list = UpgradeUtil.getBlocksInRadius(pos, 3);

                list.forEach((pos1) -> {
                    if (world.getBlockState(pos1).getBlock().equals(Blocks.FIRE)) {
                        world.removeBlock(pos1, false);
                    }
                });

                if (RandomUtil.isChance(6, 1) && !world.dimension().getRegistryName().equals(DimensionType.NETHER_LOCATION.getRegistryName())) {
                    world.setBlockAndUpdate(pos.offset(0, 1, 0), Blocks.WATER.defaultBlockState());
                    TimerUtil.addTimeCommand(new TimeCommand(20 * 5, () -> {
                        world.setBlockAndUpdate(pos.offset(0, 1, 0), Blocks.AIR.defaultBlockState());
                    }));
                }
            }

            @Override
            public List<ITextComponent> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this, 1), UpgradeUtil.getTranslatedDescriptionForUpgrade(this, 2));
            }

            @Override
            public void initConfigOptions() {
                getItem().setMaxDamage(ExtraBowsConfig.DURABILITY_WATER_UPGRADE.get());
            }
        };

        PUSH_UPGRADE = new ArrowModifierUpgrade("push_upgrade") {
            @Override
            public void handleBlockHit(BlockPos pos, World world, PlayerEntity player, AbstractArrowEntity arrow, UpgradeList upgradeList) {
                world.getEntitiesOfClass(LivingEntity.class, UpgradeUtil.getRadiusBoundingBox(pos, 6)).forEach((livingEntity -> {

                    double deltaX = (livingEntity.getX() - arrow.getX());
                    double deltaZ = (livingEntity.getZ() - arrow.getZ());

                    int posX = deltaX > 0 ? 1 : -1;
                    int posZ = deltaZ > 0 ? 1 : -1;

                    deltaX = Math.abs(deltaX);
                    deltaZ = Math.abs(deltaZ);

                    double highestValue = deltaX > deltaZ ? deltaX : deltaZ;

                    livingEntity.push(posX * (deltaX / highestValue), 0.7f, posZ * (deltaZ / highestValue));
                }));
            }

            @Override
            public void handleEntityHit(Entity entity, World world, PlayerEntity player, AbstractArrowEntity arrow, UpgradeList upgradeList) {
                world.getEntitiesOfClass(LivingEntity.class, UpgradeUtil.getRadiusBoundingBox(new BlockPos(entity.position()), 6)).forEach((livingEntity -> {
                    double deltaX = (livingEntity.getX() - arrow.getX());
                    double deltaZ = (livingEntity.getZ() - arrow.getZ());

                    int posX = deltaX > 0 ? 1 : -1;
                    int posZ = deltaZ > 0 ? 1 : -1;

                    deltaX = Math.abs(deltaX);
                    deltaZ = Math.abs(deltaZ);

                    double highestValue = deltaX > deltaZ ? deltaX : deltaZ;

                    livingEntity.push(posX * (deltaX / highestValue), 0.7f, posZ * (deltaZ / highestValue));
                }));
            }

            @Override
            public List<ITextComponent> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }

            @Override
            public void initConfigOptions() {
                getItem().setMaxDamage(ExtraBowsConfig.DURABILITY_PUSH_UPGRADE.get());
            }
        };

        FLYING_UPGRADE = new ArrowModifierUpgrade("flying_upgrade") {
            @Override
            public void handleEntityInit(AbstractArrowEntity arrow, UpgradeList upgradeList, PlayerEntity player) {
                arrow.getTags().add("flyingUpgrade");
                player.startRiding(arrow);
            }

            @Override
            public void handleBlockHit(BlockPos pos, World world, PlayerEntity player, AbstractArrowEntity arrow, UpgradeList upgradeList) {
                if (arrow.isVehicle()) {
                    player.hurt(DamageSource.FALL, 2);
                }
            }

            @Override
            public void handleEntityHit(Entity entity, World world, PlayerEntity player, AbstractArrowEntity arrow, UpgradeList upgradeList) {
                if (arrow.isVehicle()) {
                    player.hurt(DamageSource.FALL, 2);
                }
            }

            @Override
            public List<ITextComponent> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }

            @Override
            public void initConfigOptions() {
                getItem().setMaxDamage(ExtraBowsConfig.DURABILITY_FLY_UPGRADE.get());
            }
        };

        METEOR_UPGRADE = new ArrowModifierUpgrade("meteor_upgrade") {
            @Override
            public void handleBlockHit(BlockPos pos, World world, PlayerEntity player, AbstractArrowEntity arrow, UpgradeList upgradeList) {
                spawnMeteor(world, arrow);
            }

            @Override
            public void handleEntityHit(Entity entity, World world, PlayerEntity player, AbstractArrowEntity arrow, UpgradeList upgradeList) {
                spawnMeteor(world, arrow);
            }

            private void spawnMeteor(World world, AbstractArrowEntity arrow) {
                if (world.isEmptyBlock(new BlockPos(arrow.getX(), arrow.getY() + 20, arrow.getZ()))) {
                    FireballEntity fireballEntity = new FireballEntity(EntityType.FIREBALL, world);
                    fireballEntity.setPos(arrow.getX(), arrow.getY() + 20, arrow.getZ());
                    fireballEntity.push(0, -2f, 0);
                    world.addFreshEntity(fireballEntity);
                }
            }

            @Override
            public List<ITextComponent> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }

            @Override
            public void initConfigOptions() {
                getItem().setMaxDamage(ExtraBowsConfig.DURABILITY_METEOR_UPGRADE.get());
            }
        };

        ELECTRIC_UPGRADE = new ElectricUpgrade();

        BRIDGE_UPGRADE = new BridgeUpgrade();
    }
}
