package com.tompkins_development.bettergens.forge.recipe;

import com.google.gson.JsonObject;
import com.tompkins_development.bettergens.forge.util.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class StirlingGeneratorRecipe extends AbstractGeneratorRecipe {

    public StirlingGeneratorRecipe(ResourceLocation id, Ingredient input, int burnTimeTicks, int feProductionPerTick) {
        super(id, input, burnTimeTicks, feProductionPerTick);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<AbstractGeneratorRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "stirling_generator";
    }

    public static class Serializer implements RecipeSerializer<AbstractGeneratorRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Constants.MOD_ID,"stirling_generator");

        @Override
        public AbstractGeneratorRecipe fromJson(ResourceLocation id, JsonObject json) {

            int feProductionPerTick = json.get(FE_PRODUCTION_PER_TICK_JSON).getAsInt();
            int burnTimeTicks = json.get(BURN_TIME_TICKS_JSON).getAsInt();
            Ingredient input = Ingredient.fromJson(json.get("input"));

            return new StirlingGeneratorRecipe(id, input, burnTimeTicks, feProductionPerTick);
        }

        @Override
        public AbstractGeneratorRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {

            int feProductionPerTick = buf.readInt();
            int burnTimeTicks = buf.readInt();
            Ingredient input = Ingredient.fromNetwork(buf);

            return new StirlingGeneratorRecipe(id, input, burnTimeTicks, feProductionPerTick);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, AbstractGeneratorRecipe recipe) {
            buf.writeInt(recipe.getFeProductionPerTick());
            buf.writeInt(recipe.getBurnTimeTicks());
            recipe.getInput().toNetwork(buf);

        }
    }
}
