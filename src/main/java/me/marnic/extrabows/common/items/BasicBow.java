package me.marnic.extrabows.common.items;

import me.marnic.extrabows.api.energy.ExtraBowsEnergy;
import me.marnic.extrabows.api.item.BasicItem;
import me.marnic.extrabows.api.upgrade.BasicUpgrade;
import me.marnic.extrabows.api.upgrade.UpgradeList;
import me.marnic.extrabows.api.upgrade.Upgrades;
import me.marnic.extrabows.api.util.ArrowUtil;
import me.marnic.extrabows.api.util.RandomUtil;
import me.marnic.extrabows.api.util.UpgradeUtil;
import me.marnic.extrabows.common.items.bows.ItemElectricBow;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Copyright (c) 24.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BasicBow extends BowItem implements BasicItem {

    private CustomBowSettings settings;

    public BasicBow(String name) {
        super(new Properties().group(ExtraBowsObjects.CREATIVE_TAB).maxStackSize(1));
        createItem(name);
    }

    public BasicBow(String name, Properties builder, CustomBowSettings settings) {
        super(builder.maxStackSize(1));
        this.settings = settings;
        createItem(name);
    }

    @Override
    public void initConfigOptions() {
        initClientConfigOptions();
    }

    @OnlyIn(Dist.CLIENT)
    private void initClientConfigOptions() {
        ItemModelsProperties.func_239418_a_(this,new ResourceLocation("pull"),(p_210310_0_, p_210310_1_, p_210310_2_) -> {
            if (p_210310_2_ == null) {
                return 0.0F;
            } else {
                return !(p_210310_2_.getActiveItemStack().getItem() instanceof BowItem) ? 0.0F : (float)(p_210310_0_.getUseDuration() - p_210310_2_.getItemInUseCount()) / settings.getTime();
            }
        });

        ItemModelsProperties.func_239418_a_(this, new ResourceLocation("pulling"), (p_239428_0_, p_239428_1_, p_239428_2_) -> {
            return p_239428_2_ != null && p_239428_2_.isHandActive() && p_239428_2_.getActiveItemStack() == p_239428_0_ ? 1.0F : 0.0F;
        });
    }

    private ItemStack findAmmoNEW(PlayerEntity player, boolean electric) {
        if (electric) {
            return new ItemStack(Items.ARROW);
        }
        if (this.isArrow(player.getHeldItem(Hand.OFF_HAND))) {
            return player.getHeldItem(Hand.OFF_HAND);
        } else if (this.isArrow(player.getHeldItem(Hand.MAIN_HAND))) {
            return player.getHeldItem(Hand.MAIN_HAND);
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
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (isLoaded(UpgradeUtil.getUpgradesFromStack(stack), stack)) {
            playerIn.setActiveHand(handIn);
            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity entityplayer = (PlayerEntity) entityLiving;
            boolean flag = entityplayer.abilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;

            UpgradeList list = UpgradeUtil.getUpgradesFromStack(stack);

            boolean isLoaded = isLoaded(list, stack);

            ItemStack arrowStack = this.findAmmoNEW(entityplayer, isLoaded);

            int i = this.getUseDuration(stack) - timeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, entityplayer, i, !arrowStack.isEmpty() || flag);
            if (i < 0) return;

            if (!arrowStack.isEmpty() || flag) {
                if (arrowStack.isEmpty()) {
                    arrowStack = new ItemStack(Items.ARROW);
                }

                float f = ArrowUtil.getArrowVelocity(i, this);

                if ((double) f >= 0.1D) {
                    boolean flag1 = entityplayer.abilities.isCreativeMode || (arrowStack.getItem() instanceof ArrowItem && ((ArrowItem) arrowStack.getItem()).isInfinite(arrowStack, stack, entityplayer));

                    if (!worldIn.isRemote) {
                        list.applyDamage(entityplayer);
                        if (list.hasMul()) {
                            list.getArrowMultiplier().handleAction(this, worldIn, stack, entityplayer, f, arrowStack, flag1, list, isLoaded);
                        } else {
                            AbstractArrowEntity entityarrow = ArrowUtil.createArrowComplete(worldIn, stack, arrowStack, entityplayer, this, f, flag1, 0, 0, list, isLoaded);

                            worldIn.addEntity(entityarrow);
                        }
                    }

                    worldIn.playSound(null, entityplayer.getPosX(), entityplayer.getPosY(), entityplayer.getPosZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (RandomUtil.RANDOM.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                    if (!flag1 && !entityplayer.abilities.isCreativeMode) {

                        boolean useNormal = true;

                        energyTest:
                        {
                            if (isLoaded) {
                                ExtraBowsEnergy extraBowsEnergy = (ExtraBowsEnergy) stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);

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
                                if (stack.getDamage() == stack.getMaxDamage()) {
                                    list.dropItems(entityplayer);
                                    stack.damageItem(1, entityplayer, (p) -> p.sendBreakAnimation(p.getActiveHand()));
                                }
                            }
                        }
                    }

                    entityplayer.addStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        UpgradeList list = UpgradeUtil.getUpgradesFromStack(stack);

        tooltip.add(new StringTextComponent("Press B to open the Upgrade inventory"));
        if (!list.hasMul() && !list.hasMods()) {
            tooltip.add(new TranslationTextComponent("message.no_upgrades.text"));
        } else {
            tooltip.add(new StringTextComponent("Upgrades:"));
        }
        if (list.hasMul()) {
            tooltip.add(new StringTextComponent("Arrow Multiplier:"));
            tooltip.add(new StringTextComponent("- " + list.getArrowMultiplier().getName()));
        }
        if (list.hasMods()) {
            tooltip.add(new StringTextComponent("Arrow Modifiers:"));
            for (BasicUpgrade upgrade : list.getArrowModifiers()) {
                tooltip.add(new StringTextComponent("- " + upgrade.getName()));
            }
        }

        if (isElectric(list, stack) && worldIn != null) {
            ExtraBowsEnergy storage = (ExtraBowsEnergy) stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
            if (storage != null) {
                tooltip.add(new StringTextComponent("Energy: " + storage.getEnergyStored() + "/" + storage.getMaxEnergyStored() + "RF"));
            }
        }

    }

    @Nullable
    @Override
    public CompoundNBT getShareTag(ItemStack stack) {

        CompoundNBT compound = super.getShareTag(stack);

        if (compound == null) {
            compound = new CompoundNBT();
        }

        ExtraBowsEnergy extraBowsEnergy = (ExtraBowsEnergy) stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
        if (extraBowsEnergy != null) {
            compound.putInt("energy", extraBowsEnergy.getEnergyStored());
            compound.putInt("maxEnergy", extraBowsEnergy.getMaxEnergyStored());
        }

        ItemStackHandler handler = (ItemStackHandler) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);

        if (handler != null) {
            compound.put("itemHandlerData", handler.serializeNBT());
        }

        return compound;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        super.readShareTag(stack, nbt);

        if (nbt != null) {
            ExtraBowsEnergy extraBowsEnergy = (ExtraBowsEnergy) stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
            ItemStackHandler handler = (ItemStackHandler) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);

            if (extraBowsEnergy != null) {
                int energy = nbt.getInt("energy");
                int maxEnergy = nbt.getInt("maxEnergy");
                extraBowsEnergy.setEnergy(energy);
                extraBowsEnergy.setCapacity(maxEnergy);
            }

            if (handler != null) {
                CompoundNBT tag = nbt.getCompound("itemHandlerData");
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

    public void setSettings(CustomBowSettings settings) {
        this.settings = settings;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
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
            return stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null).getEnergyStored() >= CustomBowSettings.ENERGY_COST_PER_ARROW;
        }
        return false;
    }

    public void onArrowCreate(AbstractArrowEntity arrow) {

    }

    public void onArrowHit(AbstractArrowEntity arrow) {

    }

    private boolean isArrow(ItemStack stack) {
        return stack.getItem() instanceof ArrowItem;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return settings.getMaxUses();
    }

    @Override
    public boolean isDamageable() {
        return settings != null && settings.getMaxUses() > 0;
    }

    public class BowCapability implements ICapabilitySerializable<CompoundNBT>, ICapabilityProvider {

        private ItemStackHandler handler = new ItemStackHandler(4);
        private ExtraBowsEnergy energy = new ExtraBowsEnergy(CustomBowSettings.ENERGY_BOW_UPGRADE, CustomBowSettings.ENERGY_RECEIVE, CustomBowSettings.ENERGY_COST_PER_ARROW * 3);

        private LazyOptional<IItemHandler> handlerLazyOptional = LazyOptional.of(() -> handler);
        private LazyOptional<IEnergyStorage> energyStorage = LazyOptional.of(() -> energy);

        private ItemStack stack;

        public BowCapability(ItemStack stack) {
            this.stack = stack;
        }

        public BowCapability(ExtraBowsEnergy energy, ItemStack stack) {
            this.energy = energy;
            this.stack = stack;
        }

        @Nullable
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
            if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
                return handlerLazyOptional.cast();
            }
            if (capability == CapabilityEnergy.ENERGY && isElectric(UpgradeUtil.getUpgradesFromStack(stack), stack)) {
                return energyStorage.cast();
            }
            return LazyOptional.empty();
        }

        @Override
        public CompoundNBT serializeNBT() {
            CompoundNBT nbt = new CompoundNBT();
            nbt.put("handler", handler.serializeNBT());
            nbt.putInt("capacity", energy.getMaxEnergyStored());
            nbt.putInt("energy", energy.getEnergyStored());
            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            handler.deserializeNBT(nbt.getCompound("handler"));
            energy.setCapacity(nbt.getInt("capacity"));
            energy.setEnergy(nbt.getInt("energy"));
        }
    }
}
