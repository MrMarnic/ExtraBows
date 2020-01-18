package me.marnic.extrabows.api.util;

import me.marnic.extrabows.api.upgrade.*;
import me.marnic.extrabows.common.items.upgrades.BasicUpgradeItem;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

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

        Inventory handler = InventoryUtil.getFromStack(stack,4);
        if(handler!=null) {
            if(!handler.getInvStack(0).isEmpty()) {
                multiplierUpgrade = (ArrowMultiplierUpgrade)getUpgradeFromStack(handler.getInvStack(0));
            }

            for(int i = 1;i<4;i++) {
                if(!handler.getInvStack(i).isEmpty()) {
                    mods.add((ArrowModifierUpgrade)getUpgradeFromStack(handler.getInvStack(i)));
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

    public static Iterable<BlockPos> getBlocksInRadius(BlockPos center, int radius) {
        BlockPos min = new BlockPos(center.getX() - radius,center.getY() - radius,center.getZ() - radius);
        BlockPos max = new BlockPos(center.getX() + radius,center.getY() + radius,center.getZ() + radius);

        return BlockPos.iterate(min,max);
    }

    public static Box getRadiusBoundingBox(BlockPos center, int radius) {
        BlockPos min = new BlockPos(center.getX() - radius,center.getY() - radius,center.getZ() - radius);
        BlockPos max = new BlockPos(center.getX() + radius,center.getY() + radius,center.getZ() + radius);

        return new Box(min,max);
    }

    public static boolean isUpgrade(ItemStack stack) {
        return stack.getItem() instanceof BasicUpgradeItem;
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

    public static List<Text> createDescriptionFromStingList(Text... list) {
        return Arrays.asList(list);
    }

    public static Text getTranslatedDescriptionForUpgrade(BasicUpgrade upgrade) {
        return new TranslatableText(upgrade.getNonFormattedName()+".description.text");
    }

    public static Text getTranslatedDescriptionForUpgrade(BasicUpgrade upgrade,int numb) {
        return new TranslatableText(upgrade.getNonFormattedName()+".description.text"+numb);
    }
}
