package me.marnic.extrabows.common.items;

import me.marnic.extrabows.api.registry.ExtraBowsRegistry;
import me.marnic.extrabows.api.upgrade.BasicUpgrade;
import me.marnic.extrabows.api.upgrade.ExtraBowsUtil;
import me.marnic.extrabows.api.upgrade.UpgradeList;
import me.marnic.extrabows.api.util.ArrowUtil;
import me.marnic.extrabows.api.util.RandomUtil;
import me.marnic.extrabows.api.util.UpgradeUtil;
import me.marnic.extrabows.common.items.bows.BowSettings;
import me.marnic.extrabows.common.main.ExtraBowsObjects;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.List;

/**
 * Copyright (c) 07.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BasicBow extends BowItem {

    private BowSettings settings;

    public BasicBow(BowSettings settings) {
        super(settings.isRegister() ? new Settings().maxDamage(settings.getMaxUses()).group(ExtraBowsObjects.CREATIVE_TAB) : new Settings().maxDamage(settings.getMaxUses()).group(ItemGroup.COMBAT));
        this.settings = settings;

        if(settings.isRegister()) {
            ExtraBowsRegistry.register(this,settings.getName());
        }


        this.addPropertyGetter(new Identifier("pull"), (itemStack_1, world_1, livingEntity_1) -> {
            if (livingEntity_1 == null) {
                return 0.0F;
            } else {
                return !(livingEntity_1.getActiveItem().getItem() instanceof BowItem) ? 0.0F : (float)(itemStack_1.getMaxUseTime() - livingEntity_1.getItemUseTimeLeft()) / settings.getTime();
            }
        });
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

    @Override
    public void onStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
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

                float f = ArrowUtil.getArrowVelocity(i,this);

                if ((double)f >= 0.1D)
                {
                    boolean flag1 = flag && itemstack.getItem() == Items.ARROW;

                    if (!worldIn.isClient)
                    {
                        list.applyDamage(playerEntity);
                        if(list.hasMul()) {
                            list.getArrowMultiplier().handleAction(this,worldIn,itemstack,playerEntity,f,stack,flag1,list);
                        }else {
                            ProjectileEntity entityarrow = ArrowUtil.createArrowComplete(worldIn,itemstack,playerEntity,this,f,stack,flag1,0,0,list);

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

    public BowSettings getSettings() {
        return settings;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world_1, List<Text> tooltip, TooltipContext tooltipContext_1) {
        UpgradeList list = UpgradeUtil.getUpgradesFromStackNEW(stack);
        tooltip.add(new LiteralText("Press B to open the Upgrade inventory"));
        if(!list.hasMul() && !list.hasMods()) {
            tooltip.add(new LiteralText(new TranslatableText("message.no_upgrades.text").asString()));
        }else {
            tooltip.add(new LiteralText("Upgrades:"));
        }
        if(list.hasMul()) {
            tooltip.add(new LiteralText("Arrow Multiplier:"));
            tooltip.add(new LiteralText("- " + list.getArrowMultiplier().getName()));
        }
        if(list.hasMods()) {
            tooltip.add(new LiteralText("Arrow Modifiers:"));
            for(BasicUpgrade upgrade:list.getArrowModifiers()) {
                tooltip.add(new LiteralText("- " + upgrade.getName()));
            }
        }
    }

    @Override
    public UseAction getUseAction(ItemStack itemStack_1) {
        return UseAction.BOW;
    }

    @Override
    public boolean canRepair(ItemStack left, ItemStack right) {
        return right.getItem().equals(settings.getType());
    }
}
