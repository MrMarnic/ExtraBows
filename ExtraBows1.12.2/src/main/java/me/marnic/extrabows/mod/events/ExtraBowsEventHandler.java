package me.marnic.extrabows.mod.events;

import me.marnic.extrabows.api.block.BasicBlock;
import me.marnic.extrabows.api.item.BasicItem;
import me.marnic.extrabows.api.item.IModelRegistry;
import me.marnic.extrabows.api.upgrade.UpgradeList;
import me.marnic.extrabows.api.upgrade.Upgrades;
import me.marnic.extrabows.api.util.TimeCommand;
import me.marnic.extrabows.api.util.TimerUtil;
import me.marnic.extrabows.api.util.UpgradeUtil;
import me.marnic.extrabows.mod.input.ExtraBowsInputHandler;
import me.marnic.extrabows.mod.items.BasicBow;
import me.marnic.extrabows.mod.items.CustomBowSettings;
import me.marnic.extrabows.mod.main.ExtraBowsObjects;
import me.marnic.extrabows.mod.main.Identification;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * Copyright (c) 24.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
@Mod.EventBusSubscriber
public class ExtraBowsEventHandler {

    public static final ArrayList<Item> ITEMS_TO_REGISTER = new ArrayList<>();
    public static final ArrayList<IModelRegistry> MODELS_TO_REGISTER = new ArrayList<>();
    public static final ArrayList<Block> BLOCKS_TO_REGISTER = new ArrayList<>();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> e) {
        for(Item item:ITEMS_TO_REGISTER) {
            e.getRegistry().register(item);
        }

        e.getRegistry().register(new BasicBow(new CustomBowSettings("minecraft:bow")).setCreativeTab(CreativeTabs.COMBAT).setUnlocalizedName("bow"));
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> e) {
        for(Block block:BLOCKS_TO_REGISTER) {
            e.getRegistry().register(block);
        }
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent e) {
        for(IModelRegistry registry:MODELS_TO_REGISTER) {
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
        if(e.getState().getBlock().equals(ExtraBowsObjects.BRIDGE_BLOCK)) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void itemCrafted(PlayerEvent.ItemCraftedEvent e) {
        if(e.crafting.getItem().equals(Upgrades.LIGHTNING_UPGRADE.getItem())) {
            for(int i = 1;i<14;i++) {
                TimerUtil.addTimeCommand(new TimeCommand(20*i, new Runnable() {
                    @Override
                    public void run() {
                        e.player.world.addWeatherEffect(new EntityLightningBolt(e.player.world,e.player.posX,e.player.posY,e.player.posZ,false));
                    }
                }));
            }
        }
    }
}
