package com.tompkins_development.bettergens.forge.compat;


import com.tompkins_development.bettergens.forge.recipe.AbstractGeneratorRecipe;
import com.tompkins_development.bettergens.forge.recipe.StirlingGeneratorRecipe;
import com.tompkins_development.bettergens.forge.util.Constants;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class StirlingGeneratorRecipeCategory extends AbstractGeneratorRecipeCategory {

    public static final ResourceLocation UID = new ResourceLocation(Constants.MOD_ID, "stirling_generator");
    public static final RecipeType<AbstractGeneratorRecipe> STIRLING_GENERATOR_RECIPE_TYPE = new RecipeType<>(UID, StirlingGeneratorRecipe.class);

    public StirlingGeneratorRecipeCategory(IGuiHelper helper) {
        super(helper);
    }

    @Override
    public RecipeType<AbstractGeneratorRecipe> getRecipeType() {
        return STIRLING_GENERATOR_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Stirling Generator");
    }
}
