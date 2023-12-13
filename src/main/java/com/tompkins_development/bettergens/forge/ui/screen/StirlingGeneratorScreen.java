package com.tompkins_development.bettergens.forge.ui.screen;

import com.tompkins_development.bettergens.forge.ui.menu.StirlingGeneratorMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class StirlingGeneratorScreen extends AbstractGeneratorScreen<StirlingGeneratorMenu> {

    public StirlingGeneratorScreen(StirlingGeneratorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public String getTextureName() {
        return "stirling_generator";
    }
}
