package me.marnic.extrabows.common.events;

import me.marnic.extrabows.api.upgrade.ArrowModifierUpgrade;
import me.marnic.extrabows.api.upgrade.UpgradeList;
import me.marnic.extrabows.api.upgrade.Upgrades;
import me.marnic.extrabows.api.util.ArrowUtil;
import me.marnic.extrabows.api.util.TimeCommand;
import me.marnic.extrabows.api.util.TimerUtil;
import me.marnic.extrabows.api.util.UpgradeUtil;
import me.marnic.extrabows.client.input.ExtraBowsInputHandler;
import me.marnic.extrabows.common.items.BasicBow;
import me.marnic.extrabows.common.items.BowSettings;
import me.marnic.extrabows.common.items.CustomBowSettings;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import me.marnic.extrabows.common.main.Identification;
import me.marnic.extrabows.common.recipes.BasicBowRecipe;
import me.marnic.extrabows.common.recipes.BasicBowShapelessRecipe;
import me.marnic.extrabows.common.registry.ExtraBowsRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;

/**
 * Copyright (c) 24.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
@Mod.EventBusSubscriber
public class ExtraBowsEventHandler {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void keyInput(InputEvent.KeyInputEvent e) {
        ExtraBowsInputHandler.handleKeyPressedEvent(e);
    }

    @SubscribeEvent
    public static void worldTickEvent(TickEvent.ServerTickEvent event) {
        TimerUtil.handleTickEvent(event);
    }

    @SubscribeEvent
    public static void blockbreak(BlockEvent.BreakEvent e) {
        if (e.getState().getBlock().equals(ExtraBowsObjects.BRIDGE_BLOCK)) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void itemCrafted(PlayerEvent.ItemCraftedEvent e) {
        if (e.getCrafting().getItem().equals(Upgrades.LIGHTNING_UPGRADE.getItem())) {
            for (int i = 1; i < 14; i++) {
                TimerUtil.addTimeCommand(new TimeCommand(20 * i, new Runnable() {
                    @Override
                    public void run() {
                        ((ServerWorld)e.getPlayer().world).addLightningBolt(new LightningBoltEntity(e.getPlayer().world, e.getPlayer().getPosition().getX(), e.getPlayer().getPosition().getY(), e.getPlayer().getPosition().getZ(), false));
                    }
                }));
            }
        }
    }

    @SubscribeEvent
    public static void projectileHit(ProjectileImpactEvent.Arrow e) {
        if(e.getRayTraceResult().getType() != RayTraceResult.Type.MISS) {
            if (e.getEntity() instanceof ArrowEntity) {
                AbstractArrowEntity arrow = (AbstractArrowEntity) e.getEntity();

                if (arrow.getShooter() instanceof PlayerEntity && UpgradeUtil.isExtraBowsArrow(arrow)) {
                    PlayerEntity player = (PlayerEntity) arrow.getShooter();
                    if (e.getRayTraceResult().getType() == RayTraceResult.Type.ENTITY) {
                        EntityRayTraceResult trace = (EntityRayTraceResult) e.getRayTraceResult();
                        if (arrow.shootingEntity.equals(trace.getEntity())) {
                            if (arrow.getTags().contains("flyingUpgrade")) {
                                e.setCanceled(true);
                            }
                        }
                    }

                    if (!arrow.getPersistentData().getBoolean("alreadyHit")) {
                        arrow.getPersistentData().putBoolean("alreadyHit", true);
                        UpgradeList list = ArrowUtil.ARROWS_TO_UPGRADES.get(arrow.getUniqueID());
                        if (list != null) {
                            if (e.getRayTraceResult().getType() == RayTraceResult.Type.BLOCK) {
                                BlockRayTraceResult trace = (BlockRayTraceResult) e.getRayTraceResult();
                                list.handleModifierHittingEvent(ArrowModifierUpgrade.EventType.BLOCK_HIT, trace.getPos(), null, e.getEntity().world, player, arrow);
                                list.getBasicBow().onArrowHit(arrow);
                            } else if (e.getRayTraceResult().getType() == RayTraceResult.Type.ENTITY) {
                                EntityRayTraceResult trace = (EntityRayTraceResult) e.getRayTraceResult();
                                list.handleModifierHittingEvent(ArrowModifierUpgrade.EventType.ENTITY_HIT, null, trace.getEntity(), e.getEntity().world, player, arrow);
                                list.getBasicBow().onArrowHit(arrow);
                            }
                            ArrowUtil.ARROWS_TO_UPGRADES.remove(arrow.getUniqueID());
                            TimeCommand.EndableTimeCommand command = ((TimeCommand.EndableTimeCommand) TimerUtil.forId(arrow.getEntityId()));

                            if (command != null) {
                                command.setEnd(true);
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void arrowConstructing(EntityJoinWorldEvent e) {
        if (!e.getWorld().isRemote) {
            if (e.getEntity() instanceof AbstractArrowEntity && UpgradeUtil.isExtraBowsArrow(e.getEntity())) {

                AbstractArrowEntity arrow = (AbstractArrowEntity) e.getEntity();
                if (arrow.getShooter() instanceof PlayerEntity) {
                    ItemStack bow = ((PlayerEntity) arrow.getShooter()).getHeldItemMainhand();

                    UpgradeList list = UpgradeUtil.getUpgradesFromStack(bow);

                    ArrowUtil.ARROWS_TO_UPGRADES.put(arrow.getUniqueID(), list);
                    TimeCommand.EndableTimeCommand command = new TimeCommand.EndableTimeCommand(0, arrow.getEntityId());
                    command.setExecute(new Runnable() {

                        boolean prevWater = false;

                        @Override
                        public void run() {
                            if (arrow.isInWater()) {
                                if (!prevWater) {
                                    list.handleModifierHittingEvent(ArrowModifierUpgrade.EventType.WATER_HIT, arrow.getPosition(), null, e.getWorld(), (PlayerEntity) arrow.getShooter(), arrow);
                                }
                            }

                            if (!arrow.getPersistentData().getBoolean("alreadyHit")) {
                                list.handleOnUpdatedEvent(arrow, arrow.world);
                            } else {
                                command.setEnd(true);
                            }

                            prevWater = arrow.isInWater();
                        }
                    });

                    TimerUtil.addTimeCommand(command);

                    list.handleModifierEvent(ArrowModifierUpgrade.EventType.ENTITY_INIT, arrow, (PlayerEntity) arrow.getShooter(), bow);
                }
            }
        }
    }
}
