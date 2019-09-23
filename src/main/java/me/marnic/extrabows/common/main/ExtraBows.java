package me.marnic.extrabows.common.main;

import com.google.common.collect.ImmutableMap;
import me.marnic.extrabows.api.upgrade.Upgrades;
import me.marnic.extrabows.common.config.ExtraBowsConfig;
import me.marnic.extrabows.common.events.ExtraBowsEventHandler;
import me.marnic.extrabows.common.packet.ExtraBowsPacketHandler;
import me.marnic.extrabows.common.recipes.BasicBowRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.core.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) 24.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
@Mod(Identification.MODID)
public class ExtraBows {


    public ExtraBows() {
        MinecraftForge.EVENT_BUS.register(new ExtraBowsEventHandler());

        ExtraBowsObjects.initTab();
        Upgrades.init();
        ExtraBowsObjects.init();
        ExtraBowsPacketHandler.init();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON,ExtraBowsConfig.SPEC);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void serverStarting(FMLServerStartingEvent e) {

        HashMap<String,IRecipe> bowRecipes = new HashMap<>();

        e.getServer().getRecipeManager().getRecipes().stream().filter( r -> r.getId().getNamespace().equalsIgnoreCase(Identification.MODID) && r.getId().getPath().contains("bow")).forEach(r -> {
            bowRecipes.put(r.getId().getPath().substring(7),r);
        });

        try {
            Field field = getFieldForTypeInClass(Map.class, RecipeManager.class);

            Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipes;

            ReflectionUtil.makeAccessible(field);

            recipes = (Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>>)ReflectionUtil.getFieldValue(field,e.getServer().getRecipeManager());

            HashMap<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipeMut = new HashMap<>(recipes);
            HashMap<ResourceLocation, IRecipe> craftingRecipesMut = new HashMap<>(recipeMut.get(IRecipeType.CRAFTING));


            bowRecipes.forEach((k,v)-> {
                switch (k) {
                    case "stone_bow":
                        craftingRecipesMut.put(v.getId(),new BasicBowRecipe(k,v.getIngredients(),new ItemStack(ExtraBowsObjects.STONE_BOW)));
                        break;
                    case "golden_bow":
                        craftingRecipesMut.put(v.getId(),new BasicBowRecipe(k,v.getIngredients(),new ItemStack(ExtraBowsObjects.GOLD_BOW)));
                        break;
                    case "iron_bow":
                        craftingRecipesMut.put(v.getId(),new BasicBowRecipe(k,v.getIngredients(),new ItemStack(ExtraBowsObjects.IRON_BOW)));
                        break;
                    case "diamond_bow":
                        craftingRecipesMut.put(v.getId(),new BasicBowRecipe(k,v.getIngredients(),new ItemStack(ExtraBowsObjects.DIAMOND_BOW)));
                        break;
                    case "emerald_bow":
                        craftingRecipesMut.put(v.getId(),new BasicBowRecipe(k,v.getIngredients(),new ItemStack(ExtraBowsObjects.EMERALD_BOW)));
                        break;
                }
            });


            recipeMut.put(IRecipeType.CRAFTING,(Map)craftingRecipesMut);

            ReflectionUtil.setFieldValue(field,e.getServer().getRecipeManager(),new ImmutableMap.Builder().putAll(recipeMut).build());

        }catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    @SubscribeEvent
    public void serverStarted(FMLServerStartedEvent e) {

    }

    private Field getFieldForTypeInClass(Class type,Class loc) {
        for(Field f: FieldUtils.getAllFields(loc)) {
            if(f.getType().equals(type)) {
                return f;
            }
        }
        return null;
    }
}
