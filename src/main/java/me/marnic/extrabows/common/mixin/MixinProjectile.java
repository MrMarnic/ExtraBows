package me.marnic.extrabows.common.mixin;

import me.marnic.extrabows.api.upgrade.ArrowModifierUpgrade;
import me.marnic.extrabows.api.upgrade.UpgradeList;
import me.marnic.extrabows.api.util.UpgradeUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Copyright (c) 21.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

@Mixin(ProjectileEntity.class)
public abstract class MixinProjectile extends Entity {


    public MixinProjectile(EntityType<?> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    @Shadow
    public Entity getOwner() {
        return null;
    }

    @Inject(method = "onHit",at = @At("RETURN"))
    public void onHit(HitResult hitResult_1, CallbackInfo info) {
        if(list != null) {
            if(!world.isClient) {
                if(hitResult_1.getType() == HitResult.Type.BLOCK) {
                    if(!alreadyHit) {
                        alreadyHit = true;
                        list.handleModifierHittingEvent(ArrowModifierUpgrade.EventType.BLOCK_HIT,new BlockPos(hitResult_1.getPos()),null,world,getPlayer(),getThis());
                    }
                }
            }
        }
    }

    @Inject(method = "onEntityHit",at = @At("RETURN"))
    public void onEntityHit(EntityHitResult result,CallbackInfo info) {
        if(list != null) {
            list.handleModifierHittingEvent(ArrowModifierUpgrade.EventType.ENTITY_HIT,null,result.getEntity(),world,getPlayer(),getThis());
        }
    }

    @Dynamic
    public UpgradeList list;
    @Dynamic
    public boolean prevWater;
    @Dynamic
    public boolean alreadyHit;
    @Dynamic
    public ItemStack stack;

    @Inject(method = "setOwner",at = @At("RETURN"))
    public void setOwner(Entity entity_1,CallbackInfo info) {
        if(entity_1 instanceof PlayerEntity) {
            stack = ((PlayerEntity)entity_1).getMainHandStack();
            list = UpgradeUtil.getUpgradesFromStackNEW(stack);
            list.handleModifierEvent(ArrowModifierUpgrade.EventType.ENTITY_INIT,getThis(),(PlayerEntity)entity_1,stack);
        }
    }

    @Inject(method = "tick",at = @At("RETURN"))
    public void tick(CallbackInfo info) {
        if(list != null) {
            if(inWater) {
                if(!prevWater) {
                    list.handleModifierHittingEvent(ArrowModifierUpgrade.EventType.WATER_HIT,getBlockPos(),null,world,getPlayer(),getThis());
                }
            }

            if(!alreadyHit) {
                list.handleOnUpdatedEvent(getThis(),world);
            }
            prevWater = inWater;
        }
    }

    public PlayerEntity getPlayer() {
        return (PlayerEntity)getOwner();
    }

    public ProjectileEntity getThis() {
        return ((ProjectileEntity)(Object)MixinProjectile.this);
    }
}
