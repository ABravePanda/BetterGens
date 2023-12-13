package com.tompkins_development.bettergens.forge.compat;

import com.tompkins_development.bettergens.forge.recipe.StirlingGeneratorRecipe;
import com.tompkins_development.bettergens.forge.util.Constants;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

@JeiPlugin
public class JEIBetterGens implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Constants.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new StirlingGeneratorRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        registration.addRecipes(StirlingGeneratorRecipeCategory.STIRLING_GENERATOR_RECIPE_TYPE, recipeManager.getAllRecipesFor(StirlingGeneratorRecipe.Type.INSTANCE));
    }


}