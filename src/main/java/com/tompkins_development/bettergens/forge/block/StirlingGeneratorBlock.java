package com.tompkins_development.bettergens.forge.block;

import com.tompkins_development.bettergens.forge.block.entity.ModBlockEntities;
import com.tompkins_development.bettergens.forge.block.entity.StirlingGeneratorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

public class StirlingGeneratorBlock extends AbstractGeneratorBlock {

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new StirlingGeneratorBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createGeneratorTicker(pLevel, pBlockEntityType, ModBlockEntities.STIRLING_GENERATOR.get());
    }

    @Override
    public SoundEvent getWorkSound() {
        return SoundEvents.IRON_GOLEM_HURT;
    }
}
