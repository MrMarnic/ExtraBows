package me.marnic.extrabows.mod.gui;

import me.marnic.extrabows.mod.main.Identification;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

/**
 * Copyright (c) 30.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BowUpgradeGui extends GuiContainer {

    private static ResourceLocation TEXTURE;
    private String name;
    private EntityPlayer player;

    public BowUpgradeGui(Container inventorySlotsIn,EntityPlayer player) {
        super(inventorySlotsIn);
        TEXTURE = new ResourceLocation(Identification.MODID,"textures/gui/bow_upgrade_gui.png");
        name = "Bow Upgrade Inventory";
        this.player = player;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft,guiTop,0,0,xSize,ySize);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 6, 4210752);
        this.fontRenderer.drawString(player.inventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }
}
