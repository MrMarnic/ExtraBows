package me.marnic.extrabows.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import me.marnic.extrabows.common.container.BowUpgradeGuiContainer;
import me.marnic.extrabows.common.main.Identification;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

/**
 * Copyright (c) 30.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

public class BowUpgradeGui extends AbstractContainerScreen<BowUpgradeGuiContainer> {

    private static ResourceLocation TEXTURE;
    private String name;
    private Player player;

    public BowUpgradeGui(BowUpgradeGuiContainer inventorySlotsIn, Inventory inventory, Component component) {
        super(inventorySlotsIn, inventory, Component.literal("Bow Upgrade Inventory"));
        TEXTURE = ResourceLocation.fromNamespaceAndPath(Identification.MODID, "textures/gui/bow_upgrade_gui.png");
        name = "Bow Upgrade Inventory";
        this.player = inventory.player;
    }

    //RENDER
    @Override
    public void render(GuiGraphics p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        this.renderBackground(p_230430_1_); //BACKGROUND
        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
        this.renderTooltip(p_230430_1_,p_230430_2_,p_230430_3_);
    }

    //drawGuiContainerBackgroundLayer
    @Override
    protected void renderBg(GuiGraphics p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f,1.0f,1.0f,1.0f);
        RenderSystem.setShaderTexture(0,TEXTURE);
        p_230450_1_.blit(TEXTURE,getGuiLeft(), getGuiTop(), 0, 0, getXSize(), getYSize());
    }
}
