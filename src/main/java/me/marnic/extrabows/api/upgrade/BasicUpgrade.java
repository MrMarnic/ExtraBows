package me.marnic.extrabows.api.upgrade;

import me.marnic.extrabows.api.item.ConfigLoad;
import me.marnic.extrabows.common.items.BasicBow;
import me.marnic.extrabows.common.items.upgrades.BasicUpgradeItem;
import me.marnic.extrabows.common.registry.ExtraBowsRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.List;

/**
 * Copyright (c) 26.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BasicUpgrade implements ConfigLoad {
    public static final HashMap<String, BasicUpgrade> UPGRADES = new HashMap<>();
    private String name;
    private RegistryObject<BasicUpgradeItem> item;
    private Component translation;

    public BasicUpgrade(String name, RegistryObject<BasicUpgradeItem> item) {
        this.name = name;
        this.item = item;
        UPGRADES.put(name, this);
        this.translation = Component.translatable("item.extrabows." + name);
        ExtraBowsRegistry.CONFIG_LOAD.add(this);
    }

    public static BasicUpgrade getById(String id) {
        return UPGRADES.get(id);
    }

    public String getName() {
        return translation.getString();
    }

    public String getNonFormattedName() {
        return name;
    }

    public void handleAction(BasicBow basicBow, Level worldIn, ItemStack itemstack, Player entityplayer, float f, ItemStack stack, boolean flag1, UpgradeList list, boolean isLoaded) {
    }

    public BasicUpgradeItem getItem() {
        return item.get();
    }

    public List<Component> getDescription() {
        return null;
    }

    public boolean hasDescription() {
        return getDescription() != null;
    }

    public boolean canNotBeInserted(IItemHandler itemHandler, ItemStack bow) {
        return false;
    }
}
