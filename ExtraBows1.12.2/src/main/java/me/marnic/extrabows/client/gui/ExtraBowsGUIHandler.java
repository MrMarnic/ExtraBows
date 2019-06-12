package me.marnic.extrabows.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import me.marnic.extrabows.common.container.*;

import javax.annotation.Nullable;

/**
 * Copyright (c) 30.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ExtraBowsGUIHandler implements IGuiHandler {


    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if(ID == 0) {
            return new BowUpgradeGuiContainer(player);
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if(ID == 0) {
            return new BowUpgradeGui(new BowUpgradeGuiContainer(player),player);
        }
        return null;
    }
}
