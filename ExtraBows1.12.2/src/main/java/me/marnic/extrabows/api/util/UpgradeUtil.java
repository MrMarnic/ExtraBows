package me.marnic.extrabows.api.util;

import me.marnic.extrabows.api.upgrade.ArrowModifierUpgrade;
import me.marnic.extrabows.api.upgrade.ArrowMultiplierUpgrade;
import me.marnic.extrabows.api.upgrade.BasicUpgrade;
import me.marnic.extrabows.api.upgrade.UpgradeList;
import me.marnic.extrabows.common.items.upgrades.BasicUpgradeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright (c) 28.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class UpgradeUtil {

    public static UpgradeList getUpgradesFromStackNEW(ItemStack stack) {

        ArrowMultiplierUpgrade multiplierUpgrade = null;

        ArrayList<ArrowModifierUpgrade> mods = new ArrayList<>();

        ItemStackHandler handler = getHandlerForItemStackNEW(stack);

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

        return new UpgradeList(multiplierUpgrade, mods, handler);
    }

    public static UpgradeList getUpgradesFromHandler(ItemStackHandler handler) {
        ArrowMultiplierUpgrade multiplierUpgrade = null;
        ArrayList<ArrowModifierUpgrade> modifierUpgrades = new ArrayList<>();
        ArrayList<Integer> indecs = new ArrayList<>();
        if (handler.getSlots() == 4) {
            if (!handler.getStackInSlot(0).isEmpty()) {
                multiplierUpgrade = (ArrowMultiplierUpgrade) getUpgradeFromStack(handler.getStackInSlot(0));
            }

            for (int i = 1; i < 4; i++) {
                if (!handler.getStackInSlot(i).isEmpty()) {
                    modifierUpgrades.add((ArrowModifierUpgrade) getUpgradeFromStack(handler.getStackInSlot(i)));
                    indecs.add(i);
                }
            }
        }

        return new UpgradeList(multiplierUpgrade, modifierUpgrades, handler);
    }

    public static BasicUpgrade getUpgradeFromStack(ItemStack stack) {
        if (stack.getItem() instanceof BasicUpgradeItem) {
            return ((BasicUpgradeItem) stack.getItem()).getUpgrade();
        }
        return null;
    }

    public static ItemStackHandler getHandlerForItemStackNEW(ItemStack stack) {
        return (ItemStackHandler) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    }

    public static void saveUpgradesToStackNEW(ItemStack stack, ItemStackHandler handler2) {
        ItemStackHandler handler = (ItemStackHandler) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        for (int i = 0; i < handler2.getSlots(); i++) {
            handler.setStackInSlot(i, handler2.getStackInSlot(i));
        }
    }

    public static Iterable<BlockPos> getBlocksInRadius(BlockPos center, int radius) {
        BlockPos min = new BlockPos(center.getX() - radius, center.getY() - radius, center.getZ() - radius);
        BlockPos max = new BlockPos(center.getX() + radius, center.getY() + radius, center.getZ() + radius);

        return BlockPos.getAllInBox(min, max);
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

    public static List<String> createDescriptionFromStingList(String... list) {
        return Arrays.asList(list);
    }

    public static String getTranslatedDescriptionForUpgrade(BasicUpgrade upgrade) {
        return new TextComponentTranslation(upgrade.getNonFormattedName() + ".description.text").getFormattedText();
    }

    public static String getTranslatedDescriptionForUpgrade(BasicUpgrade upgrade, int numb) {
        return new TextComponentTranslation(upgrade.getNonFormattedName() + ".description.text" + numb).getFormattedText();
    }
}
