package me.marnic.extrabows.common.items;

import me.marnic.extrabows.api.item.BasicItem;
import me.marnic.extrabows.api.upgrade.BasicUpgrade;
import me.marnic.extrabows.api.upgrade.UpgradeList;
import me.marnic.extrabows.api.util.ArrowUtil;
import me.marnic.extrabows.api.util.ExtraBowsUtil;
import me.marnic.extrabows.api.util.RandomUtil;
import me.marnic.extrabows.api.util.UpgradeUtil;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.core.util.ReflectionUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Copyright (c) 24.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BasicBow extends BowItem implements BasicItem{

    private CustomBowSettings settings;

    public BasicBow(Properties properties,String name) {
        super(properties.maxDamage(100).group(ExtraBowsObjects.CREATIVE_TAB));
        createItem(name);
    }

    public BasicBow(Properties properties,CustomBowSettings settings) {
        super(properties.maxDamage(100));
        setSettings(settings);
    }

    public BasicBow(String name) {
        super(new Properties().maxDamage(100).group(ExtraBowsObjects.CREATIVE_TAB));
        createItem(name);
    }

    public void setSettings(CustomBowSettings settings) {
        this.settings = settings;
    }

    @Override
    public void initConfigOptions() {
        this.addPropertyOverride(new ResourceLocation("pull"), (p_210310_0_, p_210310_1_, p_210310_2_) -> {
            if (p_210310_2_ == null) {
                return 0.0F;
            } else {
                return !(p_210310_2_.getActiveItemStack().getItem() instanceof BowItem) ? 0.0F : (float)(p_210310_0_.getUseDuration() - p_210310_2_.getItemInUseCount()) / settings.getTime();
            }
        });

        try {
            Field field = Item.class.getDeclaredField("maxDamage");
            field.setAccessible(true);

            field.set(this,getSettings().getMaxUses());
            field.setAccessible(false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private ItemStack findAmmoNEW(PlayerEntity player)
    {
        if (this.isArrow(player.getHeldItem(Hand.OFF_HAND)))
        {
            return player.getHeldItem(Hand.OFF_HAND);
        }
        else if (this.isArrow(player.getHeldItem(Hand.MAIN_HAND)))
        {
            return player.getHeldItem(Hand.MAIN_HAND);
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
    
    private boolean isArrow(ItemStack stack) {
        return stack.getItem() instanceof ArrowItem;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        stack.damageItem(1,entityLiving,(p) -> p.sendBreakAnimation(p.getActiveHand()));
        if (entityLiving instanceof PlayerEntity)
        {
            PlayerEntity playerEntity = (PlayerEntity)entityLiving;
            boolean flag = playerEntity.abilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack itemstack = this.findAmmoNEW(playerEntity);

            int i = this.getUseDuration(stack) - timeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, playerEntity, i, !itemstack.isEmpty() || flag);
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
                    boolean flag1 = playerEntity.abilities.isCreativeMode || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem) itemstack.getItem()).isInfinite(itemstack, stack, playerEntity));

                    if (!worldIn.isRemote)
                    {
                        list.applyDamage(playerEntity);
                        if(list.hasMul()) {
                            list.getArrowMultiplier().handleAction(this,worldIn,itemstack,playerEntity,f,stack,flag1,list);
                        }else {
                            ArrowEntity entityarrow = ArrowUtil.createArrowComplete(worldIn,itemstack,playerEntity,this,f,stack,flag1,0,0,list);

                            worldIn.addEntity(entityarrow);
                        }
                    }

                    worldIn.playSound((PlayerEntity)null, playerEntity.posX, playerEntity.posY, playerEntity.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (RandomUtil.RANDOM.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                    if (!flag1 && !playerEntity.abilities.isCreativeMode)
                    {
                        if(list.hasMul()) {
                            list.getArrowMultiplier().shrinkStack(itemstack);
                        }else {
                            itemstack.shrink(1);

                            if (itemstack.isEmpty())
                            {
                                playerEntity.inventory.deleteStack(itemstack);
                            }
                        }

                        if(!worldIn.isRemote) {
                            if(stack.getDamage()==stack.getMaxDamage()) {
                                list.dropItems(playerEntity);
                                ExtraBowsUtil.damageItemStack(stack,playerEntity);
                            }
                        }
                    }

                    playerEntity.addStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        UpgradeList list = UpgradeUtil.getUpgradesFromStackNEW(stack);
        tooltip.add(new StringTextComponent("Press B to open the Upgrade inventory"));
        if(!list.hasMul() && !list.hasMods()) {
            tooltip.add(new StringTextComponent(new TranslationTextComponent("message.no_upgrades.text").getUnformattedComponentText()));
        }else {
            tooltip.add(new StringTextComponent("Upgrades:"));
        }
        if(list.hasMul()) {
            tooltip.add(new StringTextComponent("Arrow Multiplier:"));
            tooltip.add(new StringTextComponent("- " + list.getArrowMultiplier().getName()));
        }
        if(list.hasMods()) {
            tooltip.add(new StringTextComponent("Arrow Modifiers:"));
            for(BasicUpgrade upgrade:list.getArrowModifiers()) {
                tooltip.add(new StringTextComponent("- " + upgrade.getName()));
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
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new StorageProvider();
    }
}

class StorageProvider implements ICapabilitySerializable<CompoundNBT>, ICapabilityProvider {

    public ItemStackHandler handler = new ItemStackHandler(4);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return getCapability(cap);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability) {
        if(capability== CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return LazyOptional.of(new NonNullSupplier<T>() {
                @Nonnull
                @Override
                public T get() {
                    return (T) handler;
                }
            });
        }
        return null;
    }

    @Override
    public CompoundNBT serializeNBT() {
        return handler.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        handler.deserializeNBT(nbt);
    }
}
