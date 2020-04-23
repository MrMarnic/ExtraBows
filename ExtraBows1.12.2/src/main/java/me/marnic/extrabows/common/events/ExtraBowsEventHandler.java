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
import me.marnic.extrabows.common.items.BowSettings;
import me.marnic.extrabows.common.items.CustomBowSettings;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import me.marnic.extrabows.common.main.Identification;
import me.marnic.extrabows.common.packet.ExtraBowsPacketHandler;
import me.marnic.extrabows.common.packet.PacketUpdateArrow;
import me.marnic.extrabows.common.recipes.BasicBowRecipe;
import me.marnic.extrabows.common.recipes.BasicBowShapelessRecipe;
import me.marnic.extrabows.common.registry.ExtraBowsRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
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

import java.util.HashMap;

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

        //Not working completely: Needs fix onArrowLoose
        //e.getRegistry().register(new BasicBow(new CustomBowSettings("minecraft:bow")).setCreativeTab(CreativeTabs.COMBAT).setUnlocalizedName("bow"));
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

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> e) {
        HashMap<ResourceLocation, IRecipe> bowRecipes = new HashMap<>();

        e.getRegistry().getValuesCollection().stream().filter(r -> r.getRegistryName().getResourceDomain().equalsIgnoreCase(Identification.MODID) && r.getRegistryName().getResourcePath().contains("bow")).forEach(r -> {
            bowRecipes.put(r.getRegistryName(), r);
        });

        bowRecipes.forEach((k, v) -> {
            if (k.getResourcePath().contains("stone_bow")) {
                BasicBowRecipe basicBowRecipe = (BasicBowRecipe) new BasicBowRecipe(k.getResourcePath(), v.getIngredients(), new ItemStack(ExtraBowsObjects.STONE_BOW), BowSettings.STONE).setRegistryName(k);
                e.getRegistry().register(basicBowRecipe);
                e.getRegistry().register(new BasicBowShapelessRecipe(basicBowRecipe, ExtraBowsObjects.STONE_UPGRADE_KIT));
            }
            if (k.getResourcePath().contains("golden_bow")) {
                BasicBowRecipe basicBowRecipe = (BasicBowRecipe) new BasicBowRecipe(k.getResourcePath(), v.getIngredients(), new ItemStack(ExtraBowsObjects.GOLD_BOW), BowSettings.GOLD).setRegistryName(k);
                e.getRegistry().register(basicBowRecipe);
                e.getRegistry().register(new BasicBowShapelessRecipe(basicBowRecipe, ExtraBowsObjects.GOLD_UPGRADE_KIT));
            }
            if (k.getResourcePath().contains("iron_bow")) {
                BasicBowRecipe basicBowRecipe = (BasicBowRecipe) new BasicBowRecipe(k.getResourcePath(), v.getIngredients(), new ItemStack(ExtraBowsObjects.IRON_BOW), BowSettings.IRON).setRegistryName(k);
                e.getRegistry().register(basicBowRecipe);
                e.getRegistry().register(new BasicBowShapelessRecipe(basicBowRecipe, ExtraBowsObjects.IRON_UPGRADE_KIT));
            }
            if (k.getResourcePath().contains("diamond_bow")) {
                BasicBowRecipe basicBowRecipe = (BasicBowRecipe) new BasicBowRecipe(k.getResourcePath(), v.getIngredients(), new ItemStack(ExtraBowsObjects.DIAMOND_BOW), BowSettings.DIAMOND).setRegistryName(k);
                e.getRegistry().register(basicBowRecipe);
                e.getRegistry().register(new BasicBowShapelessRecipe(basicBowRecipe, ExtraBowsObjects.DIAMOND_UPGRADE_KIT));
            }
            if (k.getResourcePath().contains("emerald_bow")) {
                BasicBowRecipe basicBowRecipe = (BasicBowRecipe) new BasicBowRecipe(k.getResourcePath(), v.getIngredients(), new ItemStack(ExtraBowsObjects.EMERALD_BOW), BowSettings.EMERALD).setRegistryName(k);
                e.getRegistry().register(basicBowRecipe);
                e.getRegistry().register(new BasicBowShapelessRecipe(basicBowRecipe, ExtraBowsObjects.EMERALD_UPGRADE_KIT));
            }
        });
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
    public static void projectileHit(ProjectileImpactEvent.Arrow e) {
        if (e.getEntity() instanceof EntityArrow) {
            EntityArrow arrow = (EntityArrow) e.getEntity();

            if (arrow.shootingEntity instanceof EntityPlayer && UpgradeUtil.isExtraBowsArrow(arrow)) {
                EntityPlayer player = (EntityPlayer) arrow.shootingEntity;
                if (e.getRayTraceResult().typeOfHit == RayTraceResult.Type.ENTITY) {
                    if (arrow.shootingEntity.equals(e.getRayTraceResult().entityHit)) {
                        if (arrow.getTags().contains("flyingUpgrade")) {
                            e.setCanceled(true);
                            return;
                        }
                    }
                }

                if (!arrow.getEntityData().getBoolean("alreadyHit")) {
                    arrow.getEntityData().setBoolean("alreadyHit", true);
                    UpgradeList list = ArrowUtil.ARROWS_TO_UPGRADES.get(arrow.getUniqueID());
                    if (list != null) {
                        if (e.getRayTraceResult().typeOfHit == RayTraceResult.Type.BLOCK) {
                            list.handleModifierHittingEvent(ArrowModifierUpgrade.EventType.BLOCK_HIT, e.getRayTraceResult().getBlockPos(), null, e.getEntity().world, player, arrow);
                            list.getBasicBow().onArrowHit(arrow);
                        } else if (e.getRayTraceResult().typeOfHit == RayTraceResult.Type.ENTITY) {
                            list.handleModifierHittingEvent(ArrowModifierUpgrade.EventType.ENTITY_HIT, null, e.getRayTraceResult().entityHit, e.getEntity().world, player, arrow);
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

    @SubscribeEvent
    public static void arrowConstructing(EntityJoinWorldEvent e) {
        if (!e.getWorld().isRemote) {
            if (e.getEntity() instanceof EntityArrow && UpgradeUtil.isExtraBowsArrow(e.getEntity())) {

                EntityArrow arrow = (EntityArrow) e.getEntity();
                if (arrow.shootingEntity instanceof EntityPlayerMP) {
                    ItemStack bow = ((EntityPlayer) arrow.shootingEntity).getHeldItemMainhand();

                    if(!(bow.getItem() instanceof BasicBow)) {
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

    @SubscribeEvent
    public static void startTracking(net.minecraftforge.event.entity.player.PlayerEvent.StartTracking e) {
        if (e.getTarget() instanceof EntityArrow) {
            if (UpgradeUtil.isExtraBowsArrow(e.getTarget()) && e.getTarget().getTags().contains("flyingUpgrade")) {
                ExtraBowsPacketHandler.INSTANCE.sendTo(new PacketUpdateArrow(((EntityArrow) e.getTarget())), (EntityPlayerMP) e.getEntityPlayer());
            }
        }
    }
}
