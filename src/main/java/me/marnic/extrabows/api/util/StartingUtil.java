package me.marnic.extrabows.api.util;

import com.google.common.collect.ImmutableMap;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import me.marnic.extrabows.common.main.Identification;
import me.marnic.extrabows.common.recipes.BasicBowRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.core.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) 24.09.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class StartingUtil {
    public static void handleServerStart(MinecraftServer server) {

        HashMap<String,Recipe> bowRecipes = new HashMap<>();

        server.getRecipeManager().values().stream().filter( r -> r.getId().getNamespace().equalsIgnoreCase(Identification.MODID) && r.getId().getPath().contains("bow")).forEach(r -> {
            bowRecipes.put(r.getId().getPath().substring(7),r);
        });

        for (Field f : FieldUtils.getAllFields(RecipeManager.class)) {
            if (f.getType().equals(Map.class)) {
                Map<RecipeType<?>, Map<Identifier, Recipe<?>>> recipes;

                ReflectionUtil.makeAccessible(f);

                recipes = (Map<RecipeType<?>, Map<Identifier, Recipe<?>>>) ReflectionUtil.getFieldValue(f, server.getRecipeManager());

                HashMap<RecipeType<?>, Map<Identifier, Recipe<?>>> recipeMut = new HashMap<>(recipes);
                HashMap<Identifier, Recipe> craftingRecipesMut = new HashMap<>(recipeMut.get(RecipeType.CRAFTING));



                bowRecipes.forEach((k,v)-> {
                    switch (k) {
                        case "stone_bow":
                            craftingRecipesMut.put(v.getId(),new BasicBowRecipe(k,v.getPreviewInputs(),new ItemStack(ExtraBowsObjects.STONE_BOW)));
                            break;
                        case "golden_bow":
                            craftingRecipesMut.put(v.getId(),new BasicBowRecipe(k,v.getPreviewInputs(),new ItemStack(ExtraBowsObjects.GOLD_BOW)));
                            break;
                        case "iron_bow":
                            craftingRecipesMut.put(v.getId(),new BasicBowRecipe(k,v.getPreviewInputs(),new ItemStack(ExtraBowsObjects.IRON_BOW)));
                            break;
                        case "diamond_bow":
                            craftingRecipesMut.put(v.getId(),new BasicBowRecipe(k,v.getPreviewInputs(),new ItemStack(ExtraBowsObjects.DIAMOND_BOW)));
                            break;
                        case "emerald_bow":
                            craftingRecipesMut.put(v.getId(),new BasicBowRecipe(k,v.getPreviewInputs(),new ItemStack(ExtraBowsObjects.EMERALD_BOW)));
                            break;
                    }
                });

                recipeMut.put(RecipeType.CRAFTING, (Map) craftingRecipesMut);

                ReflectionUtil.setFieldValue(f, server.getRecipeManager(), new ImmutableMap.Builder().putAll(recipeMut).build());
            }
        }
    }
}
