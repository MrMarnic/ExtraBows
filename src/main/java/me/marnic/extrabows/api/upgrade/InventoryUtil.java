package me.marnic.extrabows.api.upgrade;

import net.fabricmc.fabric.api.util.NbtType;
import net.fabricmc.loader.metadata.ModMetadataV1;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.World;

/**
 * Copyright (c) 08.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class InventoryUtil {
    public static void saveInvToItemStack(Inventory toSave, ItemStack stack, World world) {
        if(!world.isClient) {
            ItemStack[] list = new ItemStack[toSave.getInvSize()];

            for(int i = 0;i<list.length;i++) {
                list[i] = toSave.getInvStack(i);
            }

            CompoundTag inv = new CompoundTag();
            ListTag tags = new ListTag();


            for(ItemStack s:list) {
                CompoundTag tag = new CompoundTag();
                s.toTag(tag);
                tags.add(tag);
            }

            inv.put("data",tags);

            stack.getOrCreateTag().put("inventory",inv);
        }
    }

    public static Inventory getFromStack(ItemStack stack,int normalSize) {
        if(stack.hasTag()) {
            CompoundTag inv = stack.getSubTag("inventory");
            if(inv != null) {
                ListTag tags = inv.getList("data", NbtType.COMPOUND);

                ItemStack[] list = new ItemStack[tags.size()];

                for(int i = 0;i<tags.size();i++) {
                    ItemStack st = ItemStack.fromTag((CompoundTag) tags.get(i));
                    list[i] = st;
                }

                BasicInventory inventory = new BasicInventory(list);
                return inventory;
            }
        }

        return new BasicInventory(normalSize);
    }
}
