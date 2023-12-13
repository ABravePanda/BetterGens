package com.tompkins_development.bettergens.forge.datagen.providers;


import com.tompkins_development.bettergens.forge.block.ModBlocks;
import com.tompkins_development.bettergens.forge.util.Constants;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModBlockModelProvider extends BlockModelProvider {

    public ModBlockModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        registerGenerators(List.of(
                ModBlocks.STIRLING_GENERATOR
        ));
    }

    private void registerGenerators(List<RegistryObject<Block>> generators) {
        for(RegistryObject<Block> generator : generators) {
            String path = generator.getId().getPath();
            ResourceLocation top = new ResourceLocation(Constants.MOD_ID, "block/" + path + "_top");
            ResourceLocation side = new ResourceLocation(Constants.MOD_ID, "block/" + path + "_side");
            ResourceLocation front = new ResourceLocation(Constants.MOD_ID, "block/" + path + "_front");
            this.orientable(path, side, front, top);
        }
    }
}
