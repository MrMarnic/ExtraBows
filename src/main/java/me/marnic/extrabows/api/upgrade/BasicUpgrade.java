package me.marnic.extrabows.api.upgrade;

import me.marnic.extrabows.api.item.ConfigLoad;
import me.marnic.extrabows.common.items.BasicBow;
import me.marnic.extrabows.common.items.upgrades.BasicUpgradeItem;
import me.marnic.extrabows.common.registry.ExtraBowsRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

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
    private BasicUpgradeItem item;
    private TranslationTextComponent translation;

    public BasicUpgrade(String name) {
        this.name = name;
        UPGRADES.put(name, this);
        this.item = new BasicUpgradeItem(name, this);
        this.item.setMaxDamage(400);
        this.translation = new TranslationTextComponent("item.extrabows." + name);
        ExtraBowsRegistry.CONFIG_LOAD.add(this);
    }

    public static BasicUpgrade getById(String id) {
        return UPGRADES.get(id);
    }

    public String getName() {
        return translation.getFormattedText();
    }

    public String getNonFormattedName() {
        return name;
    }

    public void handleAction(BasicBow basicBow, World worldIn, ItemStack itemstack, PlayerEntity entityplayer, float f, ItemStack stack, boolean flag1, UpgradeList list, boolean isLoaded) {
    }

    public BasicUpgradeItem getItem() {
        return item;
    }

    public List<ITextComponent> getDescription() {
        return null;
    }

    public boolean hasDescription() {
        return getDescription() != null;
    }

    public boolean canNotBeInserted(IItemHandler itemHandler, ItemStack bow) {
        return false;
    }
}
