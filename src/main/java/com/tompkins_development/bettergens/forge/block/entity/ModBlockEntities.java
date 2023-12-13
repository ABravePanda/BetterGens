package com.tompkins_development.bettergens.forge.block.entity;

import com.tompkins_development.bettergens.forge.block.ModBlocks;
import com.tompkins_development.bettergens.forge.util.Constants;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Constants.MOD_ID);

    public static final RegistryObject<BlockEntityType<StirlingGeneratorBlockEntity>> STIRLING_GENERATOR = BLOCK_ENTITIES.register("stirling_generator_block_entity", () ->
            BlockEntityType.Builder.of(StirlingGeneratorBlockEntity::new, ModBlocks.STIRLING_GENERATOR.get()).build(null));

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}
