package me.marnic.extrabows.api.util;

import me.marnic.extrabows.api.upgrade.ArrowModifierUpgrade;
import me.marnic.extrabows.api.upgrade.UpgradeList;
import me.marnic.extrabows.common.items.BasicBow;
import me.marnic.extrabows.common.items.bows.BowSettings;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * Copyright (c) 26.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ArrowUtil {
    public static ArrowEntity createArrow(World worldIn, ItemStack stack, LivingEntity shooter, BasicBow basicBow, PlayerEntity player)
    {
        ArrowEntity entitytippedarrow = new ArrowEntity(worldIn,shooter) {

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

        entitytippedarrow.initFromStack(stack);
        UpgradeUtil.getUpgradesFromStackNEW(stack).handleModifierEvent(ArrowModifierUpgrade.EventType.SET_EFFECT,entitytippedarrow,player,stack);
        return entitytippedarrow;
    }

    private static void shootArrow(Entity shooter, float pitch, float yaw, float p_184547_4_, float velocity, float inaccuracy, ArrowEntity arrow, float yawplus) {
        yaw=yaw+yawplus;
        float f = -MathHelper.sin(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F));
        float f1 = -MathHelper.sin(pitch * ((float)Math.PI / 180F));
        float f2 = MathHelper.cos(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F));
        arrow.setVelocity((double)f, (double)f1, (double)f2, velocity, inaccuracy);
        arrow.setVelocity(arrow.getVelocity().add(shooter.getVelocity().x, shooter.onGround ? 0.0D : shooter.getVelocity().y, shooter.getVelocity().z));
    }

    public static ArrowEntity createArrowComplete(World worldIn, ItemStack itemstack, PlayerEntity playerEntity, BasicBow basicBow, float f, ItemStack stack, boolean flag1, float inacplus, float yawplus, UpgradeList list) {
        BowSettings settings = basicBow.getSettings();
        ArrowEntity arrowEntity = ArrowUtil.createArrow(worldIn, stack, playerEntity,basicBow,playerEntity);
        UpgradeUtil.getUpgradesFromStackNEW(stack).handleModifierEvent(ArrowModifierUpgrade.EventType.ARROW_CREATE,arrowEntity,playerEntity,stack);
        /*
        In this line handleArrowCreate of the upgrades should be handled
         */

        shootArrow(playerEntity, playerEntity.pitch, playerEntity.yaw, 0.0F, f * settings.getVelocityMul(), settings.getInaccuracy() + inacplus,arrowEntity,yawplus);
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
            arrowEntity.method_7449(k);
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
