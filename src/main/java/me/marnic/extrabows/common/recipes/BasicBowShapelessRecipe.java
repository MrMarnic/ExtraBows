package me.marnic.extrabows.common.recipes;

import me.marnic.extrabows.api.util.UpgradeUtil;
import me.marnic.extrabows.common.items.BasicBow;
import me.marnic.extrabows.common.items.other.bowUpgradeKits.BasicBowUpgradeKitItem;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import me.marnic.extrabows.common.main.Identification;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;

/**
 * Copyright (c) 23.09.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BasicBowShapelessRecipe extends ShapelessRecipe {

    ItemStack base;

    public BasicBowShapelessRecipe(BasicBowRecipe recipe, BasicBowUpgradeKitItem upgradeKit, ItemStack output) {
        super(new ResourceLocation(Identification.MODID, recipe.getGroup()), recipe.getGroup(), CraftingBookCategory.EQUIPMENT, output, NonNullList.create());
        getIngredients().add(Ingredient.of(Items.BOW, ExtraBowsObjects.STONE_BOW.get(), ExtraBowsObjects.IRON_BOW.get(), ExtraBowsObjects.GOLD_BOW.get(), ExtraBowsObjects.DIAMOND_BOW.get(), ExtraBowsObjects.EMERALD_BOW.get()));
        getIngredients().add(Ingredient.of(upgradeKit));
    }

    @Override
    public boolean matches(CraftingContainer inv, Level worldIn) {
        boolean b = super.matches(inv, worldIn);
        if (b) {
            for (int i = 0; i < inv.getWidth() * inv.getHeight(); i++) {
                Item item = inv.getItem(i).getItem();
                if (item instanceof BasicBow) {
                    base = inv.getItem(i);
                }
            }
        }

        if (base == null) {
            return false;
        }

        return b;
    }



    @Override
    public ItemStack assemble(CraftingContainer inv, RegistryAccess access) {
        ItemStack out = super.assemble(inv, access);

        if (base != null) {
            UpgradeUtil.copyUpgradesToStack(base, out);
        }

        UpgradeUtil.getUpgradesFromStack(out).handleInsertedEvent(out);

        return out;
    }
}