package me.marnic.extrabows.common.main;

import me.marnic.extrabows.common.blocks.BlockBridgeBlock;
import me.marnic.extrabows.common.blocks.tileentities.TileEntityBridgeBlock;
import me.marnic.extrabows.common.container.BowUpgradeGuiContainer;
import me.marnic.extrabows.common.items.BowSettings;
import me.marnic.extrabows.common.items.bows.*;
import me.marnic.extrabows.common.items.other.*;
import me.marnic.extrabows.common.items.other.bowUpgradeKits.BasicBowUpgradeKitItem;
import me.marnic.extrabows.common.items.upgrades.ItemUpgradePlate;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

/**
 * Copyright (c) 24.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ExtraBowsObjects {

    public static final DeferredRegister<Item> ITEM_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS,Identification.MODID);
    public static final DeferredRegister<Block> BLOCK_REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS,Identification.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES,Identification.MODID);
    public static final DeferredRegister<MenuType<?>> CONTAINER_TYPE_REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES,Identification.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB_REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB,Identification.MODID);

    public static final RegistryObject<CreativeModeTab> CREATIVE_MODE_TAB = CREATIVE_MODE_TAB_REGISTRY.register("extrabows_items",() -> CreativeModeTab.builder().icon(() -> new ItemStack(ExtraBowsObjects.DIAMOND_BOW.get()))
             .displayItems((g,output) -> {
                 ExtraBowsObjects.ITEM_REGISTRY.getEntries().forEach((item) -> {
                     output.accept(item.get());
                 });
             }).title(Component.translatable("itemGroup.extrabows_items")).build());

    public static RegistryObject<ItemStoneBow> STONE_BOW = ITEM_REGISTRY.register("stone_bow",ItemStoneBow::new);
    public static RegistryObject<ItemIronBow> IRON_BOW = ITEM_REGISTRY.register("iron_bow",ItemIronBow::new);
    public static RegistryObject<ItemGoldBow> GOLD_BOW = ITEM_REGISTRY.register("golden_bow",ItemGoldBow::new);
    public static RegistryObject<ItemDiamondBow> DIAMOND_BOW = ITEM_REGISTRY.register("diamond_bow",ItemDiamondBow::new);
    public static RegistryObject<ItemEmeraldBow> EMERALD_BOW = ITEM_REGISTRY.register("emerald_bow",ItemEmeraldBow::new);
    public static RegistryObject<ItemElectricBow> ENERGY_BOW = ITEM_REGISTRY.register("electric_bow",ItemElectricBow::new);

    public static RegistryObject<ItemUpgradePlate> UPGRADE_PLATE = ITEM_REGISTRY.register("upgrade_plate", ItemUpgradePlate::new);

    public static RegistryObject<BlockBridgeBlock> BRIDGE_BLOCK = registerBlock("bridge_block",BlockBridgeBlock::new);

    public static RegistryObject<ItemStrongDiamond> STRONG_DIAMOND = ITEM_REGISTRY.register("strong_diamond",ItemStrongDiamond::new);
    public static RegistryObject<ItemStrongEmerald> STRONG_EMERALD = ITEM_REGISTRY.register("strong_emerald",ItemStrongEmerald::new);
    public static RegistryObject<BlockStrongStone> STRONG_STONE = registerBlock("strong_stone",BlockStrongStone::new);
    public static RegistryObject<ItemStrongGold> STRONG_GOLD = ITEM_REGISTRY.register("strong_gold",ItemStrongGold::new);
    public static RegistryObject<ItemStrongIron> STRONG_IRON = ITEM_REGISTRY.register("strong_iron",ItemStrongIron::new);

    public static RegistryObject<BasicBowUpgradeKitItem> STONE_UPGRADE_KIT = ITEM_REGISTRY.register("stone_upgrade_kit",() -> new BasicBowUpgradeKitItem("stone_upgrade_kit", BowSettings.STONE));
    public static RegistryObject<BasicBowUpgradeKitItem> IRON_UPGRADE_KIT = ITEM_REGISTRY.register("iron_upgrade_kit",() -> new BasicBowUpgradeKitItem("iron_upgrade_kit", BowSettings.IRON));
    public static RegistryObject<BasicBowUpgradeKitItem> GOLD_UPGRADE_KIT = ITEM_REGISTRY.register("golden_upgrade_kit",() -> new BasicBowUpgradeKitItem("golden_upgrade_kit", BowSettings.GOLD));
    public static RegistryObject<BasicBowUpgradeKitItem> DIAMOND_UPGRADE_KIT = ITEM_REGISTRY.register("diamond_upgrade_kit",() -> new BasicBowUpgradeKitItem("diamond_upgrade_kit", BowSettings.DIAMOND));
    public static RegistryObject<BasicBowUpgradeKitItem> EMERALD_UPGRADE_KIT = ITEM_REGISTRY.register("emerald_upgrade_kit",() -> new BasicBowUpgradeKitItem("emerald_upgrade_kit", BowSettings.EMERALD));


    public static RegistryObject<BlockEntityType<TileEntityBridgeBlock>> BRIDGE_BLOCK_TILE_ENTITY = BLOCK_ENTITY_REGISTRY.register("tile_bridge_block", () -> BlockEntityType.Builder.of(TileEntityBridgeBlock::new, BRIDGE_BLOCK.get()).build(null));

    public static RegistryObject<MenuType<BowUpgradeGuiContainer>> UPGRADE_INVENTORY_CONTAINER_TYPE = CONTAINER_TYPE_REGISTRY.register("upgrade_inventory", () -> new MenuType<>(BowUpgradeGuiContainer::new, FeatureFlagSet.of()));

    public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> r = BLOCK_REGISTRY.register(name,block);
        ITEM_REGISTRY.register(name,() -> {
            return new BlockItem(r.get(),new Item.Properties());
        });

        return r;
    }

    public static void register(IEventBus bus) {
        ITEM_REGISTRY.register(bus);
        BLOCK_REGISTRY.register(bus);
        BLOCK_ENTITY_REGISTRY.register(bus);
        CONTAINER_TYPE_REGISTRY.register(bus);
        CREATIVE_MODE_TAB_REGISTRY.register(bus);
    }
}
