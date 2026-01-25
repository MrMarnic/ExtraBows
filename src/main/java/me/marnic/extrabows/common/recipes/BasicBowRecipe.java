package me.marnic.extrabows.common.recipes;

import me.marnic.extrabows.api.util.UpgradeUtil;
import me.marnic.extrabows.common.items.BasicBow;
import me.marnic.extrabows.common.items.CustomBowSettings;
import me.marnic.extrabows.common.main.Identification;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

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
        super(new ResourceLocation(Identification.MODID, "recipe_" + type), type, CraftingBookCategory.EQUIPMENT, 3, 3, ingredients, output);

        this.settings = settings;

        for (int i = 0; i < ingredients.size(); i++) {
            if (ingredients.get(i).getItems().length > 0 && ingredients.get(i).getItems()[0].getItem() instanceof BasicBow) {
                this.bowIndex = i;
            }
        }
    }

    @Override
    public boolean matches(CraftingContainer inv, Level worldIn) {
        boolean b = super.matches(inv, worldIn);
        if (b && bowIndex > -1) {
            base = inv.getItem(bowIndex);
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

    public int getBowIndex() {
        return bowIndex;
    }

    public CustomBowSettings getSettingsType() {
        return settings;
    }
}