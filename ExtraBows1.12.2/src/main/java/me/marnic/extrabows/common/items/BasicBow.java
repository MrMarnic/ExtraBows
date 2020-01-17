package me.marnic.extrabows.common.items;

import me.marnic.extrabows.api.energy.ExtraBowsEnergy;
import me.marnic.extrabows.api.item.BasicItem;
import me.marnic.extrabows.api.upgrade.BasicUpgrade;
import me.marnic.extrabows.api.upgrade.UpgradeList;
import me.marnic.extrabows.api.upgrade.Upgrades;
import me.marnic.extrabows.api.util.ArrowUtil;
import me.marnic.extrabows.api.util.UpgradeUtil;
import me.marnic.extrabows.common.items.bows.ItemElectricBow;
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
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
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
public class BasicBow extends ItemBow implements BasicItem {

    private CustomBowSettings settings;

    public BasicBow(CustomBowSettings settings) {
        this.settings = settings;
        createItem(settings.getName());
        setMaxDamage(settings.getMaxUses());
        this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                if (entityIn == null) {
                    return 0.0F;
                } else {
                    return !(entityIn.getActiveItemStack().getItem() instanceof ItemBow) ? 0.0F : (float) (stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / settings.getTime();
                }
            }
        });
    }

    private ItemStack findAmmoNEW(EntityPlayer player, boolean electric) {
        if (electric) {
            return new ItemStack(Items.ARROW);
        }
        if (this.isArrow(player.getHeldItem(EnumHand.OFF_HAND))) {
            return player.getHeldItem(EnumHand.OFF_HAND);
        } else if (this.isArrow(player.getHeldItem(EnumHand.MAIN_HAND))) {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                ItemStack itemstack = player.inventory.getStackInSlot(i);

                if (this.isArrow(itemstack)) {
                    return itemstack;
                }
            }

            return ItemStack.EMPTY;
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (isLoaded(UpgradeUtil.getUpgradesFromStack(stack), stack)) {
            playerIn.setActiveHand(handIn);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer) entityLiving;
            boolean flag = entityplayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;

            UpgradeList list = UpgradeUtil.getUpgradesFromStack(stack);

            boolean isLoaded = isLoaded(list, stack);

            ItemStack arrowStack = this.findAmmoNEW(entityplayer, isLoaded);

            int i = this.getMaxItemUseDuration(stack) - timeLeft;

            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, entityplayer, i, !arrowStack.isEmpty() || flag);

            if (i < 0) return;

            if (!arrowStack.isEmpty() || flag) {
                if (arrowStack.isEmpty()) {
                    arrowStack = new ItemStack(Items.ARROW);
                }

                float f = ArrowUtil.getArrowVelocity(i, this);

                if ((double) f >= 0.1D) {
                    boolean flag1 = entityplayer.capabilities.isCreativeMode || (arrowStack.getItem() instanceof ItemArrow && ((ItemArrow) arrowStack.getItem()).isInfinite(arrowStack, stack, entityplayer));

                    if (!worldIn.isRemote) {
                        list.applyDamage(entityplayer);
                        if (list.hasMul()) {
                            list.getArrowMultiplier().handleAction(this, worldIn, stack, entityplayer, f, arrowStack, flag1, list, isLoaded);
                        } else {
                            EntityArrow entityarrow = ArrowUtil.createArrowComplete(worldIn, stack, arrowStack, entityplayer, this, f, flag1, 0, 0, list, isLoaded);
                            worldIn.spawnEntity(entityarrow);
                        }
                    }

                    worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                    if (!flag1 && !entityplayer.capabilities.isCreativeMode) {

                        boolean useNormal = true;

                        energyTest:
                        {
                            if (isLoaded) {
                                ExtraBowsEnergy extraBowsEnergy = (ExtraBowsEnergy) stack.getCapability(CapabilityEnergy.ENERGY, null);

                                if (extraBowsEnergy.getEnergyStored() < CustomBowSettings.ENERGY_COST_PER_ARROW) {
                                    break energyTest;
                                }
                                useNormal = false;
                                if (list.hasMul()) {
                                    list.getArrowMultiplier().removeEnergy(arrowStack, extraBowsEnergy);
                                } else {
                                    if (extraBowsEnergy.getEnergyStored() >= CustomBowSettings.ENERGY_COST_PER_ARROW) {
                                        extraBowsEnergy.extractEnergy(CustomBowSettings.ENERGY_COST_PER_ARROW, false);
                                    }
                                }
                            }
                        }

                        if (useNormal) {
                            if (list.hasMul()) {
                                list.getArrowMultiplier().shrinkStack(arrowStack);
                            } else {
                                arrowStack.shrink(1);

                                if (arrowStack.isEmpty()) {
                                    entityplayer.inventory.deleteStack(arrowStack);
                                }
                            }

                            if (!worldIn.isRemote) {
                                if (stack.getItemDamage() == stack.getMaxDamage()) {
                                    list.dropItems(entityplayer);
                                    stack.damageItem(1, entityplayer);
                                }
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
        UpgradeList list = UpgradeUtil.getUpgradesFromStack(stack);

        tooltip.add("Press B to open the Upgrade inventory");
        if (!list.hasMul() && !list.hasMods()) {
            tooltip.add(new TextComponentTranslation("message.no_upgrades.text").getUnformattedText());
        } else {
            tooltip.add("Upgrades:");
        }
        if (list.hasMul()) {
            tooltip.add("Arrow Multiplier:");
            tooltip.add("- " + list.getArrowMultiplier().getName());
        }
        if (list.hasMods()) {
            tooltip.add("Arrow Modifiers:");
            for (BasicUpgrade upgrade : list.getArrowModifiers()) {
                tooltip.add("- " + upgrade.getName());
            }
        }

        if (isElectric(list, stack)) {
            ExtraBowsEnergy storage = (ExtraBowsEnergy) stack.getCapability(CapabilityEnergy.ENERGY, null);
            if (storage != null) {
                tooltip.add("Energy: " + storage.getEnergyStored() + "/" + storage.getMaxEnergyStored() + "RF");
            }
        }

    }

    @Nullable
    @Override
    public NBTTagCompound getNBTShareTag(ItemStack stack) {

        NBTTagCompound compound = super.getNBTShareTag(stack);

        if(compound == null) {
            compound = new NBTTagCompound();
        }

        ExtraBowsEnergy extraBowsEnergy = (ExtraBowsEnergy) stack.getCapability(CapabilityEnergy.ENERGY,null);
        if(extraBowsEnergy != null) {
            compound.setInteger("energy",extraBowsEnergy.getEnergyStored());
            compound.setInteger("maxEnergy",extraBowsEnergy.getMaxEnergyStored());
        }

        ItemStackHandler handler = (ItemStackHandler) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,null);

        if(handler != null) {
            compound.setTag("itemHandlerData",handler.serializeNBT());
        }

        return compound;
    }

    @Override
    public boolean getShareTag() {
        return true;
    }

    @Override
    public void readNBTShareTag(ItemStack stack, @Nullable NBTTagCompound nbt) {
        super.readNBTShareTag(stack, nbt);

        if(nbt != null) {
            ExtraBowsEnergy extraBowsEnergy = (ExtraBowsEnergy) stack.getCapability(CapabilityEnergy.ENERGY,null);
            ItemStackHandler handler = (ItemStackHandler) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,null);

            if(extraBowsEnergy != null) {
                int energy = nbt.getInteger("energy");
                int maxEnergy = nbt.getInteger("maxEnergy");
                extraBowsEnergy.setEnergy(energy);
                extraBowsEnergy.setCapacity(maxEnergy);
            }

            if(handler != null) {
                NBTTagCompound tag = nbt.getCompoundTag("itemHandlerData");
                handler.deserializeNBT(tag);
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
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new BowCapability(stack);
    }

    @Override
    public boolean getIsRepairable(ItemStack left, ItemStack right) {
        return right.getItem().equals(settings.getType());
    }

    private boolean isElectric(UpgradeList list, ItemStack stack) {
        return list.contains(Upgrades.ELECTRIC_UPGRADE) || stack.getItem() instanceof ItemElectricBow;
    }

    private boolean isLoaded(UpgradeList list, ItemStack stack) {
        boolean b = list.contains(Upgrades.ELECTRIC_UPGRADE) || stack.getItem() instanceof ItemElectricBow;
        if (b) {
            return stack.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored() >= CustomBowSettings.ENERGY_COST_PER_ARROW;
        }
        return false;
    }

    public void onArrowCreate(EntityArrow arrow) {

    }

    public void onArrowHit(EntityArrow arrow) {

    }

    public class BowCapability implements ICapabilitySerializable<NBTTagCompound>, ICapabilityProvider {

        private ItemStackHandler handler = new ItemStackHandler(4);
        private ExtraBowsEnergy energy = new ExtraBowsEnergy(CustomBowSettings.ENERGY_BOW_UPGRADE, CustomBowSettings.ENERGY_RECEIVE, CustomBowSettings.ENERGY_COST_PER_ARROW * 3);

        private ItemStack stack;

        public BowCapability(ItemStack stack) {
            this.stack = stack;
        }

        public BowCapability(ExtraBowsEnergy energy,ItemStack stack) {
            this.energy = energy;
            this.stack = stack;
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY | (capability == CapabilityEnergy.ENERGY && isElectric(UpgradeUtil.getUpgradesFromStack(stack),stack));
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(handler);
            }
            if (capability == CapabilityEnergy.ENERGY) {
                return CapabilityEnergy.ENERGY.cast(energy);
            }
            return null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setTag("handler", handler.serializeNBT());
            nbt.setInteger("capacity", energy.getMaxEnergyStored());
            nbt.setInteger("energy", energy.getEnergyStored());
            return nbt;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            handler.deserializeNBT((NBTTagCompound) nbt.getTag("handler"));
            energy.setCapacity(nbt.getInteger("capacity"));
            energy.setEnergy(nbt.getInteger("energy"));
        }
    }
}
