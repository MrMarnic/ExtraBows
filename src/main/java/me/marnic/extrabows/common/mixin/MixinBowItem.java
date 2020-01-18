package me.marnic.extrabows.common.mixin;

import me.marnic.extrabows.api.upgrade.BasicUpgrade;
import me.marnic.extrabows.api.upgrade.ExtraBowsUtil;
import me.marnic.extrabows.api.upgrade.UpgradeList;
import me.marnic.extrabows.api.util.ArrowUtil;
import me.marnic.extrabows.api.util.RandomUtil;
import me.marnic.extrabows.api.util.UpgradeUtil;
import me.marnic.extrabows.common.items.BasicBow;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * Copyright (c) 22.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

@Mixin(BowItem.class)
public abstract class MixinBowItem extends RangedWeaponItem {
    public MixinBowItem(Settings item$Settings_1) {
        super(item$Settings_1);
    }

    private ItemStack findAmmoNEW(PlayerEntity player)
    {
        if (this.isArrow(player.getStackInHand(Hand.OFF_HAND)))
        {
            return player.getStackInHand(Hand.OFF_HAND);
        }
        else if (this.isArrow(player.getStackInHand(Hand.MAIN_HAND)))
        {
            return player.getStackInHand(Hand.MAIN_HAND);
        }
        else
        {
            for (int i = 0; i < player.inventory.getInvSize(); ++i)
            {
                ItemStack itemstack = player.inventory.getInvStack(i);

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

    @Overwrite
    public void onStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if(stack.getItem().getClass().equals(BowItem.class)) {
            if (entityLiving instanceof PlayerEntity)
            {
                PlayerEntity playerEntity = (PlayerEntity)entityLiving;
                boolean flag = playerEntity.abilities.creativeMode || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;
                ItemStack itemstack = this.findAmmoNEW(playerEntity);
                int i = this.getMaxUseTime(stack) - timeLeft;
                if (i < 0) return;

                UpgradeList list = UpgradeUtil.getUpgradesFromStackNEW(stack);
                if (!itemstack.isEmpty() || flag)
                {
                    if (itemstack.isEmpty())
                    {
                        itemstack = new ItemStack(Items.ARROW);
                    }

                    float f = BowItem.getPullProgress(i);

                    if ((double)f >= 0.1D)
                    {
                        boolean flag1 = flag && itemstack.getItem() == Items.ARROW;

                        if (!worldIn.isClient)
                        {
                            list.applyDamage(playerEntity);
                            if(list.hasMul()) {
                                list.getArrowMultiplier().handleAction(getThis(),worldIn,itemstack,playerEntity,f,stack,flag1,list);
                            }else {
                                ProjectileEntity entityarrow = ArrowUtil.createArrowComplete(worldIn,itemstack,playerEntity,getThis(),f,stack,flag1,0,0,list);

                                worldIn.spawnEntity(entityarrow);
                            }
                        }

                        worldIn.playSound((PlayerEntity)null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (RandomUtil.RANDOM.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                        if (!flag1 && !playerEntity.abilities.creativeMode)
                        {
                            if(list.hasMul()) {
                                list.getArrowMultiplier().shrinkStack(itemstack);
                            }else {
                                ExtraBowsUtil.shrinkStack(itemstack,1);
                            }

                            if(!worldIn.isClient) {
                                if(stack.getDamage()==stack.getMaxDamage()) {
                                    list.dropItems(playerEntity);
                                    stack.damage(1,playerEntity,(p) -> p.sendToolBreakStatus(p.getActiveHand()));
                                }
                            }
                        }

                        playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                    }
                }
            }
        }
    }

    public BowItem getThis() {
        return (BowItem)(Object)this;
    }
}
