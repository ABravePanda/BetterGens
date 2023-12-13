package com.tompkins_development.bettergens.forge.datagen.recipe;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractGeneratorRecipeBuilder implements RecipeBuilder {

    public Ingredient input;
    public int burnTimeTicks;
    public int feProductionPerTick;
    public final Advancement.Builder advancement = Advancement.Builder.advancement();

    public AbstractGeneratorRecipeBuilder(Ingredient input, int burnTimeTicks, int feProductionPerTick) {
        this.input = input;
        this.burnTimeTicks = burnTimeTicks;
        this.feProductionPerTick = feProductionPerTick;
    }

    @Override
    public RecipeBuilder unlockedBy(String pCriterionName, CriterionTriggerInstance pCriterionTrigger) {
        this.advancement.addCriterion(pCriterionName, pCriterionTrigger);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String pGroupName) {
        return this;
    }

    @Override
    public Item getResult() {
        return Items.DIAMOND;
    }


}
