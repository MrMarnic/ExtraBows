package me.marnic.extrabows.common.recipes;

import me.marnic.extrabows.api.util.UpgradeUtil;
import me.marnic.extrabows.common.items.BasicBow;
import me.marnic.extrabows.common.items.CustomBowSettings;
import me.marnic.extrabows.common.main.Identification;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Copyright (c) 23.09.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BasicBowRecipe extends ShapedRecipe {

    ItemStack base;
    private int bowIndex = -1;
    private CustomBowSettings settings;

    public BasicBowRecipe(String type, NonNullList<Ingredient> ingredients, ItemStack output, CustomBowSettings settings) {
        super(new ResourceLocation(Identification.MODID, "recipe_" + type), type, 3, 3, ingredients, output);

        this.settings = settings;

        for (int i = 0; i < ingredients.size(); i++) {
            if (ingredients.get(i).getMatchingStacks().length > 0 && ingredients.get(i).getMatchingStacks()[0].getItem() instanceof BasicBow) {
                this.bowIndex = i;
            }
        }
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        boolean b = super.matches(inv, worldIn);
        if (b && bowIndex > -1) {
            base = inv.getStackInSlot(bowIndex);
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

    public int getBowIndex() {
        return bowIndex;
    }

    public CustomBowSettings getSettingsType() {
        return settings;
    }
}