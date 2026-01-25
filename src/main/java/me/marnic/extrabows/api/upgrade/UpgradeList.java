package me.marnic.extrabows.api.upgrade;

import me.marnic.extrabows.common.items.BasicBow;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
    private HashMap<BasicUpgrade, Object> dataMap;
    private ItemStackHandler handler;
    private BasicBow basicBow;

    public UpgradeList(ArrowMultiplierUpgrade arrowMultiplier, ArrayList<ArrowModifierUpgrade> arrowModifiers, ItemStackHandler handler, BasicBow bow) {
        this.arrowMultiplier = arrowMultiplier;
        this.arrowModifiers = arrowModifiers;
        this.dataMap = new HashMap<>();
        this.handler = handler;
        this.basicBow = bow;
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

    public BasicBow getBasicBow() {
        return basicBow;
    }

    public boolean contains(BasicUpgrade upgrade) {
        return ((arrowMultiplier != null && arrowMultiplier.equals(upgrade)) | arrowModifiers.contains(upgrade));
    }

    public void handleModifierHittingEvent(ArrowModifierUpgrade.EventType eventType, BlockPos pos, Entity entity, Level world, Player player, AbstractArrow arrow) {
        if (hasMods()) {
            switch (eventType) {
                case ENTITY_HIT:
                    if (entity != null) {
                        for (ArrowModifierUpgrade up : getArrowModifiers()) {
                            up.handleEntityHit(entity, world, player, arrow, this);
                        }
                    }
                case BLOCK_HIT:
                    if (pos != null) {
                        for (ArrowModifierUpgrade up : getArrowModifiers()) {
                            up.handleBlockHit(pos, world, player, arrow, this);
                        }
                    }
                case WATER_HIT:
                    if (pos != null) {
                        for (ArrowModifierUpgrade up : getArrowModifiers()) {
                            up.handleWaterHit(pos, world, player, arrow, this);
                        }
                    }
            }
        }
    }

    public void handleOnUpdatedEvent(AbstractArrow arrow, Level world) {
        if (hasMods()) {
            for (ArrowModifierUpgrade upgrade : getArrowModifiers()) {
                upgrade.handleFlyingEvent(arrow, world, this);
            }
        }
    }

    public void handleInsertedEvent(ItemStack bow) {
        for (ArrowModifierUpgrade modifierUpgrade : getArrowModifiers()) {
            modifierUpgrade.handleUpgradeInsert(bow);
        }
    }

    public void handleModifierEvent(ArrowModifierUpgrade.EventType eventType, AbstractArrow arrow, Player player, ItemStack bowStack) {
        if (hasMods()) {
            switch (eventType) {
                case ARROW_CREATE:
                    for (ArrowModifierUpgrade up : getArrowModifiers()) {
                        up.handleArrowCreate(arrow, player, this);
                    }

                case SET_EFFECT:
                    for (ArrowModifierUpgrade up : getArrowModifiers()) {
                        up.handleSetEffect(arrow, this);
                    }
                case ENTITY_INIT:
                    for (ArrowModifierUpgrade up : getArrowModifiers()) {
                        up.handleEntityInit(arrow, this, player);
                    }
            }
        }
    }

    public void applyDamage(Player player) {
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                ItemStack old = stack.copy();
                stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(p.getUsedItemHand()));
            }
        }
    }

    public HashMap<BasicUpgrade, Object> getDataMap() {
        return dataMap;
    }

    public void dropItems(Player player) {
        for (int i = 0; i < handler.getSlots(); i++) {
            player.drop(handler.getStackInSlot(i), false);
        }
    }
}
