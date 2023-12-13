package com.tompkins_development.bettergens.forge.compat;


import com.tompkins_development.bettergens.forge.block.ModBlocks;
import com.tompkins_development.bettergens.forge.recipe.AbstractGeneratorRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractGeneratorRecipeCategory implements IRecipeCategory<AbstractGeneratorRecipe> {

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable slot;

    public AbstractGeneratorRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(150, 18);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.STIRLING_GENERATOR.get()));
        slot = helper.getSlotDrawable();
    }


    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }


    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AbstractGeneratorRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getInput());

    }

    @Override
    public void draw(AbstractGeneratorRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        slot.draw(guiGraphics);
        String feTick = Component.translatable("bettergens.jei.category.generator.feProduction", recipe.getFeProductionPerTick()).getString();
        String ticks = Component.translatable("bettergens.jei.category.generator.burnTicks", recipe.getBurnTimeTicks()).getString();

        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        guiGraphics.drawString(font, ticks, 21, 0, 0xFF808080, false);
        guiGraphics.drawString(font, feTick, 21, 11, 0xFF808080, false);
    }
}
