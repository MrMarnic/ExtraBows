package me.marnic.extrabows.common.main;

import me.marnic.extrabows.api.upgrade.Upgrades;
import me.marnic.extrabows.api.util.IdentificationUtil;
import me.marnic.extrabows.common.blockentities.BlockEntityBridgeBlock;
import me.marnic.extrabows.common.blocks.BlockBridgeBlock;
import me.marnic.extrabows.common.container.BowUpgradeContainer;
import me.marnic.extrabows.common.items.BasicBow;
import me.marnic.extrabows.common.items.bows.*;
import me.marnic.extrabows.common.items.other.*;
import me.marnic.extrabows.common.items.upgrades.ItemUpgradePlate;
import me.marnic.extrabows.common.upgrades.BridgeUpgrade;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.container.ContainerFactory;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.BrewingStandBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.container.Container;
import net.minecraft.container.ContainerType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.core.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Copyright (c) 07.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ExtraBowsObjects {

    public static ItemGroup CREATIVE_TAB;

    public static BlockEntityType<BlockEntityBridgeBlock> BRIDGE_BLOCK_TYPE;
    public static BlockBridgeBlock BRIDGE_BLOCK;

    public static BlockStrongStone STRONG_STONE;
    public static ItemStrongIron STRONG_IRON;
    public static ItemStrongGold STRONG_GOLD;
    public static ItemStrongDiamond STRONG_DIAMOND;
    public static ItemStrongEmerald STRONG_EMERALD;

    public static ItemUpgradePlate UPGRADE_PLATE;

    public static ContainerType<BowUpgradeContainer> BOW_UPGRADE_CONTAINER_TYPE;

    public static ItemStoneBow STONE_BOW;
    public static ItemIronBow IRON_BOW;
    public static ItemGoldBow GOLD_BOW;
    public static ItemDiamondBow DIAMOND_BOW;
    public static ItemEmeraldBow EMERALD_BOW;

    public static Identifier BOW_UPGRADE_CONTAINER_IDEN;

    public static void init() {
        CREATIVE_TAB = FabricItemGroupBuilder.create(IdentificationUtil.fromString("extrabows_items")).icon(()->new ItemStack(DIAMOND_BOW)).build();

        AllBowSettings.init();

        BRIDGE_BLOCK = new BlockBridgeBlock();

        BRIDGE_BLOCK_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, IdentificationUtil.fromString("bridge_block_entity"),BlockEntityType.Builder.create(BlockEntityBridgeBlock::new,BRIDGE_BLOCK).build(null));

        STRONG_STONE = new BlockStrongStone();
        STRONG_IRON = new ItemStrongIron();
        STRONG_GOLD = new ItemStrongGold();
        STRONG_DIAMOND = new ItemStrongDiamond();
        STRONG_EMERALD = new ItemStrongEmerald();

        UPGRADE_PLATE = new ItemUpgradePlate();

        STONE_BOW = new ItemStoneBow();
        IRON_BOW = new ItemIronBow();
        GOLD_BOW = new ItemGoldBow();
        DIAMOND_BOW = new ItemDiamondBow();
        EMERALD_BOW = new ItemEmeraldBow();

        BOW_UPGRADE_CONTAINER_IDEN = IdentificationUtil.fromString("bow_upgrade_container");

        ContainerProviderRegistry.INSTANCE.registerFactory(BOW_UPGRADE_CONTAINER_IDEN, (syncId, identifier, player, buf) -> new BowUpgradeContainer(syncId,player.inventory,buf));

        Upgrades.init();

        BridgeUpgrade.BUILDING_BLOCK = ExtraBowsObjects.BRIDGE_BLOCK;
    }

    public static class AllBowSettings {
        public static BowSettings STONE_BOW;
        public static BowSettings IRON_BOW;
        public static BowSettings GOLDEN_BOW;
        public static BowSettings DIAMOND_BOW;
        public static BowSettings EMERALD_BOW;

        public static void init() {
            STONE_BOW = new BowSettings("stone_bow",434,3f,1f, BowSettings.NORMAL_INACCURACY,18f).setType(Items.COBBLESTONE);
            IRON_BOW = new BowSettings("iron_bow",534,3.5f,2f, BowSettings.NORMAL_INACCURACY,16f).setType(Items.IRON_INGOT);
            GOLDEN_BOW = new BowSettings("golden_bow",300,4.25f,0, 0.5f,10f).setType(Items.GOLD_INGOT);
            DIAMOND_BOW = new BowSettings("diamond_bow",750,3.75f,5f, BowSettings.NORMAL_INACCURACY,15f).setType(Items.DIAMOND);
            EMERALD_BOW = new BowSettings("emerald_bow",1500,4f,8f, BowSettings.NORMAL_INACCURACY,15f).setType(Items.EMERALD);
        }
    }
}
