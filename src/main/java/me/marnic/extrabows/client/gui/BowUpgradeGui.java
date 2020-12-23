package me.marnic.extrabows.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import me.marnic.extrabows.common.container.BowUpgradeGuiContainer;
import me.marnic.extrabows.common.main.Identification;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;

/**
 * Copyright (c) 30.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BowUpgradeGui extends ContainerScreen<BowUpgradeGuiContainer> implements IHasContainer<BowUpgradeGuiContainer> {

    private static ResourceLocation TEXTURE;
    private String name;
    private PlayerEntity player;

    public BowUpgradeGui(BowUpgradeGuiContainer inventorySlotsIn, PlayerInventory inventory, ITextComponent component) {
        super(inventorySlotsIn, inventory, new StringTextComponent("Bow Upgrade Inventory"));
        TEXTURE = new ResourceLocation(Identification.MODID, "textures/gui/bow_upgrade_gui.png");
        name = "Bow Upgrade Inventory";
        this.player = inventory.player;
    }

    //RENDER
    @Override
    public void func_230430_a_(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        this.func_238651_a_(p_230430_1_,0); //BACKGROUND
        super.func_230430_a_(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
        this.func_230459_a_(p_230430_1_, p_230430_2_, p_230430_3_);
    }

    //drawGuiContainerBackgroundLayer
    @Override
    protected void func_230450_a_(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        this.field_230706_i_.getTextureManager().bindTexture(TEXTURE);
        this.func_238474_b_(p_230450_1_,guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}
