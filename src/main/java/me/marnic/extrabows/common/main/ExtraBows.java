package me.marnic.extrabows.common.main;

import com.google.common.collect.ImmutableMap;
import me.marnic.extrabows.api.item.ConfigLoad;
import me.marnic.extrabows.api.upgrade.Upgrades;
import me.marnic.extrabows.common.config.ExtraBowsConfig;
import me.marnic.extrabows.common.events.ExtraBowsEventHandler;
import me.marnic.extrabows.common.items.BowSettings;
import me.marnic.extrabows.common.packet.ExtraBowsPacketHandler;
import me.marnic.extrabows.common.recipes.BasicBowRecipe;
import me.marnic.extrabows.common.recipes.BasicBowShapelessRecipe;
import me.marnic.extrabows.common.registry.ExtraBowsRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.core.util.ReflectionUtil;

import java.lang.reflect.Field;
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

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ExtraBowsConfig.SPEC);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private static void registerRecipe(IRecipe recipe, HashMap<ResourceLocation, IRecipe> whereToRegister) {
        whereToRegister.put(recipe.getId(), recipe);
    }

    public void doClientStuff(FMLClientSetupEvent event)
    {
        for (ConfigLoad item : ExtraBowsRegistry.CONFIG_LOAD) {
            item.initConfigOptions();
        }
    }

    @SubscribeEvent
    public void serverStarting(FMLServerStartingEvent e) {
        HashMap<ResourceLocation, IRecipe> bowRecipes = new HashMap<>();

        e.getServer().getRecipeManager().getRecipes().stream().filter(r -> r.getId().getNamespace().equalsIgnoreCase(Identification.MODID) && r.getId().getPath().contains("bow")).forEach(r -> {
            bowRecipes.put(r.getId(), r);
        });

        try {
            Field field = getFieldForTypeInClass(Map.class, RecipeManager.class);

            Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipes;

            ReflectionUtil.makeAccessible(field);

            recipes = (Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>>) ReflectionUtil.getFieldValue(field, e.getServer().getRecipeManager());

            HashMap<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipeMut = new HashMap<>(recipes);
            HashMap<ResourceLocation, IRecipe> craftingRecipesMut = new HashMap<>(recipeMut.get(IRecipeType.CRAFTING));


            bowRecipes.forEach((k, v) -> {
                if (k.getPath().contains("stone_bow")) {
                    BasicBowRecipe basicBowRecipe = (BasicBowRecipe) new BasicBowRecipe(k.getPath(), v.getIngredients(), new ItemStack(ExtraBowsObjects.STONE_BOW), BowSettings.STONE);
                    registerRecipe(basicBowRecipe, craftingRecipesMut);
                    registerRecipe(new BasicBowShapelessRecipe(basicBowRecipe, ExtraBowsObjects.STONE_UPGRADE_KIT), craftingRecipesMut);
                }
                if (k.getPath().contains("golden_bow")) {
                    BasicBowRecipe basicBowRecipe = (BasicBowRecipe) new BasicBowRecipe(k.getPath(), v.getIngredients(), new ItemStack(ExtraBowsObjects.GOLD_BOW), BowSettings.GOLD);
                    registerRecipe(basicBowRecipe, craftingRecipesMut);
                    registerRecipe(new BasicBowShapelessRecipe(basicBowRecipe, ExtraBowsObjects.GOLD_UPGRADE_KIT), craftingRecipesMut);
                }
                if (k.getPath().contains("iron_bow")) {
                    BasicBowRecipe basicBowRecipe = (BasicBowRecipe) new BasicBowRecipe(k.getPath(), v.getIngredients(), new ItemStack(ExtraBowsObjects.IRON_BOW), BowSettings.IRON);
                    registerRecipe(basicBowRecipe, craftingRecipesMut);
                    registerRecipe(new BasicBowShapelessRecipe(basicBowRecipe, ExtraBowsObjects.IRON_UPGRADE_KIT), craftingRecipesMut);
                }
                if (k.getPath().contains("diamond_bow")) {
                    BasicBowRecipe basicBowRecipe = (BasicBowRecipe) new BasicBowRecipe(k.getPath(), v.getIngredients(), new ItemStack(ExtraBowsObjects.DIAMOND_BOW), BowSettings.DIAMOND);
                    registerRecipe(basicBowRecipe, craftingRecipesMut);
                    registerRecipe(new BasicBowShapelessRecipe(basicBowRecipe, ExtraBowsObjects.DIAMOND_UPGRADE_KIT), craftingRecipesMut);
                }
                if (k.getPath().contains("emerald_bow")) {
                    BasicBowRecipe basicBowRecipe = (BasicBowRecipe) new BasicBowRecipe(k.getPath(), v.getIngredients(), new ItemStack(ExtraBowsObjects.EMERALD_BOW), BowSettings.EMERALD);
                    registerRecipe(basicBowRecipe, craftingRecipesMut);
                    registerRecipe(new BasicBowShapelessRecipe(basicBowRecipe, ExtraBowsObjects.EMERALD_UPGRADE_KIT), craftingRecipesMut);
                }
            });


            recipeMut.put(IRecipeType.CRAFTING, (Map) craftingRecipesMut);

            ReflectionUtil.setFieldValue(field, e.getServer().getRecipeManager(), new ImmutableMap.Builder().putAll(recipeMut).build());

        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    private Field getFieldForTypeInClass(Class type, Class loc) {
        for (Field f : FieldUtils.getAllFields(loc)) {
            if (f.getType().equals(type)) {
                return f;
            }
        }
        return null;
    }
}
