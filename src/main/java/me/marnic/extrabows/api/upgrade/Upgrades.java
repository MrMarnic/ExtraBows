package me.marnic.extrabows.api.upgrade;

import me.marnic.extrabows.api.energy.ExtraBowsEnergy;
import me.marnic.extrabows.api.util.*;
import me.marnic.extrabows.common.config.ExtraBowsConfig;
import me.marnic.extrabows.common.items.BasicBow;
import me.marnic.extrabows.common.items.CustomBowSettings;
import me.marnic.extrabows.common.items.upgrades.BasicUpgradeItem;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import me.marnic.extrabows.common.upgrades.BridgeUpgrade;
import me.marnic.extrabows.common.upgrades.ElectricUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.RegistryObject;

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

    public static RegistryObject<BasicUpgradeItem> DOUBLE_UPGRADE_ITEM = register("double_upgrade");
    public static RegistryObject<BasicUpgradeItem> TRIPLE_UPGRADE_ITEM = register("triple_upgrade");
    public static RegistryObject<BasicUpgradeItem> LIGHTNING_UPGRADE_ITEM = register("lightning_upgrade");
    public static RegistryObject<BasicUpgradeItem> FIRE_UPGRADE_ITEM = register("fire_upgrade");
    public static RegistryObject<BasicUpgradeItem> ENDER_UPGRADE_ITEM = register("ender_upgrade");
    public static RegistryObject<BasicUpgradeItem> FREEZE_UPGRADE_ITEM = register("freeze_upgrade");
    public static RegistryObject<BasicUpgradeItem> EXPLOSIVE_UPGRADE_ITEM = register("explosive_upgrade");
    public static RegistryObject<BasicUpgradeItem> WATER_UPGRADE_ITEM = register("water_upgrade");
    public static RegistryObject<BasicUpgradeItem> BRIDGE_UPGRADE_ITEM = register("bridge_upgrade");
    public static RegistryObject<BasicUpgradeItem> PUSH_UPGRADE_ITEM = register("push_upgrade");
    public static RegistryObject<BasicUpgradeItem> FLYING_UPGRADE_ITEM = register("flying_upgrade");
    public static RegistryObject<BasicUpgradeItem> METEOR_UPGRADE_ITEM = register("meteor_upgrade");
    public static RegistryObject<BasicUpgradeItem> HEAL_UPGRADE_ITEM = register("heal_from_damage_upgrade");
    public static RegistryObject<BasicUpgradeItem> ARROW_COST_UPGRADE_ITEM = register("arrow_cost_upgrade");
    public static RegistryObject<BasicUpgradeItem> ELECTRIC_UPGRADE_ITEM = register("electric_upgrade");


    public static RegistryObject<BasicUpgradeItem> register(String name) {
        return ExtraBowsObjects.ITEM_REGISTRY.register(name, () -> {
            return new BasicUpgradeItem(name, BasicUpgrade.UPGRADES.get(name));
        });
    }

    public static void init() {
        DOUBLE_UPGRADE = new ArrowMultiplierUpgrade("double_upgrade", DOUBLE_UPGRADE_ITEM) {
            @Override
            public void handleAction(BasicBow basicBow, Level levelIn, ItemStack bow, Player entityplayer, float f, ItemStack arrow, boolean flag1, UpgradeList list, boolean isLoaded) {

                if (arrow.getCount() >= 2 || flag1 || isLoaded) {
                    AbstractArrow entityarrow2 = ArrowUtil.createArrowComplete(levelIn, bow, arrow, entityplayer, basicBow, f, flag1, 0, 2.5f, list, isLoaded);
                    levelIn.addFreshEntity(entityarrow2);

                    if(bow.isEmpty()) {
                        return;
                    }

                    AbstractArrow entityarrow1 = ArrowUtil.createArrowComplete(levelIn, bow, arrow, entityplayer, basicBow, f, flag1, 0, -2.5f, list, isLoaded);
                    levelIn.addFreshEntity(entityarrow1);
                } else {
                    AbstractArrow entityarrow = ArrowUtil.createArrowComplete(levelIn, bow, arrow, entityplayer, basicBow, f, flag1, 0, 0, list, isLoaded);
                    levelIn.addFreshEntity(entityarrow);
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
            public boolean canShoot(ItemStack arrow, Player player) {
                return arrow.getCount() >= 2;
            }

            @Override
            public List<Component> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }

            @Override
            public void initConfigOptions() {
                getItem().setMaxDamage(ExtraBowsConfig.DURABILITY_DOUBLE_UPGRADE.get());
            }
        };
        TRIPLE_UPGRADE = new ArrowMultiplierUpgrade("triple_upgrade", TRIPLE_UPGRADE_ITEM) {
            @Override
            public void handleAction(BasicBow basicBow, Level levelIn, ItemStack bow, Player entityplayer, float f, ItemStack arrow, boolean flag1, UpgradeList list, boolean isLoaded) {
                if (arrow.getCount() >= 3 || flag1 || isLoaded) {
                    AbstractArrow entityarrow1 = ArrowUtil.createArrowComplete(levelIn, bow, arrow, entityplayer, basicBow, f, flag1, 0, 2.5f, list, isLoaded);
                    levelIn.addFreshEntity(entityarrow1);
                    if(bow.isEmpty()) {
                        return;
                    }
                    AbstractArrow entityarrow2 = ArrowUtil.createArrowComplete(levelIn, bow, arrow, entityplayer, basicBow, f, flag1, 0, 0, list, isLoaded);
                    levelIn.addFreshEntity(entityarrow2);
                    if(bow.isEmpty()) {
                        return;
                    }
                    AbstractArrow entityarrow3 = ArrowUtil.createArrowComplete(levelIn, bow, arrow, entityplayer, basicBow, f, flag1, 0, -2.5f, list, isLoaded);
                    levelIn.addFreshEntity(entityarrow3);
                } else if (arrow.getCount() == 2) {
                    Upgrades.DOUBLE_UPGRADE.handleAction(basicBow, levelIn, bow, entityplayer, f, arrow, flag1, list, isLoaded);
                } else {
                    AbstractArrow entityarrow = ArrowUtil.createArrowComplete(levelIn, bow, arrow, entityplayer, basicBow, f, flag1, 0, 0, list, isLoaded);

                    levelIn.addFreshEntity(entityarrow);
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
            public boolean canShoot(ItemStack arrow, Player player) {
                return arrow.getCount() >= 3;
            }

            @Override
            public List<Component> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }

            @Override
            public void initConfigOptions() {
                getItem().setMaxDamage(ExtraBowsConfig.DURABILITY_TRIPLE_UPGRADE.get());
            }
        };

        LIGHTNING_UPGRADE = new ArrowModifierUpgrade("lightning_upgrade", LIGHTNING_UPGRADE_ITEM) {
            @Override
            public void handleBlockHit(BlockPos pos, Level level, Player player, AbstractArrow arrow, UpgradeList upgradeList) {
                LightningBolt lightningBoltEntity = new LightningBolt(EntityType.LIGHTNING_BOLT,level);
                lightningBoltEntity.setPos(pos.getX(), pos.getY(), pos.getZ());
                ((ServerLevel) level).addFreshEntity(lightningBoltEntity);
                arrow.remove(Entity.RemovalReason.DISCARDED);
            }

            @Override
            public void handleEntityHit(Entity entity, Level level, Player player, AbstractArrow arrow, UpgradeList upgradeList) {
                LightningBolt lightningBoltEntity = new LightningBolt(EntityType.LIGHTNING_BOLT,level);
                lightningBoltEntity.setPos(entity.getX(), entity.getY(), entity.getZ());
                ((ServerLevel) level).addFreshEntity(lightningBoltEntity);
                arrow.remove(Entity.RemovalReason.DISCARDED);
            }

            @Override
            public void handleArrowCreate(AbstractArrow arrow, Player player, UpgradeList upgradeList) {
                arrow.setSecondsOnFire(10000);
            }

            @Override
            public List<Component> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }

            @Override
            public void initConfigOptions() {
                getItem().setMaxDamage(ExtraBowsConfig.DURABILITY_LIGHTNING_UPGRADE.get());
            }
        };

        FIRE_UPGRADE = new ArrowModifierUpgrade("fire_upgrade", FIRE_UPGRADE_ITEM) {
            @Override
            public void handleBlockHit(BlockPos pos, Level level, Player player, AbstractArrow arrow, UpgradeList upgradeList) {
                pos = pos.offset(0, 1, 0);

                if (level.isEmptyBlock(pos)) {
                    level.setBlock(pos, Blocks.FIRE.defaultBlockState(), 11);
                }
            }

            @Override
            public void handleEntityHit(Entity entity, Level level, Player player, AbstractArrow arrow, UpgradeList upgradeList) {
                entity.setSecondsOnFire(5);
            }

            @Override
            public void handleArrowCreate(AbstractArrow arrow, Player player, UpgradeList upgradeList) {
                arrow.setSecondsOnFire(10000);
            }

            @Override
            public List<Component> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this, 1), UpgradeUtil.getTranslatedDescriptionForUpgrade(this, 2));
            }

            @Override
            public void initConfigOptions() {
                getItem().setMaxDamage(ExtraBowsConfig.DURABILITY_FIRE_UPGRADE.get());
            }
        };

        ENDER_UPGRADE = new ArrowModifierUpgrade("ender_upgrade", ENDER_UPGRADE_ITEM) {
            @Override
            public void handleBlockHit(BlockPos pos, Level level, Player player, AbstractArrow arrow, UpgradeList upgradeList) {
                player.moveTo(pos.getX(), pos.getY() + 1, pos.getZ());
            }

            @Override
            public void handleEntityHit(Entity entity, Level level, Player player, AbstractArrow arrow, UpgradeList upgradeList) {
                player.moveTo(entity.getX(), entity.getY(), entity.getZ());
            }

            @Override
            public List<Component> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }

            @Override
            public void initConfigOptions() {
                getItem().setMaxDamage(ExtraBowsConfig.DURABILITY_ENDER_UPGRADE.get());
            }
        };

        FREEZE_UPGRADE = new ArrowModifierUpgrade("freeze_upgrade", FREEZE_UPGRADE_ITEM) {
            @Override
            public void handleBlockHit(BlockPos pos, Level level, Player player, AbstractArrow arrow, UpgradeList upgradeList) {
                pos = pos.offset(0, 1, 0);

                if (level.isEmptyBlock(pos)) {
                    level.setBlockAndUpdate(pos, Blocks.SNOW.defaultBlockState());
                    if (RandomUtil.isChance(8, 1)) {
                        SnowGolem snowman = new SnowGolem(EntityType.SNOW_GOLEM, level);
                        snowman.setPos(pos.getX(), pos.getY(), pos.getZ());
                        level.addFreshEntity(snowman);
                    }
                }

                int y = pos.getY();

                UpgradeUtil.getBlocksInRadius(pos, 2).forEach((pos1) -> {
                    if (level.isEmptyBlock(pos1) && RandomUtil.isChance(2, 1) && pos1.getY() == y) {
                        level.setBlockAndUpdate(pos1, Blocks.SNOW.defaultBlockState());
                    }
                });
            }

            @Override
            public void handleEntityHit(Entity entity, Level level, Player player, AbstractArrow arrow, UpgradeList upgradeList) {
                if (entity instanceof LivingEntity) {
                    ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1));
                }
            }

            @Override
            public void handleWaterHit(BlockPos pos, Level level, Player player, AbstractArrow arrow, UpgradeList upgradeList) {
                if (level.getBlockState(pos).getBlock().equals(Blocks.WATER)) {
                    level.setBlockAndUpdate(pos, Blocks.PACKED_ICE.defaultBlockState());
                }

                UpgradeUtil.getBlocksInRadius(pos, 2).forEach((pos1) -> {
                    if (level.getBlockState(pos1).getBlock().equals(Blocks.WATER) || level.getBlockState(pos1).getBlock().equals(Blocks.WATER)) {
                        if (level.isEmptyBlock(pos1.above())) {
                            if (RandomUtil.isChance(2, 1)) {
                                level.setBlockAndUpdate(pos1, Blocks.PACKED_ICE.defaultBlockState());
                            }
                            if (RandomUtil.isChance(2, 1)) {
                                if (level.isEmptyBlock(pos1.above().above())) {
                                    level.setBlockAndUpdate(pos1.above(), Blocks.SNOW.defaultBlockState());
                                }
                            }
                        }
                    }
                });
            }

            @Override
            public List<Component> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }

            @Override
            public void initConfigOptions() {
                getItem().setMaxDamage(ExtraBowsConfig.DURABILITY_FREEZE_UPGRADE.get());
            }
        };

        EXPLOSIVE_UPGRADE = new ArrowModifierUpgrade("explosive_upgrade", EXPLOSIVE_UPGRADE_ITEM) {

            @Override
            public void handleBlockHit(BlockPos pos, Level level, Player player, AbstractArrow arrow, UpgradeList upgradeList) {
                level.explode(player, pos.getX(), pos.getY(), pos.getZ(), 3, Level.ExplosionInteraction.TNT);
                arrow.remove(Entity.RemovalReason.DISCARDED);
            }


            @Override
            public void handleEntityHit(Entity entity, Level level, Player player, AbstractArrow arrow, UpgradeList upgradeList) {
                level.explode(player, entity.getX(), entity.getY(), entity.getZ(), 3, Level.ExplosionInteraction.TNT);
                arrow.remove(Entity.RemovalReason.DISCARDED);
            }

            @Override
            public List<Component> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }

            @Override
            public void initConfigOptions() {
                getItem().setMaxDamage(ExtraBowsConfig.DURABILITY_EXPLOSIVE_UPGRADE.get());
            }
        };

        HEAL_FROM_DAMAGE = new ArrowModifierUpgrade("heal_from_damage_upgrade", HEAL_UPGRADE_ITEM) {
            @Override
            public void handleEntityHit(Entity entity, Level level, Player player, AbstractArrow arrow, UpgradeList upgradeList) {
                if (entity instanceof LivingEntity) {
                    if (RandomUtil.isChance(3, 1)) {
                        player.heal(0.5f);
                    }
                }
            }

            @Override
            public List<Component> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }

            @Override
            public void initConfigOptions() {
                getItem().setMaxDamage(ExtraBowsConfig.DURABILITY_HEAL_FROM_DAMAGE_UPGRADE.get());
            }
        };

        ARROW_COST = new ArrowModifierUpgrade("arrow_cost_upgrade", ARROW_COST_UPGRADE_ITEM) {
            @Override
            public void handleArrowCreate(AbstractArrow arrow, Player player, UpgradeList upgradeList) {
                if (RandomUtil.isChance(8, 1)) {
                    if (!player.addItem(new ItemStack(Items.ARROW, 1))) {
                        player.drop(new ItemStack(Items.ARROW, 1), true);
                    }
                }
            }

            @Override
            public List<Component> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }

            @Override
            public void initConfigOptions() {
                getItem().setMaxDamage(ExtraBowsConfig.DURABILITY_ARROW_COST_UPGRADE.get());
            }
        };

        WATER_UPGRADE = new ArrowModifierUpgrade("water_upgrade", WATER_UPGRADE_ITEM) {
            @Override
            public void handleBlockHit(BlockPos pos, Level level, Player player, AbstractArrow arrow, UpgradeList upgradeList) {
                Stream<BlockPos> list = UpgradeUtil.getBlocksInRadius(pos, 3);

                list.forEach((pos1) -> {
                    if (level.getBlockState(pos1).getBlock().equals(Blocks.FIRE)) {
                        level.removeBlock(pos1, false);
                    }
                });

                if (RandomUtil.isChance(6, 1) && !level.dimension().equals(Level.NETHER)) {
                    level.setBlockAndUpdate(pos.offset(0, 1, 0), Blocks.WATER.defaultBlockState());
                    TimerUtil.addTimeCommand(new TimeCommand(20 * 5, () -> {
                        level.setBlockAndUpdate(pos.offset(0, 1, 0), Blocks.AIR.defaultBlockState());
                    }));
                }
            }

            @Override
            public List<Component> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this, 1), UpgradeUtil.getTranslatedDescriptionForUpgrade(this, 2));
            }

            @Override
            public void initConfigOptions() {
                getItem().setMaxDamage(ExtraBowsConfig.DURABILITY_WATER_UPGRADE.get());
            }
        };

        PUSH_UPGRADE = new ArrowModifierUpgrade("push_upgrade", PUSH_UPGRADE_ITEM) {
            @Override
            public void handleBlockHit(BlockPos pos, Level level, Player player, AbstractArrow arrow, UpgradeList upgradeList) {
                level.getEntitiesOfClass(LivingEntity.class, UpgradeUtil.getRadiusBoundingBox(pos, 6)).forEach((livingEntity -> {

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
            public void handleEntityHit(Entity entity, Level level, Player player, AbstractArrow arrow, UpgradeList upgradeList) {
                level.getEntitiesOfClass(LivingEntity.class, UpgradeUtil.getRadiusBoundingBox(new BlockPos(entity.blockPosition()), 6)).forEach((livingEntity -> {
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
            public List<Component> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }

            @Override
            public void initConfigOptions() {
                getItem().setMaxDamage(ExtraBowsConfig.DURABILITY_PUSH_UPGRADE.get());
            }
        };

        FLYING_UPGRADE = new ArrowModifierUpgrade("flying_upgrade", FLYING_UPGRADE_ITEM) {
            @Override
            public void handleEntityInit(AbstractArrow arrow, UpgradeList upgradeList, Player player) {
                arrow.getTags().add("flyingUpgrade");
                player.startRiding(arrow);
            }

            @Override
            public void handleBlockHit(BlockPos pos, Level level, Player player, AbstractArrow arrow, UpgradeList upgradeList) {
                if (arrow.isVehicle()) {
                    player.hurt(level.damageSources().fall(), 2);
                }
            }

            @Override
            public void handleEntityHit(Entity entity, Level level, Player player, AbstractArrow arrow, UpgradeList upgradeList) {
                if (arrow.isVehicle()) {
                    player.hurt(level.damageSources().fall(), 2);
                }
            }

            @Override
            public List<Component> getDescription() {
                return UpgradeUtil.createDescriptionFromStingList(UpgradeUtil.getTranslatedDescriptionForUpgrade(this));
            }

            @Override
            public void initConfigOptions() {
                getItem().setMaxDamage(ExtraBowsConfig.DURABILITY_FLY_UPGRADE.get());
            }
        };

        METEOR_UPGRADE = new ArrowModifierUpgrade("meteor_upgrade", METEOR_UPGRADE_ITEM) {
            @Override
            public void handleBlockHit(BlockPos pos, Level level, Player player, AbstractArrow arrow, UpgradeList upgradeList) {
                spawnMeteor(level, arrow);
            }

            @Override
            public void handleEntityHit(Entity entity, Level level, Player player, AbstractArrow arrow, UpgradeList upgradeList) {
                spawnMeteor(level, arrow);
            }

            private void spawnMeteor(Level level, AbstractArrow arrow) {
                if (level.isEmptyBlock(new BlockPos(arrow.getBlockX(), arrow.getBlockY() + 20, arrow.getBlockZ()))) {
                    Fireball fireballEntity = new LargeFireball(EntityType.FIREBALL, level);
                    fireballEntity.setPos(arrow.getX(), arrow.getY() + 20, arrow.getZ());
                    fireballEntity.push(0, -2f, 0);
                    level.addFreshEntity(fireballEntity);
                }
            }

            @Override
            public List<Component> getDescription() {
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
