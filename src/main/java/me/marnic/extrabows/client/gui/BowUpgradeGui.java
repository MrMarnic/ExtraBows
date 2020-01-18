package me.marnic.extrabows.client.gui;

import me.marnic.extrabows.api.util.IdentificationUtil;
import me.marnic.extrabows.common.container.BowUpgradeContainer;
import me.marnic.extrabows.common.main.Identification;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.client.gui.screen.ingame.FurnaceScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

/**
 * Copyright (c) 30.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BowUpgradeGui extends AbstractContainerScreen<BowUpgradeContainer> {

    private static Identifier TEXTURE;
    private String name;
    private PlayerEntity player;

    public BowUpgradeGui(int syncId, PlayerEntity playerEntity, PacketByteBuf buf) {
        super(new BowUpgradeContainer(syncId,playerEntity.inventory,buf),playerEntity.inventory,new LiteralText("Bow Upgrade Inventory"));
        TEXTURE = IdentificationUtil.fromString("textures/gui/bow_upgrade_gui.png");
        name = "Bow Upgrade Inventory";
        this.player = playerEntity;
    }

    @Override
    protected void drawBackground(float partialTicks, int mouseX, int mouseY) {
        minecraft.getTextureManager().bindTexture(TEXTURE);
        blit(x,y,0,0,containerWidth,containerHeight);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.drawMouseoverTooltip(mouseX, mouseY);
    }

    @Override
    protected void drawForeground(int mouseX, int mouseY) {
        this.font.draw(name, this.containerWidth / 2 - this.font.getStringWidth(name) / 2, 6, 4210752);
        this.font.draw(player.inventory.getDisplayName().asString(), 8, this.containerHeight - 96 + 2, 4210752);
    }
}
