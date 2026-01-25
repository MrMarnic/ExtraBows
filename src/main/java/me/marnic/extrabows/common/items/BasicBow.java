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
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
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
        ItemProperties.register(this,new ResourceLocation("pull"),(p_210310_0_, p_210310_1_, p_210310_2_,f) -> {
            if (p_210310_2_ == null) {
                return 0.0F;
            } else {
                return !(p_210310_2_.getMainHandItem().getItem() instanceof BowItem) ? 0.0F : (float)(p_210310_0_.getUseDuration() - p_210310_2_.getUseItemRemainingTicks()) / settings.getTime();
            }
        });

        ItemProperties.register(this, new ResourceLocation("pulling"), (p_239428_0_, p_239428_1_, p_239428_2_,f) -> {
            return p_239428_2_ != null && p_239428_2_.isUsingItem() && p_239428_2_.getMainHandItem() == p_239428_0_ ? 1.0F : 0.0F;
        });
    }

    private ItemStack findAmmoNEW(Player player, boolean electric) {
        if (electric) {
            return new ItemStack(Items.ARROW);
        }
        if (this.isArrow(player.getItemInHand(InteractionHand.OFF_HAND))) {
            return player.getItemInHand(InteractionHand.OFF_HAND);
        } else if (this.isArrow(player.getItemInHand(InteractionHand.MAIN_HAND))) {
            return player.getItemInHand(InteractionHand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
                ItemStack itemstack = player.getInventory().getItem(i);

                if (this.isArrow(itemstack)) {
                    return itemstack;
                }
            }

            return ItemStack.EMPTY;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level LevelIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        if (isLoaded(UpgradeUtil.getUpgradesFromStack(stack), stack)) {
            playerIn.setItemInHand(handIn, stack);
            return InteractionResultHolder.consume(stack);
        }
        return super.use(LevelIn, playerIn, handIn);
    }

    @Override
    public int getUseDuration(ItemStack p_77626_1_) {
        return (int)(settings.getTime() * 20);
    }

    /*
    @Nullable
    @Override
    public CompoundTag getShareTag(ItemStack stack) {
        CompoundTag baseTag = super.getShareTag(stack);
        ItemStackHandler itemStackHandler = (ItemStackHandler)stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,null).orElse(null);
        CompoundTag itemStackHandlerCapabilityTag = itemStackHandler.serializeNBT();
        CompoundTag combinedTag = new CompoundTag();
        if (baseTag != null) {
            combinedTag.put("base", baseTag);
        }
        if (itemStackHandlerCapabilityTag != null) {
            combinedTag.put("capItem", itemStackHandlerCapabilityTag);
        }
        stack.getCapability(CapabilityEnergy.ENERGY, null).ifPresent((energy) -> {
            CompoundTag energyCapabilityTag = (CompoundTag)((ExtraBowsEnergy)energy).serializeNBT();
            combinedTag.put("capEnergy", energyCapabilityTag);
        });
        return combinedTag;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        super.readShareTag(stack,nbt);

        if (nbt == null) {
            stack.setTag(null);
            return;
        }
        CompoundTag baseTag = nbt.getCompound("base");              // empty if not found
        CompoundTag capabilityTag = nbt.getCompound("capItem"); // empty if not found
        CompoundTag capabilityEnergy = nbt.getCompound("capEnergy");
        stack.setTag(baseTag);
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,null).ifPresent((handler) -> {
            ((ItemStackHandler)handler).deserializeNBT(capabilityTag);
        });
        stack.getCapability(CapabilityEnergy.ENERGY, null).ifPresent((extraBowsEnergy) -> {
            ((ExtraBowsEnergy)extraBowsEnergy).deserializeNBT(capabilityEnergy);
        });
    }
     */

    @Override
    public void releaseUsing(ItemStack stack, Level levelIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player) {
            Player entityplayer = (Player) entityLiving;
            boolean flag = entityplayer.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;

            UpgradeList list = UpgradeUtil.getUpgradesFromStack(stack);

            boolean isLoaded = isLoaded(list, stack);

            ItemStack arrowStack = this.findAmmoNEW(entityplayer, isLoaded);

            int i = this.getUseDuration(stack) - timeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, levelIn, entityplayer, i, !arrowStack.isEmpty() || flag);
            if (i < 0) return;

            if (!arrowStack.isEmpty() || flag) {
                if (arrowStack.isEmpty()) {
                    arrowStack = new ItemStack(Items.ARROW);
                }

                float f = ArrowUtil.getArrowVelocity(i, this);

                if ((double) f >= 0.1D) {
                    boolean flag1 = entityplayer.getAbilities().instabuild || (arrowStack.getItem() instanceof ArrowItem && ((ArrowItem) arrowStack.getItem()).isInfinite(arrowStack, stack, entityplayer));

                    if (!levelIn.isClientSide) {
                        list.applyDamage(entityplayer);
                        if (list.hasMul()) {
                            list.getArrowMultiplier().handleAction(this, levelIn, stack, entityplayer, f, arrowStack, flag1, list, isLoaded);
                        } else {
                            AbstractArrow entityarrow = ArrowUtil.createArrowComplete(levelIn, stack, arrowStack, entityplayer, this, f, flag1, 0, 0, list, isLoaded);

                            levelIn.addFreshEntity(entityarrow);
                        }
                    }

                    levelIn.playSound(null, entityplayer.getX(), entityplayer.getY(), entityplayer.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (RandomUtil.RANDOM.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                    if (!flag1 && !entityplayer.getAbilities().instabuild) {

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
                                    entityplayer.getInventory().removeItem(arrowStack);
                                }
                            }

                            if (!levelIn.isClientSide) {
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
    public void appendHoverText(ItemStack stack, @Nullable Level LevelIn, List<Component> tooltip, TooltipFlag flagIn) {
        UpgradeList list = UpgradeUtil.getUpgradesFromStack(stack);

        tooltip.add(new TextComponent("Press B to open the Upgrade inventory").withStyle(ChatFormatting.GRAY));
        if (!list.hasMul() && !list.hasMods()) {
            tooltip.add(new TranslatableComponent("message.no_upgrades.text").withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(new TextComponent("Upgrades:").withStyle(ChatFormatting.GRAY));
        }
        if (list.hasMul()) {
            tooltip.add(new TextComponent("Arrow Multiplier:").withStyle(ChatFormatting.GRAY));
            tooltip.add(new TextComponent("- " + list.getArrowMultiplier().getName()).withStyle(ChatFormatting.GRAY));
        }
        if (list.hasMods()) {
            tooltip.add(new TextComponent("Arrow Modifiers:").withStyle(ChatFormatting.GRAY));
            for (BasicUpgrade upgrade : list.getArrowModifiers()) {
                tooltip.add(new TextComponent("- " + upgrade.getName()).withStyle(ChatFormatting.GRAY));
            }
        }

        if (isElectric(list, stack) && LevelIn != null) {
            ExtraBowsEnergy storage = (ExtraBowsEnergy) stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
            if (storage != null) {
                tooltip.add(new TextComponent("Energy: " + storage.getEnergyStored() + "/" + storage.getMaxEnergyStored() + "RF").withStyle(ChatFormatting.GRAY));
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
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
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

    public void onArrowCreate(AbstractArrow arrow) {

    }

    public void onArrowHit(AbstractArrow arrow) {

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

    public class BowCapability implements ICapabilitySerializable<Tag>, ICapabilityProvider {

        private ItemStackHandler handler;
        private ExtraBowsEnergy energy = new ExtraBowsEnergy(CustomBowSettings.ENERGY_BOW_UPGRADE, CustomBowSettings.ENERGY_RECEIVE, CustomBowSettings.ENERGY_COST_PER_ARROW * 3);

        private LazyOptional<IItemHandler> handlerLazyOptional = LazyOptional.of(this::getCached);
        private LazyOptional<IEnergyStorage> energyStorage = LazyOptional.of(() -> energy);

        private ItemStack stack;

        private ItemStackHandler getCached() {
            if (handler == null) {
                handler =  new ItemStackHandler(4) {
                    /** returns true if the contents have changed since the last call.
                     * Resets to false after each call.
                     * @return true if changed since the last call
                     */
                    public boolean isDirty() {
                        boolean currentState = isDirty;
                        isDirty = false;
                        return currentState;
                    }

                    /** Called whenever the contents of the bag have changed.
                     *   We need to do this manually in order to make sure that the server sends a synchronisation packet to the client for the parent ItemStack
                     *   The reason is because capability information is not stored in the ItemStack nbt tag, so vanilla does not notice when
                     *   the flowerbag's capability has changed.
                     * @param slot
                     */
                    @Override
                    protected void onContentsChanged(int slot) {
                        // A problem - the ItemStack and the ItemStackHandler don't know which player is holding the flower bag.  Or in fact whether
                        //   the bag is being held by any player at all.
                        // We have a few choices -
                        // * we can search all the players on the server to see which one is holding the bag; or
                        // * we can try to store the owner of the ItemStack in the ItemStackHandler, ItemStack, or ContainerFlowerBag,
                        //   (which becomes problematic if the owner drops the ItemStack, or if there is no container); or
                        // * we can mark the bag as dirty and let the containerFlowerBag detect that.
                        // I've used the third method because it is easier to code, produces less coupling between classes, and probably more efficient
                        // Fortunately, we only need to manually force an update when the player has the container open.  If changes could occur while the
                        //   item was discarded (inside an ItemEntity) it would be much trickier.
                        isDirty = true;
                    }

                    private boolean isDirty = true;
                };
            }
            return handler;
        }

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
        public Tag serializeNBT() {
            Tag tag1 = getCached().serializeNBT();
            Tag tag2 = null;
            if(energy != null) {
                tag2 = energy.serializeNBT();
            }

            CompoundTag nbt = new CompoundTag();
            nbt.put("capItem",tag1);

            if(tag2 != null) {
                nbt.put("capEnergy",tag2);
            }

            return nbt;
        }

        @Override
        public void deserializeNBT(Tag nbt) {

            CompoundTag c = (CompoundTag)nbt;

            if(c.contains("capItem")) {
                getCached().deserializeNBT(c.getCompound("capItem"));
            }
            if(c.contains("capEnergy")) {
                energy.deserializeNBT(c.getCompound("capEnergy"));
            }
        }
    }
}
