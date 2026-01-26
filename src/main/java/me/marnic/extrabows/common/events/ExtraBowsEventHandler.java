package me.marnic.extrabows.common.events;

import me.marnic.extrabows.api.upgrade.ArrowModifierUpgrade;
import me.marnic.extrabows.api.upgrade.UpgradeList;
import me.marnic.extrabows.api.upgrade.Upgrades;
import me.marnic.extrabows.api.util.ArrowUtil;
import me.marnic.extrabows.api.util.TimeCommand;
import me.marnic.extrabows.api.util.TimerUtil;
import me.marnic.extrabows.api.util.UpgradeUtil;
import me.marnic.extrabows.common.items.BasicBow;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Copyright (c) 24.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
@Mod.EventBusSubscriber
public class ExtraBowsEventHandler {

    @SubscribeEvent
    public static void worldTickEvent(TickEvent.ServerTickEvent event) {
        TimerUtil.handleTickEvent(event);
    }

    @SubscribeEvent
    public static void blockbreak(BlockEvent.BreakEvent e) {
        if (e.getState().getBlock().equals(ExtraBowsObjects.BRIDGE_BLOCK.get())) {
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
                        LightningBolt lightningBoltEntity = new LightningBolt(EntityType.LIGHTNING_BOLT,e.getEntity().getCommandSenderWorld());
                        lightningBoltEntity.setPos(e.getEntity().getX(),e.getEntity().getY(),e.getEntity().getZ());
                        e.getEntity().getCommandSenderWorld().addFreshEntity(lightningBoltEntity);
                    }
                }));
            }
        }
    }

    @SubscribeEvent
    public static void projectileHit(ProjectileImpactEvent e) {
        
        if(!(e.getEntity() instanceof Arrow)) return;
        
        if (e.getRayTraceResult().getType() != HitResult.Type.MISS) {
            if (e.getEntity() instanceof Arrow) {
                AbstractArrow arrow = (AbstractArrow) e.getEntity();

                if (arrow.getOwner() instanceof Player && UpgradeUtil.isExtraBowsArrow(arrow)) {
                    Player player = (Player) arrow.getOwner();
                    if (e.getRayTraceResult().getType() == HitResult.Type.ENTITY) {
                        EntityHitResult trace = (EntityHitResult) e.getRayTraceResult();
                        if (arrow.getOwner().equals(trace.getEntity())) {
                            if (arrow.getTags().contains("flyingUpgrade")) {
                                e.setResult(Event.Result.DENY);
                                return;
                            }
                        }
                    }

                    if (!arrow.getPersistentData().getBoolean("alreadyHit")) {
                        arrow.getPersistentData().putBoolean("alreadyHit", true);
                        UpgradeList list = ArrowUtil.ARROWS_TO_UPGRADES.get(arrow.getUUID());
                        if (list != null) {
                            if (e.getRayTraceResult().getType() == HitResult.Type.BLOCK) {
                                BlockHitResult trace = (BlockHitResult) e.getRayTraceResult();
                                list.handleModifierHittingEvent(ArrowModifierUpgrade.EventType.BLOCK_HIT, trace.getBlockPos(), null, e.getEntity().getCommandSenderWorld(), player, arrow);
                                list.getBasicBow().onArrowHit(arrow);
                            } else if (e.getRayTraceResult().getType() == HitResult.Type.ENTITY) {
                                EntityHitResult trace = (EntityHitResult) e.getRayTraceResult();
                                list.handleModifierHittingEvent(ArrowModifierUpgrade.EventType.ENTITY_HIT, null, trace.getEntity(), e.getEntity().getCommandSenderWorld(), player, arrow);
                                list.getBasicBow().onArrowHit(arrow);
                            }
                            ArrowUtil.ARROWS_TO_UPGRADES.remove(arrow.getUUID());
                            TimeCommand.EndableTimeCommand command = ((TimeCommand.EndableTimeCommand) TimerUtil.forId(arrow.getId()));

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
    public static void arrowConstructing(EntityJoinLevelEvent e) {
        if (!e.getLevel().isClientSide) {
            if (e.getEntity() instanceof AbstractArrow && UpgradeUtil.isExtraBowsArrow(e.getEntity())) {

                AbstractArrow arrow = (AbstractArrow) e.getEntity();
                if (arrow.getOwner() instanceof Player) {
                    ItemStack bow = ((Player) arrow.getOwner()).getMainHandItem();

                    if (!(bow.getItem() instanceof BasicBow)) {
                        return;
                    }

                    UpgradeList list = UpgradeUtil.getUpgradesFromStack(bow);

                    ArrowUtil.ARROWS_TO_UPGRADES.put(arrow.getUUID(), list);
                    TimeCommand.EndableTimeCommand command = new TimeCommand.EndableTimeCommand(0, arrow.getId());
                    command.setExecute(new Runnable() {

                        boolean prevWater = false;

                        @Override
                        public void run() {
                            if (arrow.isInWater()) {
                                if (!prevWater) {
                                    list.handleModifierHittingEvent(ArrowModifierUpgrade.EventType.WATER_HIT, new BlockPos(arrow.blockPosition()), null, e.getLevel(), (Player) arrow.getOwner(), arrow);
                                }
                            }

                            if (!arrow.getPersistentData().getBoolean("alreadyHit")) {
                                list.handleOnUpdatedEvent(arrow, arrow.getCommandSenderWorld());
                            } else {
                                command.setEnd(true);
                            }

                            prevWater = arrow.isInWater();
                        }
                    });

                    TimerUtil.addTimeCommand(command);

                    list.handleModifierEvent(ArrowModifierUpgrade.EventType.ENTITY_INIT, arrow, (Player) arrow.getOwner(), bow);
                }
            }
        }
    }
}
