package com.tompkins_development.bettergens.forge.recipe;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

public abstract class AbstractGeneratorRecipe implements Recipe<SimpleContainer> {

    public static String BURN_TIME_TICKS_JSON = "burn_time_in_ticks";
    public static String FE_PRODUCTION_PER_TICK_JSON = "fe_production_per_tick";

    private ResourceLocation id;
    private Ingredient input;
    private int burnTimeTicks;
    private int feProductionPerTick;


    public AbstractGeneratorRecipe(ResourceLocation id, Ingredient input, int burnTimeTicks, int feProductionPerTick) {
        this.id = id;
        this.input = input;
        this.burnTimeTicks = burnTimeTicks;
        this.feProductionPerTick = feProductionPerTick;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if(pLevel.isClientSide) return false;

        boolean correctInput = input.test(pContainer.getItem(0));
        return correctInput;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return new ItemStack(Items.STONE);
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return new ItemStack(Items.STONE);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }


    public Ingredient getInput() {
        return input;
    }

    public int getBurnTimeTicks() {
        return burnTimeTicks;
    }

    public int getFeProductionPerTick() {
        return feProductionPerTick;
    }


}