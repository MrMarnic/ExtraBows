package me.marnic.extrabows.common.main;

import me.marnic.extrabows.common.blocks.BlockBridgeBlock;
import me.marnic.extrabows.common.blocks.tileentities.TileEntityBridgeBlock;
import me.marnic.extrabows.common.items.BowSettings;
import me.marnic.extrabows.common.items.bows.*;
import me.marnic.extrabows.common.items.other.*;
import me.marnic.extrabows.common.items.other.bowUpgradeKits.BasicBowUpgradeKitItem;
import me.marnic.extrabows.common.items.upgrades.ItemUpgradePlate;
import me.marnic.extrabows.common.upgrades.BridgeUpgrade;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
    public static ItemEnergyBow ENERGY_BOW;

    public static ItemUpgradePlate UPGRADE_PLATE;

    public static CreativeTabs CREATIVE_TAB;

    public static BlockBridgeBlock BRIDGE_BLOCK;

    public static ItemStrongDiamond STRONG_DIAMOND;
    public static ItemStrongEmerald STRONG_EMERALD;
    public static BlockStrongStone STRONG_STONE;
    public static ItemStrongGold STRONG_GOLD;
    public static ItemStrongIron STRONG_IRON;

    public static BasicBowUpgradeKitItem STONE_UPGRADE_KIT;
    public static BasicBowUpgradeKitItem IRON_UPGRADE_KIT;
    public static BasicBowUpgradeKitItem GOLD_UPGRADE_KIT;
    public static BasicBowUpgradeKitItem DIAMOND_UPGRADE_KIT;
    public static BasicBowUpgradeKitItem EMERALD_UPGRADE_KIT;

    public static void initTab() {
        CREATIVE_TAB = new CreativeTabs("extrabows_items") {
            @Override
            public ItemStack getTabIconItem() {
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
        ENERGY_BOW = new ItemEnergyBow();

        UPGRADE_PLATE = new ItemUpgradePlate();

        BRIDGE_BLOCK = new BlockBridgeBlock();
        BRIDGE_BLOCK.setCreativeTab(CREATIVE_TAB);

        GameRegistry.registerTileEntity(TileEntityBridgeBlock.class, new ResourceLocation(Identification.MODID, "tile_entity_bridge_block"));

        BridgeUpgrade.BUILDING_BLOCK = BRIDGE_BLOCK;

        STRONG_DIAMOND = new ItemStrongDiamond();
        STRONG_EMERALD = new ItemStrongEmerald();
        STRONG_GOLD = new ItemStrongGold();
        STRONG_IRON = new ItemStrongIron();
        STRONG_STONE = new BlockStrongStone();

        STONE_UPGRADE_KIT = new BasicBowUpgradeKitItem("stone_upgrade_kit", BowSettings.STONE);
        IRON_UPGRADE_KIT = new BasicBowUpgradeKitItem("iron_upgrade_kit", BowSettings.IRON);
        GOLD_UPGRADE_KIT = new BasicBowUpgradeKitItem("golden_upgrade_kit", BowSettings.GOLD);
        DIAMOND_UPGRADE_KIT = new BasicBowUpgradeKitItem("diamond_upgrade_kit", BowSettings.DIAMOND);
        EMERALD_UPGRADE_KIT = new BasicBowUpgradeKitItem("emerald_upgrade_kit", BowSettings.EMERALD);
    }
}
