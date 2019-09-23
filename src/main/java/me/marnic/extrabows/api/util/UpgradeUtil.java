package me.marnic.extrabows.api.util;

import me.marnic.extrabows.api.upgrade.ArrowModifierUpgrade;
import me.marnic.extrabows.api.upgrade.ArrowMultiplierUpgrade;
import me.marnic.extrabows.api.upgrade.BasicUpgrade;
import me.marnic.extrabows.api.upgrade.UpgradeList;
import me.marnic.extrabows.common.items.upgrades.BasicUpgradeItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

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

        if(handler!=null) {
            if(!handler.getStackInSlot(0).isEmpty()) {
                multiplierUpgrade = (ArrowMultiplierUpgrade)getUpgradeFromStack(handler.getStackInSlot(0));
            }

            for(int i = 1;i<4;i++) {
                if(!handler.getStackInSlot(i).isEmpty()) {
                    mods.add((ArrowModifierUpgrade)getUpgradeFromStack(handler.getStackInSlot(i)));
                }
            }
        }

        return new UpgradeList(multiplierUpgrade,mods,handler);
    }

    public static BasicUpgrade getUpgradeFromStack(ItemStack stack) {
        if(stack.getItem() instanceof BasicUpgradeItem) {
            return ((BasicUpgradeItem)stack.getItem()).getUpgrade();
        }
        return null;
    }

    public static ItemStackHandler getHandlerForItemStackNEW(ItemStack stack) {
        AtomicReference<ItemStackHandler> handler = new AtomicReference<>();
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,null).ifPresent(h -> handler.set((ItemStackHandler) h));
        return handler.get();
    }

    public static void saveUpgradesToStackNEW(ItemStack stack,ItemStackHandler handler2) {
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,null).ifPresent(handler -> {
            for(int i = 0;i<handler2.getSlots();i++) {
                ((ItemStackHandler)handler).setStackInSlot(i,handler2.getStackInSlot(i));
            }
        });
    }

    public static void copyUpgradesToStack(ItemStack old,ItemStack newOne) {
        ItemStackHandler handlerOld = getHandlerForItemStackNEW(old);

        UpgradeUtil.saveUpgradesToStackNEW(newOne,handlerOld);
    }

    public static Stream<BlockPos> getBlocksInRadius(BlockPos center, int radius) {
        BlockPos min = new BlockPos(center.getX() - radius,center.getY() - radius,center.getZ() - radius);
        BlockPos max = new BlockPos(center.getX() + radius,center.getY() + radius,center.getZ() + radius);

        return BlockPos.getAllInBox(min,max);
    }

    public static AxisAlignedBB getRadiusBoundingBox(BlockPos center,int radius) {
        BlockPos min = new BlockPos(center.getX() - radius,center.getY() - radius,center.getZ() - radius);
        BlockPos max = new BlockPos(center.getX() + radius,center.getY() + radius,center.getZ() + radius);

        return new AxisAlignedBB(min,max);
    }

    public static boolean isMultiplierUpgrade(ItemStack stack) {
        if(stack.getItem() instanceof BasicUpgradeItem) {
            return ((BasicUpgradeItem)stack.getItem()).getUpgrade() instanceof ArrowMultiplierUpgrade;
        }
        return false;
    }

    public static boolean isModifierUpgrade(ItemStack stack) {
        if(stack.getItem() instanceof BasicUpgradeItem) {
            return ((BasicUpgradeItem)stack.getItem()).getUpgrade() instanceof ArrowModifierUpgrade;
        }
        return false;
    }

    public static List<ITextComponent> createDescriptionFromStingList(ITextComponent... list) {
        return Arrays.asList(list);
    }

    public static ITextComponent getTranslatedDescriptionForUpgrade(BasicUpgrade upgrade) {
        return new TranslationTextComponent(upgrade.getNonFormattedName()+".description.text");
    }

    public static ITextComponent getTranslatedDescriptionForUpgrade(BasicUpgrade upgrade,int numb) {
        return new TranslationTextComponent(upgrade.getNonFormattedName()+".description.text"+numb);
    }

    public static boolean isExtraBowsArrow(Entity e) {
        return e.getTags().contains("extrabows");
    }
}
