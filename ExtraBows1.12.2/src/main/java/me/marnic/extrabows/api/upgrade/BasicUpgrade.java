package me.marnic.extrabows.api.upgrade;

import me.marnic.extrabows.common.items.BasicBow;
import me.marnic.extrabows.common.items.upgrades.BasicUpgradeItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;

/**
 * Copyright (c) 26.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BasicUpgrade {
    private String name;
    private BasicUpgradeItem item;
    private int durability;
    private TextComponentTranslation translation;

    public BasicUpgrade(String name, int durability) {
        this.name = name;
        UPGRADES.put(name, this);
        this.item = new BasicUpgradeItem(name, this);
        this.item.setMaxDamage(durability);
        this.durability = durability;
        this.translation = new TextComponentTranslation("item." + name + ".name");
    }

    public BasicUpgrade(String name) {
        this.name = name;
        UPGRADES.put(name, this);
        this.item = new BasicUpgradeItem(name, this);
        this.item.setMaxDamage(400);
        this.durability = 400;
        this.translation = new TextComponentTranslation("item." + name + ".name");
    }

    public String getName() {
        return translation.getFormattedText();
    }

    public String getNonFormattedName() {
        return name;
    }

    public void handleAction(BasicBow basicBow, World worldIn, ItemStack itemstack, EntityPlayer entityplayer, float f, ItemStack stack, boolean flag1, UpgradeList list) {
    }

    public BasicUpgradeItem getItem() {
        return item;
    }

    public static final HashMap<String, BasicUpgrade> UPGRADES = new HashMap<>();

    public static BasicUpgrade getById(String id) {
        return UPGRADES.get(id);
    }

    public int getDurability() {
        return durability;
    }

    public List<String> getDescription() {
        return null;
    }

    public boolean hasDescription() {
        return getDescription() != null;
    }
}
