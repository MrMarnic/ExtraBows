package me.marnic.extrabows.client.gui;

import me.marnic.extrabows.common.container.BowUpgradeGuiContainer;
import me.marnic.extrabows.common.main.Identification;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

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
        super(inventorySlotsIn,inventory,new StringTextComponent("Bow Upgrade Inventory"));
        TEXTURE = new ResourceLocation(Identification.MODID,"textures/gui/bow_upgrade_gui.png");
        name = "Bow Upgrade Inventory";
        this.player = inventory.player;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        minecraft.getTextureManager().bindTexture(TEXTURE);
        blit(guiLeft,guiTop,0,0,xSize,ySize);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString(name, this.xSize / 2 - this.font.getStringWidth(name) / 2, 6, 4210752);
        this.font.drawString(player.inventory.getDisplayName().getUnformattedComponentText(), 8, this.ySize - 96 + 2, 4210752);
    }
}
