package me.marnic.extrabows.common.recipes;

import me.marnic.extrabows.api.util.UpgradeUtil;
import me.marnic.extrabows.common.items.BasicBow;
import me.marnic.extrabows.common.main.Identification;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

/**
 * Copyright (c) 23.09.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BasicBowRecipe extends ShapedRecipe {
    public BasicBowRecipe(String type,NonNullList<Ingredient> ingredients,ItemStack output) {
        super(new ResourceLocation(Identification.MODID,type),"recipe_"+type,3,3,ingredients,output);
    }

    ItemStack base;

    @Override
    public boolean matches(CraftingInventory p_77569_1_, World p_77569_2_) {
        boolean b = super.matches(p_77569_1_, p_77569_2_);
        if(b) {
            base = p_77569_1_.getStackInSlot(4);
        }

        return b;
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory p_77572_1_) {
        ItemStack out = super.getCraftingResult(p_77572_1_);

        if(base!=null) {
            UpgradeUtil.copyUpgradesToStack(base,out);
        }

        return out;
    }
}
