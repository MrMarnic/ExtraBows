package me.marnic.extrabows.common.recipes;

import me.marnic.extrabows.api.util.UpgradeUtil;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

/**
 * Copyright (c) 23.09.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BasicBowRecipe extends ShapedRecipes {
    public BasicBowRecipe(String type,NonNullList<Ingredient> ingredients,ItemStack output) {
        super("recipe_"+type,3,3,ingredients,output);
    }

    ItemStack base;

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        boolean b = super.matches(inv, worldIn);
        if(b) {
            base = inv.getStackInSlot(4);
        }

        return b;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack out = super.getCraftingResult(inv);

        if(base!=null) {
            UpgradeUtil.copyUpgradesToStack(base,out);
        }

        return out;
    }
}