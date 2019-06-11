package me.marnic.extrabows.api.upgrade;

import me.marnic.extrabows.api.util.UpgradeUtil;
import me.marnic.extrabows.mod.packet.ExtraBowsPacketHandler;
import me.marnic.extrabows.mod.packet.PacketSendDestroyMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Copyright (c) 28.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class UpgradeList {
    private ArrowMultiplierUpgrade arrowMultiplier;
    private ArrayList<ArrowModifierUpgrade> arrowModifiers;
    private HashMap<BasicUpgrade,Object> dataMap;
    private ItemStackHandler handler;

    public UpgradeList(ArrowMultiplierUpgrade arrowMultiplier, ArrayList<ArrowModifierUpgrade> arrowModifiers,ItemStackHandler handler) {
        this.arrowMultiplier = arrowMultiplier;
        this.arrowModifiers = arrowModifiers;
        this.dataMap = new HashMap<>();
        this.handler = handler;
    }

    public ArrayList<ArrowModifierUpgrade> getArrowModifiers() {
        return arrowModifiers;
    }

    public ArrowMultiplierUpgrade getArrowMultiplier() {
        return arrowMultiplier;
    }

    public boolean hasMul() {
        return arrowMultiplier != null;
    }

    public boolean hasMods() {
        return !arrowModifiers.isEmpty();
    }

    public boolean contains(BasicUpgrade upgrade) {
        return (arrowMultiplier.equals(upgrade)| arrowModifiers.contains(upgrade));
    }

    public void handleModifierHittingEvent(ArrowModifierUpgrade.EventType eventType, BlockPos pos, Entity entity, World world, EntityPlayer player,EntityArrow arrow) {
        if(hasMods()) {
            switch (eventType) {
                case ENTITY_HIT:
                    if(entity != null) {
                        for(ArrowModifierUpgrade up:getArrowModifiers()) {
                            up.handleEntityHit(entity,world,player,arrow,this);
                        }
                    }
                case BLOCK_HIT:
                    if(pos != null) {
                        for(ArrowModifierUpgrade up:getArrowModifiers()) {
                            up.handleBlockHit(pos,world,player,arrow,this);
                        }
                    }
                case WATER_HIT:
                    if(pos != null) {
                        for(ArrowModifierUpgrade up:getArrowModifiers()) {
                            up.handleWaterHit(pos,world,player,arrow,this);
                        }
                    }
            }
        }
    }

    public void handleOnUpdatedEvent(EntityArrow arrow,World world) {
        if(hasMods()) {
            for(ArrowModifierUpgrade upgrade:getArrowModifiers()) {
                upgrade.handleFlyingEvent(arrow,world,this);
            }
        }
    }

    public void handleModifierEvent(ArrowModifierUpgrade.EventType eventType, EntityArrow arrow, EntityPlayer player, ItemStack bowStack) {
        if(hasMods()) {
            switch (eventType) {
                case ARROW_CREATE:
                    for(ArrowModifierUpgrade up:getArrowModifiers()) {
                        up.handleArrowCreate(arrow,player,this);
                    }

                case SET_EFFECT:
                    for(ArrowModifierUpgrade up:getArrowModifiers()) {
                        up.handleSetEffect(arrow,this);
                    }
                case ENTITY_INIT:
                    for(ArrowModifierUpgrade up:getArrowModifiers()) {
                        up.handleEntityInit(arrow,this,player);
                    }
            }
        }
    }

    public void applyDamage(EntityPlayer player) {
        for(int i = 0;i<handler.getSlots();i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if(!stack.isEmpty()) {
                ItemStack old = stack.copy();
                stack.damageItem(1,player);
                if(stack.isEmpty()) {
                    ExtraBowsPacketHandler.INSTANCE.sendTo(new PacketSendDestroyMessage().setText(old.getDisplayName()),(EntityPlayerMP)player);
                }
            }
        }
    }

    public HashMap<BasicUpgrade, Object> getDataMap() {
        return dataMap;
    }

    public void dropItems(EntityPlayer player) {
        for(int i = 0;i<handler.getSlots();i++) {
            player.dropItem(handler.getStackInSlot(i),false);
        }
    }
}
