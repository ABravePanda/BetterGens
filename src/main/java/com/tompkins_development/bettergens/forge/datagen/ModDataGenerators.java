package com.tompkins_development.bettergens.forge.datagen;

import com.tompkins_development.bettergens.forge.datagen.loot.ModLootTableProvider;
import com.tompkins_development.bettergens.forge.datagen.providers.ModBlockModelProvider;
import com.tompkins_development.bettergens.forge.datagen.providers.ModBlockStateProvider;
import com.tompkins_development.bettergens.forge.datagen.providers.ModItemModelProvider;
import com.tompkins_development.bettergens.forge.datagen.recipe.ModRecipeProvider;
import com.tompkins_development.bettergens.forge.datagen.tag.ModBlockTagGenerator;
import com.tompkins_development.bettergens.forge.datagen.tag.ModFluidTagGenerator;
import com.tompkins_development.bettergens.forge.datagen.tag.ModItemTagGenerator;
import com.tompkins_development.bettergens.forge.util.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new ModRecipeProvider(packOutput));
        generator.addProvider(event.includeServer(), ModLootTableProvider.create(packOutput));
        ModBlockTagGenerator blockTagGenerator = new ModBlockTagGenerator(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTagGenerator);
        generator.addProvider(event.includeServer(), new ModItemTagGenerator(packOutput, lookupProvider, blockTagGenerator.contentsGetter(), existingFileHelper));
        generator.addProvider(event.includeServer(), new ModFluidTagGenerator(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new ModBlockModelProvider(packOutput, existingFileHelper));

        generator.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModBlockStateProvider(packOutput, existingFileHelper));
    }
}