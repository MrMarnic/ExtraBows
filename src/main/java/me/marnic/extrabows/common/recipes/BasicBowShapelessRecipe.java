package me.marnic.extrabows.common.recipes;

import me.marnic.extrabows.api.util.UpgradeUtil;
import me.marnic.extrabows.common.items.BasicBow;
import me.marnic.extrabows.common.items.other.bowUpgradeKits.BasicBowUpgradeKitItem;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import me.marnic.extrabows.common.main.Identification;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Copyright (c) 23.09.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BasicBowShapelessRecipe extends ShapelessRecipe {

    ItemStack base;

    public BasicBowShapelessRecipe(BasicBowRecipe recipe, BasicBowUpgradeKitItem upgradeKit) {
        super(new ResourceLocation(Identification.MODID, recipe.getGroup()),recipe.getGroup(), recipe.getRecipeOutput(), NonNullList.create());

        getIngredients().add(Ingredient.fromItems(Items.BOW, ExtraBowsObjects.STONE_BOW, ExtraBowsObjects.IRON_BOW, ExtraBowsObjects.GOLD_BOW, ExtraBowsObjects.DIAMOND_BOW, ExtraBowsObjects.EMERALD_BOW));
        getIngredients().add(Ingredient.fromItems(upgradeKit));
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        boolean b = super.matches(inv, worldIn);
        if (b) {
            for (int i = 0; i < inv.getWidth() * inv.getHeight(); i++) {
                Item item = inv.getStackInSlot(i).getItem();
                if (item instanceof BasicBow) {
                    base = inv.getStackInSlot(i);
                }
            }
        }

        if (base == null) {
            return false;
        }

        return b;
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack out = super.getCraftingResult(inv);

        if (base != null) {
            UpgradeUtil.copyUpgradesToStack(base, out);
        }

        UpgradeUtil.getUpgradesFromStack(out).handleInsertedEvent(out);

        return out;
    }
}