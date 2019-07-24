package me.marnic.extrabows.common.main;

import me.marnic.extrabows.common.blocks.BlockBridgeBlock;
import me.marnic.extrabows.common.blocks.tileentities.TileEntityBridgeBlock;
import me.marnic.extrabows.common.container.BowUpgradeGuiContainer;
import me.marnic.extrabows.common.items.BasicBow;
import me.marnic.extrabows.common.items.bows.*;
import me.marnic.extrabows.common.items.other.*;
import me.marnic.extrabows.common.items.upgrades.ItemUpgradePlate;
import me.marnic.extrabows.common.registry.ExtraBowsRegistry;
import me.marnic.extrabows.common.upgrades.BridgeUpgrade;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.function.Supplier;

/**
 * Copyright (c) 24.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ExtraBowsObjects {

    public static ItemStoneBow STONE_BOW;
    public static ItemIronBow IRON_BOW;
    public static ItemGoldBow GOLD_BOW;
    public static ItemDiamondBow DIAMOND_BOW;
    public static ItemEmeraldBow EMERALD_BOW;

    public static ItemUpgradePlate UPGRADE_PLATE;

    public static ItemGroup CREATIVE_TAB;

    public static BlockBridgeBlock BRIDGE_BLOCK;

    public static ItemStrongDiamond STRONG_DIAMOND;
    public static ItemStrongEmerald STRONG_EMERALD;
    public static BlockStrongStone STRONG_STONE;
    public static ItemStrongGold STRONG_GOLD;
    public static ItemStrongIron STRONG_IRON;

    public static TileEntityType BRIDGE_BLOCK_TILE_ENTITY;

    public static ContainerType UPGRADE_INVENTORY_CONTAINER_TYPE;

    public static void initTab() {
        CREATIVE_TAB = new ItemGroup("extrabows_items") {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(DIAMOND_BOW);
            }
        };
    }

    public static void init() {

        STONE_BOW = new ItemStoneBow();
        IRON_BOW = new ItemIronBow();
        GOLD_BOW = new ItemGoldBow();
        DIAMOND_BOW = new ItemDiamondBow();
        EMERALD_BOW = new ItemEmeraldBow();

        UPGRADE_PLATE = new ItemUpgradePlate();

        BRIDGE_BLOCK = new BlockBridgeBlock();

        BRIDGE_BLOCK_TILE_ENTITY = TileEntityType.Builder.create(TileEntityBridgeBlock::new,BRIDGE_BLOCK).build(null).setRegistryName("tile_bridge_block");

        BridgeUpgrade.BUILDING_BLOCK = BRIDGE_BLOCK;

        STRONG_DIAMOND = new ItemStrongDiamond();
        STRONG_EMERALD = new ItemStrongEmerald();
        STRONG_GOLD = new ItemStrongGold();
        STRONG_IRON = new ItemStrongIron();
        STRONG_STONE = new BlockStrongStone();

        UPGRADE_INVENTORY_CONTAINER_TYPE = IForgeContainerType.create(BowUpgradeGuiContainer::new).setRegistryName("upgrade_inventory");

        ExtraBowsRegistry.register(BRIDGE_BLOCK_TILE_ENTITY);
        ExtraBowsRegistry.register(UPGRADE_INVENTORY_CONTAINER_TYPE);
    }
}
