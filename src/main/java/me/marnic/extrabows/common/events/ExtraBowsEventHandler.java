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
import me.marnic.extrabows.common.items.CustomBowSettings;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import me.marnic.extrabows.common.registry.ExtraBowsRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpectralArrowItem;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

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
        if(e.getState().getBlock().equals(ExtraBowsObjects.BRIDGE_BLOCK)) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void itemCrafted(PlayerEvent.ItemCraftedEvent e) {
        if(e.getCrafting().getItem().equals(Upgrades.LIGHTNING_UPGRADE.getItem())) {
            for(int i = 1;i<14;i++) {
                TimerUtil.addTimeCommand(new TimeCommand(20*i, new Runnable() {
                    @Override
                    public void run() {
                        e.getPlayer().world.addEntity(new LightningBoltEntity(e.getPlayer().world,e.getPlayer().posX,e.getPlayer().posY,e.getPlayer().posZ,false));
                    }
                }));
            }
        }
    }

    @SubscribeEvent
    public static void projectileHit(ProjectileImpactEvent.Arrow e) {
        if(!e.getArrow().world.isRemote) {
            RayTraceResult result = e.getRayTraceResult();
            if(e.getArrow().getShooter() instanceof ServerPlayerEntity) {
                ServerPlayerEntity playerEntity = (ServerPlayerEntity) e.getArrow().getShooter();
                if(!e.getArrow().getEntityData().getBoolean("alreadyHit") && result.getType() != RayTraceResult.Type.MISS) {
                    UpgradeList list = ArrowUtil.ARROWS_TO_UPGRADES.get(e.getArrow().getUniqueID());
                    if(list != null) {
                        e.getArrow().getEntityData().putBoolean("alreadyHit",true);
                        if(result.getType()== RayTraceResult.Type.BLOCK) {
                            BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) result;
                            list.handleModifierHittingEvent(ArrowModifierUpgrade.EventType.BLOCK_HIT, blockRayTraceResult.getPos(), null, e.getEntity().world, playerEntity, e.getArrow());
                        }else if(result.getType() == RayTraceResult.Type.ENTITY) {
                            EntityRayTraceResult entityRayTraceResult = (EntityRayTraceResult) result;
                            list.handleModifierHittingEvent(ArrowModifierUpgrade.EventType.ENTITY_HIT, null, entityRayTraceResult.getEntity(), e.getEntity().world, playerEntity, e.getArrow());
                        }

                        ArrowUtil.ARROWS_TO_UPGRADES.remove(e.getArrow().getUniqueID());
                        TimeCommand.EndableTimeCommand command = ((TimeCommand.EndableTimeCommand) TimerUtil.forId(e.getArrow().getEntityId()));

                        if (command != null) {
                            command.setEnd(true);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void arrowConstructing(EntityJoinWorldEvent e) {
        if (!e.getWorld().isRemote) {
            if (e.getEntity() instanceof AbstractArrowEntity) {
                AbstractArrowEntity arrow = (AbstractArrowEntity) e.getEntity();
                if (arrow.getShooter() instanceof ServerPlayerEntity) {
                    ItemStack bow = ((PlayerEntity) arrow.getShooter()).getHeldItemMainhand();

                    UpgradeList list = UpgradeUtil.getUpgradesFromStackNEW(bow);

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

                            if (!arrow.getEntityData().getBoolean("alreadyHit")) {
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
