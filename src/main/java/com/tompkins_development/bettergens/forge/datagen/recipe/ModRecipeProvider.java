package com.tompkins_development.bettergens.forge.datagen.recipe;


import com.tompkins_development.bettergens.forge.block.ModBlocks;
import com.tompkins_development.bettergens.forge.util.Constants;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        surroundingRecipe(pWriter, ModBlocks.STIRLING_GENERATOR.get(), Items.COBBLESTONE, Items.FURNACE);

        stirlingGeneratorRecipe(pWriter, Ingredient.of(Items.COAL), 100, 1);
    }


    private void stirlingGeneratorRecipe(Consumer<FinishedRecipe> pWriter, Ingredient input, int burnTimeTicks, int feProductionPerTick) {
        new StirlingGeneratorRecipeBuilder(input, burnTimeTicks, feProductionPerTick).unlockedBy("has_" + input.getItems()[0].getItem().getName(input.getItems()[0].getItem().getDefaultInstance()), has(input.getItems()[0].getItem())).save(pWriter);
    }


    private void slabRecipe(Consumer<FinishedRecipe> pWriter, RegistryObject<Block> result, RegistryObject<Block> ingredient) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result.get(), 4)
                .pattern("###")
                .define('#', ingredient.get())
                .unlockedBy("has_" + ingredient.getId().getPath(), inventoryTrigger(ItemPredicate.Builder.item().
                        of(ingredient.get()).build()))
                .save(pWriter);
    }

    private void smallBlockRecipe(Consumer<FinishedRecipe> pWriter, RegistryObject<Block> result, ItemLike ingredient) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result.get())
                .pattern("##")
                .pattern("##")
                .unlockedBy("default_trigger", has(ingredient))
                .define('#', ingredient)
                .save(pWriter);


    }

    private void smallBlockRecipe(Consumer<FinishedRecipe> pWriter, RegistryObject<Block> result, RegistryObject<Block> ingredient) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result.get())
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_" + ingredient.getId().getPath(), inventoryTrigger(ItemPredicate.Builder.item().
                        of(ingredient.get()).build()))
                .define('#', ingredient.get())
                .save(pWriter);
    }

    private void surroundingRecipe(Consumer<FinishedRecipe> pWriter, RegistryObject<Block> result, Item surrounding, RegistryObject<Block> middle) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result.get())
                .pattern("AAA")
                .pattern("A#A")
                .pattern("AAA")
                .define('A', surrounding)
                .define('#', middle.get())
                .unlockedBy("has_" + middle.getId().getPath(), inventoryTrigger(ItemPredicate.Builder.item().
                        of(middle.get()).build()))
                .save(pWriter);
    }

    private void surroundingRecipe(Consumer<FinishedRecipe> pWriter, Block result, Item surrounding, Item middle) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result)
                .pattern("AAA")
                .pattern("A#A")
                .pattern("AAA")
                .define('A', surrounding)
                .define('#', middle)
                .unlockedBy("has_" + middle.getDefaultInstance().getDisplayName(), inventoryTrigger(ItemPredicate.Builder.item().
                        of(middle).build()))
                .save(pWriter);
    }


    private void surroundingRecipe(Consumer<FinishedRecipe> pWriter, RegistryObject<Block> result, RegistryObject<Block> surrounding, RegistryObject<Block> middle) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result.get())
                .pattern("AAA")
                .pattern("A#A")
                .pattern("AAA")
                .define('A', surrounding.get())
                .define('#', middle.get())
                .save(pWriter);
    }

    private void surroundingRecipe(Consumer<FinishedRecipe> pWriter, RegistryObject<Block> result, Block surrounding, RegistryObject<Block> middle) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result.get())
                .pattern("AAA")
                .pattern("A#A")
                .pattern("AAA")
                .define('A', surrounding)
                .define('#', middle.get())
                .save(pWriter);
    }

    private void customNineBlockStorageRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pUnpacked, ItemLike pPacked) {
        customNineBlockStorageRecipes(pFinishedRecipeConsumer, RecipeCategory.MISC, pUnpacked, RecipeCategory.MISC, pPacked, getSimpleRecipeName(pPacked), (String)null, getSimpleRecipeName(pUnpacked), (String)null);
    }

    private void customNineBlockStorageRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeCategory pUnpackedCategory, ItemLike pUnpacked, RecipeCategory pPackedCategory, ItemLike pPacked) {
        customNineBlockStorageRecipes(pFinishedRecipeConsumer, pUnpackedCategory, pUnpacked, pPackedCategory, pPacked, getSimpleRecipeName(pPacked), (String)null, getSimpleRecipeName(pUnpacked), (String)null);
    }

    private void customNineBlockStorageRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeCategory pUnpackedCategory, ItemLike pUnpacked, RecipeCategory pPackedCategory, ItemLike pPacked, String pPackedName, @Nullable String pPackedGroup, String pUnpackedName, @Nullable String pUnpackedGroup) {
        ShapelessRecipeBuilder.shapeless(pUnpackedCategory, pUnpacked, 9).requires(pPacked).group(pUnpackedGroup).unlockedBy(getHasName(pPacked), has(pPacked)).save(pFinishedRecipeConsumer, new ResourceLocation(Constants.MOD_ID, pUnpackedName));
        ShapedRecipeBuilder.shaped(pPackedCategory, pPacked).define('#', pUnpacked).pattern("###").pattern("###").pattern("###").group(pPackedGroup).unlockedBy(getHasName(pUnpacked), has(pUnpacked)).save(pFinishedRecipeConsumer, new ResourceLocation(Constants.MOD_ID, pPackedName));
    }

}
