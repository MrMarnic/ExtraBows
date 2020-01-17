package me.marnic.extrabows.api.util;

import me.marnic.extrabows.api.upgrade.ArrowModifierUpgrade;
import me.marnic.extrabows.api.upgrade.ArrowMultiplierUpgrade;
import me.marnic.extrabows.api.upgrade.BasicUpgrade;
import me.marnic.extrabows.api.upgrade.UpgradeList;
import me.marnic.extrabows.common.items.BasicBow;
import me.marnic.extrabows.common.items.upgrades.BasicUpgradeItem;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.Arrays;
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
        return (ItemStackHandler) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
    }

    public static void saveUpgradesToStack(ItemStack stack, ItemStackHandler handler2) {
        ItemStackHandler handler = (ItemStackHandler) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);


        for (int i = 0; i < handler2.getSlots(); i++) {
            handler.setStackInSlot(i, handler2.getStackInSlot(i));
        }
    }

    public static void copyUpgradesToStack(ItemStack old, ItemStack newOne) {
        ItemStackHandler handlerOld = getHandlerForItemStack(old);

        UpgradeUtil.saveUpgradesToStack(newOne, handlerOld);
    }

    public static Stream<BlockPos> getBlocksInRadius(BlockPos center, int radius) {
        BlockPos min = new BlockPos(center.getX() - radius, center.getY() - radius, center.getZ() - radius);
        BlockPos max = new BlockPos(center.getX() + radius, center.getY() + radius, center.getZ() + radius);

        return BlockPos.getAllInBox(min, max);
    }

    public static AxisAlignedBB getRadiusBoundingBox(BlockPos center, int radius) {
        BlockPos min = new BlockPos(center.getX() - radius, center.getY() - radius, center.getZ() - radius);
        BlockPos max = new BlockPos(center.getX() + radius, center.getY() + radius, center.getZ() + radius);

        return new AxisAlignedBB(min, max);
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

    public static List<ITextComponent> createDescriptionFromStingList(String... list) {
        ArrayList<ITextComponent> list1 = new ArrayList();

        for(String s : list){
            list1.add(new StringTextComponent(s));
        }

        return list1;
    }

    public static String getTranslatedDescriptionForUpgrade(BasicUpgrade upgrade) {
        return new TranslationTextComponent(upgrade.getNonFormattedName() + ".description.text").getFormattedText();
    }

    public static String getTranslatedDescriptionForUpgrade(BasicUpgrade upgrade, int numb) {
        return new TranslationTextComponent(upgrade.getNonFormattedName() + ".description.text" + numb).getFormattedText();
    }

    public static boolean isExtraBowsArrow(Entity e) {
        return e.getTags().contains("extrabows");
    }
}
