package com.tompkins_development.bettergens.forge.ui.screen;

import com.mojang.blaze3d.systems.RenderSystem;

import com.tompkins_development.bettergens.forge.ui.menu.AbstractGeneratorMenu;
import com.tompkins_development.bettergens.forge.ui.renderer.EnergyDisplayTooltipArea;
import com.tompkins_development.bettergens.forge.util.Constants;
import com.tompkins_development.bettergens.forge.util.MouseUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.List;
import java.util.Optional;

public abstract class AbstractGeneratorScreen<T extends AbstractGeneratorMenu> extends AbstractContainerScreen<T> {

    private ResourceLocation guiTexture;
    private EnergyDisplayTooltipArea energyInfoArea;

    public int x;
    public int y;


    public AbstractGeneratorScreen(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        setTexture();
    }

    public abstract String getTextureName();

    @Override
    protected void init() {
        super.init();
        x = (width - imageWidth) / 2;
        y = (height - imageHeight) / 2;

        this.energyInfoArea = createEnergyInfoArea();
    }

    private EnergyDisplayTooltipArea createEnergyInfoArea() {
        return new EnergyDisplayTooltipArea(x + 152, y + 14, 16, 64 , menu.blockEntity.getEnergyStorage());
    }

    public void setTexture() {
        if(getTextureName() == null) {
            throw new IllegalStateException("Texture For Generator Was Never Set");
        }
        this.guiTexture = new ResourceLocation(Constants.MOD_ID, "textures/gui/generators/" + getTextureName() + ".png");
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, guiTexture);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(guiTexture, x, y, 0, 0, imageWidth, imageHeight);
        renderProgress(guiGraphics, x, y);
        energyInfoArea.render(guiGraphics);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        renderEnergyAreaTooltip(pGuiGraphics, pMouseX, pMouseY, x, y);
        renderProgressTooltip(pGuiGraphics, pMouseX, pMouseY, x, y);
        super.renderLabels(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    public void renderProgress(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isBurning()) {
            guiGraphics.blit(guiTexture, x + 70, y + 60, 176, 0, menu.getScaledProgress(), 5);
        }
    }

    public void renderEnergyAreaTooltip(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 156, 11, 8, 64)) {
            pGuiGraphics.renderTooltip(this.font, energyInfoArea.getTooltips(),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }

    public void renderProgressTooltip(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 70, 60, 36, 5)) {
            pGuiGraphics.renderTooltip(this.font, List.of(Component.translatable("bettergens.generator.progress", menu.getProgressPercent())),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }

    public boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtils.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }
}
