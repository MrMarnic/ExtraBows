package me.marnic.extrabows.api.util;

import me.marnic.extrabows.api.upgrade.ArrowModifierUpgrade;
import me.marnic.extrabows.api.upgrade.ArrowMultiplierUpgrade;
import me.marnic.extrabows.api.upgrade.BasicUpgrade;
import me.marnic.extrabows.api.upgrade.UpgradeList;
import me.marnic.extrabows.common.items.BasicBow;
import me.marnic.extrabows.common.items.upgrades.BasicUpgradeItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Copyright (c) 28.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class UpgradeUtil {

    public static UpgradeList getUpgradesFromStack(ItemStack stack) {

        ArrowMultiplierUpgrade multiplierUpgrade = null;

        ArrayList<ArrowModifierUpgrade> mods = new ArrayList<>();

        ItemStackHandler handler = getHandlerForItemStack(stack);

        if (handler != null) {
            if (!handler.getStackInSlot(0).isEmpty()) {
                multiplierUpgrade = (ArrowMultiplierUpgrade) getUpgradeFromStack(handler.getStackInSlot(0));
            }

            for (int i = 1; i < 4; i++) {
                if (!handler.getStackInSlot(i).isEmpty()) {
                    mods.add((ArrowModifierUpgrade) getUpgradeFromStack(handler.getStackInSlot(i)));
                }
            }
        }

        return new UpgradeList(multiplierUpgrade, mods, handler, ((BasicBow) stack.getItem()));
    }

    public static BasicUpgrade getUpgradeFromStack(ItemStack stack) {
        if (stack.getItem() instanceof BasicUpgradeItem) {
            return ((BasicUpgradeItem) stack.getItem()).getUpgrade();
        }
        return null;
    }

    public static ItemStackHandler getHandlerForItemStack(ItemStack stack) {
        //return (ItemStackHandler) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
        return InventoryUtil.getFromStack(stack);
    }

    public static void saveUpgradesToStack(ItemStack stack, ItemStackHandler handler2) {
        //ItemStackHandler handler = (ItemStackHandler) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
        ItemStackHandler handler = InventoryUtil.getFromStack(stack);

        for (int i = 0; i < handler2.getSlots(); i++) {
            handler.setStackInSlot(i, handler2.getStackInSlot(i));
        }
        InventoryUtil.saveInvToItemStack(handler,stack);
    }

    public static void copyUpgradesToStack(ItemStack old, ItemStack newOne) {
        ItemStackHandler handlerOld = getHandlerForItemStack(old);

        UpgradeUtil.saveUpgradesToStack(newOne, handlerOld);
    }

    public static Stream<BlockPos> getBlocksInRadius(BlockPos center, int radius) {
        BlockPos min = new BlockPos(center.getX() - radius, center.getY() - radius, center.getZ() - radius);
        BlockPos max = new BlockPos(center.getX() + radius, center.getY() + radius, center.getZ() + radius);

        return BlockPos.betweenClosedStream(min, max);
    }

    public static AABB getRadiusBoundingBox(BlockPos center, int radius) {
        BlockPos min = new BlockPos(center.getX() - radius, center.getY() - radius, center.getZ() - radius);
        BlockPos max = new BlockPos(center.getX() + radius, center.getY() + radius, center.getZ() + radius);

        return new AABB(min, max);
    }

    public static boolean isMultiplierUpgrade(ItemStack stack) {
        if (stack.getItem() instanceof BasicUpgradeItem) {
            return ((BasicUpgradeItem) stack.getItem()).getUpgrade() instanceof ArrowMultiplierUpgrade;
        }
        return false;
    }

    public static boolean isModifierUpgrade(ItemStack stack) {
        if (stack.getItem() instanceof BasicUpgradeItem) {
            return ((BasicUpgradeItem) stack.getItem()).getUpgrade() instanceof ArrowModifierUpgrade;
        }
        return false;
    }

    public static List<Component> createDescriptionFromStingList(String... list) {
        ArrayList<Component> list1 = new ArrayList();

        for (String s : list) {
            list1.add(Component.literal(s).withStyle(ChatFormatting.GRAY));
        }

        return list1;
    }

    public static String getTranslatedDescriptionForUpgrade(BasicUpgrade upgrade) {
        return Component.translatable(upgrade.getNonFormattedName() + ".description.text").getString();
    }

    public static String getTranslatedDescriptionForUpgrade(BasicUpgrade upgrade, int numb) {
        return Component.translatable(upgrade.getNonFormattedName() + ".description.text" + numb).getString();
    }

    public static boolean isExtraBowsArrow(Entity e) {
        return e.getTags().contains("extrabows");
    }
}
