package com.tompkins_development.bettergens.forge.datagen.recipe;

import com.google.gson.JsonObject;
import com.tompkins_development.bettergens.forge.recipe.AbstractGeneratorRecipe;
import com.tompkins_development.bettergens.forge.recipe.StirlingGeneratorRecipe;
import com.tompkins_development.bettergens.forge.util.Constants;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

public class StirlingGeneratorRecipeBuilder extends AbstractGeneratorRecipeBuilder{

    public StirlingGeneratorRecipeBuilder(Ingredient input, int burnTimeTicks, int feProductionPerTick) {
        super(input, burnTimeTicks, feProductionPerTick);
    }

    @Override
    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        this.advancement.parent(new ResourceLocation("recipes/root"))
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pRecipeId))
                .rewards(AdvancementRewards.Builder.recipe(pRecipeId)).requirements(RequirementsStrategy.OR);

        pFinishedRecipeConsumer.accept(new Result(pRecipeId, this.input, this.burnTimeTicks, this.feProductionPerTick, this.advancement, new ResourceLocation(pRecipeId.getNamespace(), "recipes/" + pRecipeId.getPath())));

    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;

        private Ingredient input;
        private int burnTimeTicks;
        private int feProductionPerTick;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;


        public Result(ResourceLocation id, Ingredient input, int burnTimeTicks, int feProductionPerTick, Advancement.Builder advancement, ResourceLocation advancementId) {
            this.id = id;
            this.input = input;
            this.burnTimeTicks = burnTimeTicks;
            this.feProductionPerTick = feProductionPerTick;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(JsonObject pJson) {
            pJson.addProperty(AbstractGeneratorRecipe.BURN_TIME_TICKS_JSON, burnTimeTicks);
            pJson.addProperty(AbstractGeneratorRecipe.FE_PRODUCTION_PER_TICK_JSON, feProductionPerTick);
            pJson.add("input", input.toJson());
        }

        @Override
        public ResourceLocation getId() {
            return new ResourceLocation(Constants.MOD_ID,
                    ForgeRegistries.ITEMS.getKey(this.input.getItems()[0].getItem()).getPath() + "_stirling_generator");
        }

        @Override
        public RecipeSerializer<?> getType() {
            return StirlingGeneratorRecipe.Serializer.INSTANCE;
        }


        @javax.annotation.Nullable
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @javax.annotation.Nullable
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
