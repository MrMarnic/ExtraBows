package me.marnic.extrabows.api.util;

import me.marnic.extrabows.api.upgrade.ArrowModifierUpgrade;
import me.marnic.extrabows.api.upgrade.UpgradeList;
import me.marnic.extrabows.common.items.BasicBow;
import me.marnic.extrabows.common.items.CustomBowSettings;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.UUID;

/**
 * Copyright (c) 26.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ArrowUtil {

    public static HashMap<UUID, UpgradeList> ARROWS_TO_UPGRADES = new HashMap<>();

    public static EntityArrow createArrow(World worldIn, ItemStack stack, ItemStack arrowStack, EntityLivingBase shooter, BasicBow basicBow, EntityPlayer player) {

        EntityArrow arrow = ((ItemArrow) arrowStack.getItem()).createArrow(worldIn, arrowStack, shooter);
        return arrow;
    }

    private static void shootArrow(Entity shooter, float pitch, float yaw, float p_184547_4_, float velocity, float inaccuracy, EntityArrow arrow, float yawplus) {
        yaw = yaw + yawplus;
        float f = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
        float f1 = -MathHelper.sin(pitch * 0.017453292F);
        float f2 = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
        arrow.shoot((double) f, (double) f1, (double) f2, velocity, inaccuracy);
        arrow.motionX += shooter.motionX;
        arrow.motionZ += shooter.motionZ;

        if (!shooter.onGround) {
            arrow.motionY += shooter.motionY;
        }
    }

    public static EntityArrow createArrowComplete(World worldIn, ItemStack bow, ItemStack arrow, EntityPlayer entityplayer, BasicBow basicBow, float f, boolean flag1, float inacplus, float yawplus, UpgradeList list, boolean isLoaded) {
        CustomBowSettings settings = basicBow.getSettings();
        EntityArrow entityarrow = ArrowUtil.createArrow(worldIn, bow, arrow, entityplayer, basicBow, entityplayer);
        UpgradeUtil.getUpgradesFromStackNEW(bow).handleModifierEvent(ArrowModifierUpgrade.EventType.ARROW_CREATE, entityarrow, entityplayer, bow);

        shootArrow(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, f * settings.getVelocityMul(), settings.getInaccuracy() + inacplus, entityarrow, yawplus);
        if (f == 1.0F) {
            entityarrow.setIsCritical(true);
        }

        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, bow);

        entityarrow.setDamage(entityarrow.getDamage() + settings.getDamage());

        if (j > 0) {
            entityarrow.setDamage(entityarrow.getDamage() + (double) j * 0.5D + 0.5D);
        }

        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, bow);

        if (k > 0) {
            entityarrow.setKnockbackStrength(k);
        }

        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, bow) > 0) {
            entityarrow.setFire(100);
        }

        if (!isLoaded) {
            bow.damageItem(1, entityplayer);
        }

        if (flag1 || entityplayer.capabilities.isCreativeMode && (arrow.getItem() == Items.SPECTRAL_ARROW || arrow.getItem() == Items.TIPPED_ARROW)) {
            entityarrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
        }

        entityarrow.addTag("extrabows");
        if (isLoaded) {
            entityarrow.addTag("electric");
        }

        basicBow.onArrowCreate(entityarrow);

        return entityarrow;
    }


    public static float getArrowVelocity(int charge, BasicBow basicBow) {
        float f = (float) charge / basicBow.getSettings().getTime();
        f = (f * f + f * 2.0F) / 3.0F;

        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }
}
