package me.marnic.extrabows.common.items;

import me.marnic.extrabows.api.item.BasicItem;
import me.marnic.extrabows.api.upgrade.BasicUpgrade;
import me.marnic.extrabows.api.upgrade.UpgradeList;
import me.marnic.extrabows.api.util.ArrowUtil;
import me.marnic.extrabows.api.util.UpgradeUtil;
import me.marnic.extrabows.client.input.ExtraBowsInputHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Copyright (c) 24.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BasicBow extends ItemBow implements BasicItem{

    private CustomBowSettings settings;

    public BasicBow(CustomBowSettings settings) {
        this.settings = settings;
        createItem(settings.getName());
        setMaxDamage(settings.getMaxUses());
        this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                if (entityIn == null)
                {
                    return 0.0F;
                }
                else
                {
                    return !(entityIn.getActiveItemStack().getItem() instanceof ItemBow) ? 0.0F : (float)(stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / settings.getTime();
                }
            }
        });
    }

    private ItemStack findAmmoNEW(EntityPlayer player)
    {
        if (this.isArrow(player.getHeldItem(EnumHand.OFF_HAND)))
        {
            return player.getHeldItem(EnumHand.OFF_HAND);
        }
        else if (this.isArrow(player.getHeldItem(EnumHand.MAIN_HAND)))
        {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        }
        else
        {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
            {
                ItemStack itemstack = player.inventory.getStackInSlot(i);

                if (this.isArrow(itemstack))
                {
                    return itemstack;
                }
            }

            return ItemStack.EMPTY;
        }
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
            boolean flag = entityplayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack itemstack = this.findAmmoNEW(entityplayer);

            int i = this.getMaxItemUseDuration(stack) - timeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, entityplayer, i, !itemstack.isEmpty() || flag);
            if (i < 0) return;

            UpgradeList list = UpgradeUtil.getUpgradesFromStackNEW(stack);
            if (!itemstack.isEmpty() || flag)
            {
                if (itemstack.isEmpty())
                {
                    itemstack = new ItemStack(Items.ARROW);
                }

                float f = ArrowUtil.getArrowVelocity(i,this);

                if ((double)f >= 0.1D)
                {
                    boolean flag1 = entityplayer.capabilities.isCreativeMode || (itemstack.getItem() instanceof ItemArrow && ((ItemArrow) itemstack.getItem()).isInfinite(itemstack, stack, entityplayer));

                    if (!worldIn.isRemote)
                    {
                        list.applyDamage(entityplayer);
                        if(list.hasMul()) {
                            list.getArrowMultiplier().handleAction(this,worldIn,itemstack,entityplayer,f,stack,flag1,list);
                        }else {
                            EntityArrow entityarrow = ArrowUtil.createArrowComplete(worldIn,itemstack,entityplayer,this,f,stack,flag1,0,0,list);

                            worldIn.spawnEntity(entityarrow);
                        }
                    }

                    worldIn.playSound((EntityPlayer)null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                    if (!flag1 && !entityplayer.capabilities.isCreativeMode)
                    {
                        if(list.hasMul()) {
                            list.getArrowMultiplier().shrinkStack(itemstack);
                        }else {
                            itemstack.shrink(1);

                            if (itemstack.isEmpty())
                            {
                                entityplayer.inventory.deleteStack(itemstack);
                            }
                        }

                        if(!worldIn.isRemote) {
                            if(stack.getItemDamage()==stack.getMaxDamage()) {
                                list.dropItems(entityplayer);
                                stack.damageItem(1,entityplayer);
                            }
                        }
                    }

                    entityplayer.addStat(StatList.getObjectUseStats(this));
                }
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        UpgradeList list = UpgradeUtil.getUpgradesFromStackNEW(stack);
        tooltip.add("Press B to open the Upgrade inventory");
        if(!list.hasMul() && !list.hasMods()) {
            tooltip.add(new TextComponentTranslation("message.no_upgrades.text").getUnformattedText());
        }else {
            tooltip.add("Upgrades:");
        }
        if(list.hasMul()) {
            tooltip.add("Arrow Multiplier:");
            tooltip.add("- " + list.getArrowMultiplier().getName());
        }
        if(list.hasMods()) {
            tooltip.add("Arrow Modifiers:");
            for(BasicUpgrade upgrade:list.getArrowModifiers()) {
                tooltip.add("- " + upgrade.getName());
            }
        }
    }

    @Override
    public Item getItem() {
        return this;
    }

    public CustomBowSettings getSettings() {
        return settings;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt){

        return new StorageProvider();
    }
}

class StorageProvider implements ICapabilitySerializable<NBTTagCompound>,ICapabilityProvider{

    public ItemStackHandler handler = new ItemStackHandler(4);

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        if(capability== CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability== CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) handler;
        }
        return null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return handler.serializeNBT();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        handler.deserializeNBT(nbt);
    }
}
