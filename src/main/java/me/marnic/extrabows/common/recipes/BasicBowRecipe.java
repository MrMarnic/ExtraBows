package me.marnic.extrabows.common.recipes;

import me.marnic.extrabows.common.main.Identification;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

/**
 * Copyright (c) 23.09.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BasicBowRecipe extends ShapedRecipe {
    public BasicBowRecipe(String type, DefaultedList<Ingredient> ingredients, ItemStack output) {
        super(new Identifier(Identification.MODID,type),"recipe_"+type,3,3,ingredients,output);
    }

    ItemStack base;

    @Override
    public boolean matches(CraftingInventory var1, World var2) {
        boolean b = method_17728(var1, var2);
        if(b) {
            base = var1.getInvStack(4);
        }

        return b;
    }

    @Override
    public ItemStack craft(CraftingInventory p_77572_1_) {

        System.out.println(getOutput());

        ItemStack out = getOutput();

        if(base!=null) {
            out.setTag(base.getOrCreateTag());
        }

        return out;
    }
}