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
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
                        LightningBoltEntity lightningBoltEntity = new LightningBoltEntity(EntityType.LIGHTNING_BOLT,e.getPlayer().world);
                        lightningBoltEntity.setPosition(e.getPlayer().getPosX(),e.getPlayer().getPosY(),e.getPlayer().getPosZ());
                        ((ServerWorld) e.getPlayer().world).addEntity(lightningBoltEntity);
                    }
                }));
            }
        }
    }

    @SubscribeEvent
    public static void projectileHit(ProjectileImpactEvent.Arrow e) {
        if (e.getRayTraceResult().getType() != RayTraceResult.Type.MISS) {
            if (e.getEntity() instanceof ArrowEntity) {
                AbstractArrowEntity arrow = (AbstractArrowEntity) e.getEntity();

                if (arrow.func_234616_v_() instanceof PlayerEntity && UpgradeUtil.isExtraBowsArrow(arrow)) {
                    PlayerEntity player = (PlayerEntity) arrow.func_234616_v_();
                    if (e.getRayTraceResult().getType() == RayTraceResult.Type.ENTITY) {
                        EntityRayTraceResult trace = (EntityRayTraceResult) e.getRayTraceResult();
                        if (arrow.func_234616_v_().equals(trace.getEntity())) {
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
                if (arrow.func_234616_v_() instanceof PlayerEntity) {
                    ItemStack bow = ((PlayerEntity) arrow.func_234616_v_()).getHeldItemMainhand();

                    if (!(bow.getItem() instanceof BasicBow)) {
                        return;
                    }

                    UpgradeList list = UpgradeUtil.getUpgradesFromStack(bow);

                    ArrowUtil.ARROWS_TO_UPGRADES.put(arrow.getUniqueID(), list);
                    TimeCommand.EndableTimeCommand command = new TimeCommand.EndableTimeCommand(0, arrow.getEntityId());
                    command.setExecute(new Runnable() {

                        boolean prevWater = false;

                        @Override
                        public void run() {
                            if (arrow.isInWater()) {
                                if (!prevWater) {
                                    list.handleModifierHittingEvent(ArrowModifierUpgrade.EventType.WATER_HIT, new BlockPos(arrow.getPositionVec()), null, e.getWorld(), (PlayerEntity) arrow.func_234616_v_(), arrow);
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

                    list.handleModifierEvent(ArrowModifierUpgrade.EventType.ENTITY_INIT, arrow, (PlayerEntity) arrow.func_234616_v_(), bow);
                }
            }
        }
    }
}
