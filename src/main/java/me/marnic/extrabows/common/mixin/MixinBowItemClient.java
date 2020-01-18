package me.marnic.extrabows.common.mixin;

import me.marnic.extrabows.api.upgrade.BasicUpgrade;
import me.marnic.extrabows.api.upgrade.UpgradeList;
import me.marnic.extrabows.api.util.UpgradeUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

/**
 * Copyright (c) 04.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
@Mixin(BowItem.class)
public abstract class MixinBowItemClient extends Item {

    public MixinBowItemClient(Settings item$Settings_1) {
        super(item$Settings_1);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, World world_1, List<Text> tooltip, TooltipContext tooltipContext_1) {
        if(stack.getItem().getClass().equals(BowItem.class)) {
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
    }
}
