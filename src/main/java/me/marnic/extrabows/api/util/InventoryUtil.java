package me.marnic.extrabows.api.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.TagType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;

public class InventoryUtil {
    public static void saveInvToItemStack(ItemStackHandler toSave, ItemStack stack) {
        stack.getOrCreateTag().put("inventory",toSave.serializeNBT());
    }

    public static ItemStackHandler getFromStack(ItemStack stack) {
        if(stack.hasTag()) {
            CompoundTag inv = stack.getOrCreateTag().getCompound("inventory");
            if(inv != null) {
                ItemStackHandler handler = new ItemStackHandler(4);
                handler.deserializeNBT(inv);
                return handler;
            }
        }

        return new ItemStackHandler(4);
    }
}
