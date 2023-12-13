package com.tompkins_development.bettergens.forge.recipe;

import com.tompkins_development.bettergens.forge.util.Constants;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Constants.MOD_ID);

    public static final RegistryObject<RecipeSerializer<AbstractGeneratorRecipe>> STIRLING_SERIALIZER =
            SERIALIZERS.register("stirling_generator", () -> StirlingGeneratorRecipe.Serializer.INSTANCE);


    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
