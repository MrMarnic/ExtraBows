package me.marnic.extrabows.common.events;

import me.marnic.extrabows.api.item.IModelRegistry;
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
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Copyright (c) 24.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
@Mod.EventBusSubscriber
public class ExtraBowsEventHandler {

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> e) {
        for (Item item : ExtraBowsRegistry.ITEMS_TO_REGISTER) {
            e.getRegistry().register(item);
        }

        e.getRegistry().register(new BasicBow(new CustomBowSettings("minecraft:bow")).setCreativeTab(CreativeTabs.COMBAT).setUnlocalizedName("bow"));
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> e) {
        for (Block block : ExtraBowsRegistry.BLOCKS_TO_REGISTER) {
            e.getRegistry().register(block);
        }
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent e) {
        for (IModelRegistry registry : ExtraBowsRegistry.MODELS_TO_REGISTER) {
            registry.registerModel();
        }
    }

    @SideOnly(Side.CLIENT)
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
        if (e.crafting.getItem().equals(Upgrades.LIGHTNING_UPGRADE.getItem())) {
            for (int i = 1; i < 14; i++) {
                TimerUtil.addTimeCommand(new TimeCommand(20 * i, new Runnable() {
                    @Override
                    public void run() {
                        e.player.world.addWeatherEffect(new EntityLightningBolt(e.player.world, e.player.posX, e.player.posY, e.player.posZ, false));
                    }
                }));
            }
        }
    }

    @SubscribeEvent
    public static void projectileHit(ProjectileImpactEvent e) {
        if (e.getEntity() instanceof EntityArrow) {
            EntityArrow arrow = (EntityArrow) e.getEntity();
            if (arrow.shootingEntity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) arrow.shootingEntity;
                if (!arrow.getEntityData().getBoolean("alreadyHit")) {
                    arrow.getEntityData().setBoolean("alreadyHit", true);
                    UpgradeList list = ArrowUtil.ARROWS_TO_UPGRADES.get(arrow.getUniqueID());
                    if (list != null) {
                        if (e.getRayTraceResult().typeOfHit == RayTraceResult.Type.BLOCK) {
                            list.handleModifierHittingEvent(ArrowModifierUpgrade.EventType.BLOCK_HIT, e.getRayTraceResult().getBlockPos(), null, e.getEntity().world, player, arrow);
                        } else if (e.getRayTraceResult().typeOfHit == RayTraceResult.Type.ENTITY) {
                            list.handleModifierHittingEvent(ArrowModifierUpgrade.EventType.ENTITY_HIT, null, e.getRayTraceResult().entityHit, e.getEntity().world, player, arrow);
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

    @SubscribeEvent
    public static void arrowConstructing(EntityJoinWorldEvent e) {
        if (!e.getWorld().isRemote) {
            if (e.getEntity() instanceof EntityArrow) {
                EntityArrow arrow = (EntityArrow) e.getEntity();
                if (arrow.shootingEntity instanceof EntityPlayerMP) {
                    ItemStack bow = ((EntityPlayer) arrow.shootingEntity).getHeldItemMainhand();

                    UpgradeList list = UpgradeUtil.getUpgradesFromStackNEW(bow);

                    ArrowUtil.ARROWS_TO_UPGRADES.put(arrow.getUniqueID(), list);
                    TimeCommand.EndableTimeCommand command = new TimeCommand.EndableTimeCommand(0, arrow.getEntityId());
                    command.setExecute(new Runnable() {

                        boolean prevWater = false;

                        @Override
                        public void run() {
                            if (arrow.isInWater()) {
                                if (!prevWater) {
                                    list.handleModifierHittingEvent(ArrowModifierUpgrade.EventType.WATER_HIT, arrow.getPosition(), null, e.getWorld(), (EntityPlayer) arrow.shootingEntity, arrow);
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

                    list.handleModifierEvent(ArrowModifierUpgrade.EventType.ENTITY_INIT, arrow, (EntityPlayer) arrow.shootingEntity, bow);
                }
            }
        }
    }
}
