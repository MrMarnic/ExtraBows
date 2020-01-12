package me.marnic.extrabows.api.util;

import com.google.gson.Gson;
import me.marnic.extrabows.api.upgrade.ArrowModifierUpgrade;
import me.marnic.extrabows.api.upgrade.UpgradeList;
import me.marnic.extrabows.common.items.BasicBow;
import me.marnic.extrabows.common.items.bows.BowSettings;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.item.*;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Copyright (c) 26.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ArrowUtil {
    public static ProjectileEntity createArrow(World worldIn, ItemStack stack,ItemStack arrow, LivingEntity shooter, BowItem basicBow, PlayerEntity player)
    {

        ProjectileEntity entity = ((ArrowItem)arrow.getItem()).createArrow(worldIn,arrow,shooter);

        /*CustomDataArrow arrow1 = new CustomDataArrow((EntityType<? extends ProjectileEntity>)entity.getType(),worldIn,entity) {
            private UpgradeList list;
            private boolean alreadyHit = false;

            @Override
            protected void onHit(HitResult hitResult_1) {
                super.onHit(hitResult_1);
                if(hitResult_1.getType() == HitResult.Type.BLOCK) {
                    if(!alreadyHit) {
                        alreadyHit = true;
                        list.handleModifierHittingEvent(ArrowModifierUpgrade.EventType.BLOCK_HIT,new BlockPos(hitResult_1.getPos()),null,world,player,this);
                    }
                }
            }

            @Override
            protected void onHit(LivingEntity livingEntity_1) {
                super.onHit(livingEntity_1);
                list.handleModifierHittingEvent(ArrowModifierUpgrade.EventType.ENTITY_HIT,null,livingEntity_1,world,player,this);
            }

            boolean prevWater;

            @Override
            protected void initDataTracker() {
                super.initDataTracker();
                list = UpgradeUtil.getUpgradesFromStackNEW(stack);
                list.handleModifierEvent(ArrowModifierUpgrade.EventType.ENTITY_INIT,this,player,stack);
            }

            @Override
            public void baseTick() {
                super.baseTick();
                if(inWater) {
                    if(!prevWater) {
                        list.handleModifierHittingEvent(ArrowModifierUpgrade.EventType.WATER_HIT,getBlockPos(),null,world,player,this);
                    }
                }

                if(!alreadyHit) {
                    list.handleOnUpdatedEvent(this,world);
                }
                prevWater = inWater;
            }
        };

        arrow1.copyFrom(entity);*/

        UpgradeUtil.getUpgradesFromStackNEW(stack).handleModifierEvent(ArrowModifierUpgrade.EventType.SET_EFFECT,entity,player,stack);
        return entity;
    }

    public static ProjectileEntity createArrowComplete(World worldIn, ItemStack itemstack, PlayerEntity playerEntity, BowItem basicBow, float f, ItemStack stack, boolean flag1, float inacplus, float yawplus, UpgradeList list) {
        BowSettings settings = null;

        if(basicBow instanceof BasicBow) {
            settings = ((BasicBow)basicBow).getSettings();
        }else {
            settings = BowSettings.DEFAULT;
        }

        ProjectileEntity arrowEntity = ArrowUtil.createArrow(worldIn, stack,itemstack, playerEntity,basicBow,playerEntity);
        UpgradeUtil.getUpgradesFromStackNEW(stack).handleModifierEvent(ArrowModifierUpgrade.EventType.ARROW_CREATE,arrowEntity,playerEntity,stack);
        /*
        In this line handleArrowCreate of the upgrades should be handled
         */
        arrowEntity.setProperties(playerEntity, playerEntity.pitch, playerEntity.yaw + yawplus, 0.0F, f * settings.getVelocityMul(), settings.getInaccuracy() + inacplus);
        if (f == 1.0F)
        {
            arrowEntity.setCritical(true);
        }

        int j = EnchantmentHelper.getLevel(Enchantments.POWER, stack);

        arrowEntity.setDamage(arrowEntity.getDamage() + settings.getDamage());

        if (j > 0)
        {
            arrowEntity.setDamage(arrowEntity.getDamage() + (double)j * 0.5D + 0.5D);
        }

        int k = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);

        if (k > 0)
        {
            arrowEntity.setPunch(k);
        }

        if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0)
        {
            arrowEntity.setOnFireFor(100);
        }

        stack.damage(1,playerEntity,(p) -> p.sendToolBreakStatus(p.getActiveHand()));

        if (flag1 || playerEntity.abilities.creativeMode && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW))
        {
            arrowEntity.pickupType = ArrowEntity.PickupPermission.CREATIVE_ONLY;
        }

        return arrowEntity;
    }



    public static float getArrowVelocity(int charge, BasicBow basicBow)
    {
        float f = (float)charge / basicBow.getSettings().getTime();
        f = (f * f + f * 2.0F) / 3.0F;

        if (f > 1.0F)
        {
            f = 1.0F;
        }

        return f;
    }
}

class CustomDataArrow extends ProjectileEntity {

    private ProjectileEntity origin;
    private ItemStack drop;

    private static Method itemStack;

    static {
        for(Method m:ProjectileEntity.class.getMethods()) {
            if(m.getParameterCount() == 0 && m.getReturnType().equals(ItemStack.class)) {
                itemStack = m;
                break;
            }
        }
    }

    public CustomDataArrow(EntityType<? extends ProjectileEntity> entityType_1, World world_1,ProjectileEntity origin) {
        super(entityType_1, world_1);
        this.origin = origin;
        try {
            this.drop = (ItemStack) itemStack.invoke(origin);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected ItemStack asItemStack() {
        return drop;
    }
}
