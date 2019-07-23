package me.marnic.extrabows.api.util;

import me.marnic.extrabows.api.upgrade.ArrowModifierUpgrade;
import me.marnic.extrabows.api.upgrade.UpgradeList;
import me.marnic.extrabows.common.items.BasicBow;
import me.marnic.extrabows.common.items.CustomBowSettings;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.ProjectileImpactEvent;

import java.util.HashMap;
import java.util.UUID;

/**
 * Copyright (c) 26.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ArrowUtil {

    public static HashMap<UUID, UpgradeList> ARROWS_TO_UPGRADES = new HashMap<>();

    public static AbstractArrowEntity createArrow(World worldIn, ItemStack bowStack,ItemStack arrowStack, LivingEntity shooter, BasicBow basicBow, PlayerEntity player)
    {
        AbstractArrowEntity arrow = ((ArrowItem)arrowStack.getItem()).createArrow(worldIn,arrowStack,shooter);
        return arrow;
    }

    private static void shootArrow(Entity shooter, float pitch, float yaw, float p_184547_4_, float velocity, float inaccuracy,AbstractArrowEntity arrow,float yawplus) {
        yaw=yaw+yawplus;
        float f = -MathHelper.sin(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F));
        float f1 = -MathHelper.sin(pitch * ((float)Math.PI / 180F));
        float f2 = MathHelper.cos(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F));
        arrow.shoot((double)f, (double)f1, (double)f2, velocity, inaccuracy);
        arrow.setMotion(arrow.getMotion().add(shooter.getMotion().x, shooter.onGround ? 0.0D : shooter.getMotion().y, shooter.getMotion().z));
    }

    public static AbstractArrowEntity createArrowComplete(World worldIn, ItemStack bow, PlayerEntity playerEntity, BasicBow basicBow, float f, ItemStack arrow, boolean flag1, float inacplus, float yawplus, UpgradeList list) {
        CustomBowSettings settings = basicBow.getSettings();
        AbstractArrowEntity arrowEntity = ArrowUtil.createArrow(worldIn, bow,arrow, playerEntity,basicBow,playerEntity);
        UpgradeUtil.getUpgradesFromStackNEW(bow).handleModifierEvent(ArrowModifierUpgrade.EventType.ARROW_CREATE,arrowEntity,playerEntity,bow);
        /*
        In this line handleArrowCreate of the upgrades should be handled
         */

        shootArrow(playerEntity, playerEntity.rotationPitch, playerEntity.rotationYaw, 0.0F, f * settings.getVelocityMul(), settings.getInaccuracy() + inacplus,arrowEntity,yawplus);
        if (f == 1.0F)
        {
            arrowEntity.setIsCritical(true);
        }

        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, bow);

        arrowEntity.setDamage(arrowEntity.getDamage() + settings.getDamage());

        if (j > 0)
        {
            arrowEntity.setDamage(arrowEntity.getDamage() + (double)j * 0.5D + 0.5D);
        }

        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, bow);

        if (k > 0)
        {
            arrowEntity.setKnockbackStrength(k);
        }

        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, bow) > 0)
        {
            arrowEntity.setFire(100);
        }

        bow.damageItem(1,playerEntity,(p) -> p.sendBreakAnimation(p.getActiveHand()));

        if (flag1 || playerEntity.abilities.isCreativeMode && (arrow.getItem() == Items.SPECTRAL_ARROW || arrow.getItem() == Items.TIPPED_ARROW))
        {
            arrowEntity.pickupStatus = ArrowEntity.PickupStatus.CREATIVE_ONLY;
        }

        return arrowEntity;
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
