package me.marnic.extrabows.api.util;

import me.marnic.extrabows.api.upgrade.ArrowModifierUpgrade;
import me.marnic.extrabows.api.upgrade.UpgradeList;
import me.marnic.extrabows.mod.items.BasicBow;
import me.marnic.extrabows.mod.items.CustomBowSettings;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Copyright (c) 26.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ArrowUtil {
    public static EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter, BasicBow basicBow,EntityPlayer player)
    {
        EntityTippedArrow entitytippedarrow = new EntityTippedArrow(worldIn, shooter) {

            private UpgradeList list;
            private boolean alreadyHit = false;

            @Override
            protected void onHit(RayTraceResult raytraceResultIn) {
                super.onHit(raytraceResultIn);
                if(raytraceResultIn.getBlockPos() != null) {
                    if(!alreadyHit) {
                        alreadyHit = true;
                        list.handleModifierHittingEvent(ArrowModifierUpgrade.EventType.BLOCK_HIT,raytraceResultIn.getBlockPos(),null,world,player,this);
                    }
                }else if(raytraceResultIn.entityHit != null){
                    if(!alreadyHit) {
                        alreadyHit = true;
                        list.handleModifierHittingEvent(ArrowModifierUpgrade.EventType.ENTITY_HIT,null,raytraceResultIn.entityHit,world,player,this);
                    }
                }
            }

            boolean prevWater;

            @Override
            protected void entityInit() {
                super.entityInit();
                list = UpgradeUtil.getUpgradesFromStackNEW(stack);
                list.handleModifierEvent(ArrowModifierUpgrade.EventType.ENTITY_INIT,this,player,stack);
            }

            @Override
            public void onEntityUpdate() {
                super.onEntityUpdate();
                if(inWater) {
                    if(!prevWater) {
                        list.handleModifierHittingEvent(ArrowModifierUpgrade.EventType.WATER_HIT,getPosition(),null,world,player,this);
                    }
                }

                if(!alreadyHit) {
                    list.handleOnUpdatedEvent(this,world);
                }
                prevWater = inWater;
            }
        };

        entitytippedarrow.setPotionEffect(stack);
        if(basicBow.getSettings().hasEffect()) {
            entitytippedarrow.addEffect(basicBow.getSettings().getEffect());
        }
        UpgradeUtil.getUpgradesFromStackNEW(stack).handleModifierEvent(ArrowModifierUpgrade.EventType.SET_EFFECT,entitytippedarrow,player,stack);
        return entitytippedarrow;
    }

    private static void shootArrow(Entity shooter, float pitch, float yaw, float p_184547_4_, float velocity, float inaccuracy,EntityArrow arrow,float yawplus) {
        yaw=yaw+yawplus;
        float f = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
        float f1 = -MathHelper.sin(pitch * 0.017453292F);
        float f2 = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
        arrow.shoot((double)f, (double)f1, (double)f2, velocity, inaccuracy);
        arrow.motionX += shooter.motionX;
        arrow.motionZ += shooter.motionZ;

        if (!shooter.onGround)
        {
            arrow.motionY += shooter.motionY;
        }
    }

    public static EntityArrow createArrowComplete(World worldIn, ItemStack itemstack, EntityPlayer entityplayer, BasicBow basicBow, float f, ItemStack stack, boolean flag1, float inacplus, float yawplus, UpgradeList list) {
        CustomBowSettings settings = basicBow.getSettings();
        EntityArrow entityarrow = ArrowUtil.createArrow(worldIn, stack, entityplayer,basicBow,entityplayer);
        entityarrow = basicBow.customizeArrow(entityarrow);
        UpgradeUtil.getUpgradesFromStackNEW(stack).handleModifierEvent(ArrowModifierUpgrade.EventType.ARROW_CREATE,entityarrow,entityplayer,stack);
        /*
        In this line handleArrowCreate of the upgrades should be handled
         */

        shootArrow(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, f * settings.getVelocityMul(), settings.getInaccuracy() + inacplus,entityarrow,yawplus);
        if (f == 1.0F)
        {
            entityarrow.setIsCritical(true);
        }

        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

        entityarrow.setDamage(entityarrow.getDamage() + settings.getDamage());

        if (j > 0)
        {
            entityarrow.setDamage(entityarrow.getDamage() + (double)j * 0.5D + 0.5D);
        }

        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

        if (k > 0)
        {
            entityarrow.setKnockbackStrength(k);
        }

        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0)
        {
            entityarrow.setFire(100);
        }

        stack.damageItem(1, entityplayer);

        if (flag1 || entityplayer.capabilities.isCreativeMode && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW))
        {
            entityarrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
        }

        return entityarrow;
    }



    public static float getArrowVelocity(int charge,BasicBow basicBow)
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
