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
import net.minecraft.nbt.INBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
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
        super(new Properties().tab(ExtraBowsObjects.CREATIVE_TAB).stacksTo(1));
        createItem(name);
    }

    public BasicBow(String name, Properties builder, CustomBowSettings settings) {
        super(builder.stacksTo(1));
        this.settings = settings;
        createItem(name);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initConfigOptions() {
        ItemModelsProperties.register(this,new ResourceLocation("pull"),(p_210310_0_, p_210310_1_, p_210310_2_) -> {
            if (p_210310_2_ == null) {
                return 0.0F;
            } else {
                return !(p_210310_2_.getMainHandItem().getItem() instanceof BowItem) ? 0.0F : (float)(p_210310_0_.getUseDuration() - p_210310_2_.getUseItemRemainingTicks()) / settings.getTime();
            }
        });

        ItemModelsProperties.register(this, new ResourceLocation("pulling"), (p_239428_0_, p_239428_1_, p_239428_2_) -> {
            return p_239428_2_ != null && p_239428_2_.isUsingItem() && p_239428_2_.getMainHandItem() == p_239428_0_ ? 1.0F : 0.0F;
        });
    }

    private ItemStack findAmmoNEW(PlayerEntity player, boolean electric) {
        if (electric) {
            return new ItemStack(Items.ARROW);
        }
        if (this.isArrow(player.getItemInHand(Hand.OFF_HAND))) {
            return player.getItemInHand(Hand.OFF_HAND);
        } else if (this.isArrow(player.getItemInHand(Hand.MAIN_HAND))) {
            return player.getItemInHand(Hand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.inventory.getContainerSize(); ++i) {
                ItemStack itemstack = player.inventory.getItem(i);

                if (this.isArrow(itemstack)) {
                    return itemstack;
                }
            }

            return ItemStack.EMPTY;
        }
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        if (isLoaded(UpgradeUtil.getUpgradesFromStack(stack), stack)) {
            playerIn.setItemInHand(handIn, stack);
            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }
        return super.use(worldIn, playerIn, handIn);
    }

    @Override
    public int getUseDuration(ItemStack p_77626_1_) {
        return (int)(settings.getTime() * 20);
    }

    @Nullable
    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        CompoundNBT baseTag = stack.getTag();
        ItemStackHandler itemStackHandlerFlowerBag = (ItemStackHandler)stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,null).orElse(null);
        CompoundNBT itemStackHandlerCapabilityTag = itemStackHandlerFlowerBag.serializeNBT();
        CompoundNBT combinedTag = new CompoundNBT();
        if (baseTag != null) {
            combinedTag.put("base", baseTag);
        }
        if (itemStackHandlerCapabilityTag != null) {
            combinedTag.put("capItem", itemStackHandlerCapabilityTag);
        }
        stack.getCapability(CapabilityEnergy.ENERGY, null).ifPresent((energy) -> {
            CompoundNBT energyCapabilityTag = ((ExtraBowsEnergy)energy).serializeNBT();
            combinedTag.put("capEnergy", energyCapabilityTag);
        });
        return combinedTag;
    }

    /** Retrieve our capability information from the transmitted NBT information
     *
     * @param stack The stack that received NBT
     * @param nbt   Received NBT, can be null
     */
    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        if (nbt == null) {
            stack.setTag(null);
            return;
        }
        CompoundNBT baseTag = nbt.getCompound("base");              // empty if not found
        CompoundNBT capabilityTag = nbt.getCompound("capItem"); // empty if not found
        CompoundNBT capabilityEnergy = nbt.getCompound("capEnergy");
        stack.setTag(baseTag);
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,null).ifPresent((handler) -> {
            ((ItemStackHandler)handler).deserializeNBT(capabilityTag);
        });
        stack.getCapability(CapabilityEnergy.ENERGY, null).ifPresent((extraBowsEnergy) -> {
            ((ExtraBowsEnergy)extraBowsEnergy).deserializeNBT(capabilityEnergy);
        });
    }

    @Override
    public void releaseUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity entityplayer = (PlayerEntity) entityLiving;
            boolean flag = entityplayer.abilities.instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;

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
                    boolean flag1 = entityplayer.abilities.instabuild || (arrowStack.getItem() instanceof ArrowItem && ((ArrowItem) arrowStack.getItem()).isInfinite(arrowStack, stack, entityplayer));

                    if (!worldIn.isClientSide) {
                        list.applyDamage(entityplayer);
                        if (list.hasMul()) {
                            list.getArrowMultiplier().handleAction(this, worldIn, stack, entityplayer, f, arrowStack, flag1, list, isLoaded);
                        } else {
                            AbstractArrowEntity entityarrow = ArrowUtil.createArrowComplete(worldIn, stack, arrowStack, entityplayer, this, f, flag1, 0, 0, list, isLoaded);

                            worldIn.addFreshEntity(entityarrow);
                        }
                    }

                    worldIn.playSound(null, entityplayer.getX(), entityplayer.getY(), entityplayer.getZ(), SoundEvents.ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (RandomUtil.RANDOM.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                    if (!flag1 && !entityplayer.abilities.instabuild) {

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
                                    entityplayer.inventory.removeItem(arrowStack);
                                }
                            }

                            if (!worldIn.isClientSide) {
                                if (stack.getDamageValue() == stack.getMaxDamage()) {
                                    list.dropItems(entityplayer);
                                    stack.hurtAndBreak(1, entityplayer, (p) -> p.broadcastBreakEvent(p.getUsedItemHand()));
                                }
                            }
                        }
                    }

                    entityplayer.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        UpgradeList list = UpgradeUtil.getUpgradesFromStack(stack);

        tooltip.add(new StringTextComponent("Press B to open the Upgrade inventory").withStyle(TextFormatting.GRAY));
        if (!list.hasMul() && !list.hasMods()) {
            tooltip.add(new TranslationTextComponent("message.no_upgrades.text").withStyle(TextFormatting.GRAY));
        } else {
            tooltip.add(new StringTextComponent("Upgrades:").withStyle(TextFormatting.GRAY));
        }
        if (list.hasMul()) {
            tooltip.add(new StringTextComponent("Arrow Multiplier:").withStyle(TextFormatting.GRAY));
            tooltip.add(new StringTextComponent("- " + list.getArrowMultiplier().getName()).withStyle(TextFormatting.GRAY));
        }
        if (list.hasMods()) {
            tooltip.add(new StringTextComponent("Arrow Modifiers:").withStyle(TextFormatting.GRAY));
            for (BasicUpgrade upgrade : list.getArrowModifiers()) {
                tooltip.add(new StringTextComponent("- " + upgrade.getName()).withStyle(TextFormatting.GRAY));
            }
        }

        if (isElectric(list, stack) && worldIn != null) {
            ExtraBowsEnergy storage = (ExtraBowsEnergy) stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
            if (storage != null) {
                tooltip.add(new StringTextComponent("Energy: " + storage.getEnergyStored() + "/" + storage.getMaxEnergyStored() + "RF").withStyle(TextFormatting.GRAY));
            }
        }

    }

   /*
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
    */

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
    public boolean isValidRepairItem(ItemStack left, ItemStack right) {
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
    public boolean isDamageable(ItemStack stack) {
        return settings != null && settings.getMaxUses() > 0;
    }

    public class BowCapability implements ICapabilitySerializable<INBT>, ICapabilityProvider {

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
        public INBT serializeNBT() {
            INBT tag1 = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(handler, null);
            INBT tag2 = CapabilityEnergy.ENERGY.writeNBT(energyStorage.orElse(null),null);

            CompoundNBT nbt = new CompoundNBT();
            nbt.put("cap1",tag1);
            nbt.put("cap2",tag2);

            return nbt;
            /*
                        CompoundNBT nbt = new CompoundNBT();
            nbt.put("handler", handler.serializeNBT());
            nbt.putInt("capacity", energy.getMaxEnergyStored());
            nbt.putInt("energy", energy.getEnergyStored());
            return nbt;
             */
        }

        @Override
        public void deserializeNBT(INBT nbt) {

            CompoundNBT c = (CompoundNBT)nbt;

            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(handler, null, c.get("cap1"));
            CapabilityEnergy.ENERGY.readNBT(energyStorage.orElse(null),null, c.get("cap2"));

            //handler.deserializeNBT(nbt.getCompound("handler"));
            //energy.setCapacity(nbt.getInt("capacity"));
            //energy.setEnergy(nbt.getInt("energy"));
        }
    }
}
