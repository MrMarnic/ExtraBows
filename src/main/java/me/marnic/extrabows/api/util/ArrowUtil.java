package me.marnic.extrabows.api.util;

import me.marnic.extrabows.api.upgrade.ArrowModifierUpgrade;
import me.marnic.extrabows.api.upgrade.UpgradeList;
import me.marnic.extrabows.common.items.BasicBow;
import me.marnic.extrabows.common.items.CustomBowSettings;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.UUID;

/**
 * Copyright (c) 26.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ArrowUtil {

    public static HashMap<UUID, UpgradeList> ARROWS_TO_UPGRADES = new HashMap<>();

    public static AbstractArrow createArrow(Level worldIn, ItemStack stack, ItemStack arrowStack, LivingEntity shooter, BasicBow basicBow, Player player) {

        AbstractArrow arrow = ((ArrowItem) arrowStack.getItem()).createArrow(worldIn, arrowStack, shooter);
        return arrow;
    }

    private static void shootArrow(Entity shooter, float pitch, float yaw, float p_184547_4_, float velocity, float inaccuracy, AbstractArrow arrow, float yawplus) {
        yaw = yaw + yawplus;
        float f = -Mth.sin(yaw * ((float) Math.PI / 180F)) * Mth.cos(pitch * ((float) Math.PI / 180F));
        float f1 = -Mth.sin(pitch * ((float) Math.PI / 180F));
        float f2 = Mth.cos(yaw * ((float) Math.PI / 180F)) * Mth.cos(pitch * ((float) Math.PI / 180F));
        arrow.shoot((double) f, (double) f1, (double) f2, velocity, inaccuracy);

        arrow.setDeltaMovement(arrow.getDeltaMovement().add(shooter.getDeltaMovement().x, shooter.isOnGround() ? 0.0D : shooter.getDeltaMovement().y, shooter.getDeltaMovement().z));
    }

    public static AbstractArrow createArrowComplete(Level worldIn, ItemStack bow, ItemStack arrow, Player entityplayer, BasicBow basicBow, float f, boolean flag1, float inacplus, float yawplus, UpgradeList list, boolean isLoaded) {
        CustomBowSettings settings = basicBow.getSettings();
        AbstractArrow entityarrow = ArrowUtil.createArrow(worldIn, bow, arrow, entityplayer, basicBow, entityplayer);
        UpgradeUtil.getUpgradesFromStack(bow).handleModifierEvent(ArrowModifierUpgrade.EventType.ARROW_CREATE, entityarrow, entityplayer, bow);

        shootArrow(entityplayer, entityplayer.getXRot(), entityplayer.getYRot(), 0.0F, f * settings.getVelocityMul(), settings.getInaccuracy() + inacplus, entityarrow, yawplus);
        if (f == 1.0F) {
            entityarrow.setCritArrow(true);
        }

        int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, bow);

        entityarrow.setBaseDamage(entityarrow.getBaseDamage() + settings.getDamage());

        if (j > 0) {
            entityarrow.setBaseDamage(entityarrow.getBaseDamage() + (double) j * 0.5D + 0.5D);
        }

        int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, bow);

        if (k > 0) {
            entityarrow.setKnockback(k);
        }

        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, bow) > 0) {
            entityarrow.setSecondsOnFire(100);
        }

        if (!isLoaded) {
            bow.hurtAndBreak(1, entityplayer, (p) -> p.broadcastBreakEvent(p.getUsedItemHand()));
        }

        if (flag1 || entityplayer.getAbilities().instabuild && (arrow.getItem() == Items.SPECTRAL_ARROW || arrow.getItem() == Items.TIPPED_ARROW)) {
            entityarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
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
