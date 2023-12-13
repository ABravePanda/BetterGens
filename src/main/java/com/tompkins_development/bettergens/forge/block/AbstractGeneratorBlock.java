package com.tompkins_development.bettergens.forge.block;

import com.tompkins_development.bettergens.forge.block.entity.AbstractGeneratorBlockEntity;
import com.tompkins_development.bettergens.forge.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;

public abstract class AbstractGeneratorBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty GENERATING = BooleanProperty.create("generating");

    public AbstractGeneratorBlock() {
        super(BlockBehaviour.Properties.of().strength(0.5f).sound(SoundType.METAL));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(GENERATING, false));
    }

    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite()).setValue(GENERATING, false);
    }

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }


    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }


    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING).add(GENERATING);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getValue(GENERATING) ? 15 : 0;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if(pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if(blockEntity instanceof AbstractGeneratorBlockEntity) {
                ((AbstractGeneratorBlockEntity) blockEntity).drops();
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(!pLevel.isClientSide) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if(blockEntity instanceof AbstractGeneratorBlockEntity) {
                NetworkHooks.openScreen(((ServerPlayer) pPlayer), (MenuProvider) blockEntity, pPos);
            } else {
                throw new IllegalStateException("No container provider found");
            }
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide);
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if(getWorkSound() == null) return;

        if (pState.getValue(GENERATING)) {
            double d0 = (double) pPos.getX() + 0.5D;
            double d1 = (double) pPos.getY();
            double d2 = (double) pPos.getZ() + 0.5D;
            if (pRandom.nextDouble() < 0.1D) {
                pLevel.playLocalSound(d0, d1, d2, getWorkSound(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }
        }
    }

    public abstract SoundEvent getWorkSound();



    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createGeneratorTicker(Level pLevel, BlockEntityType<T> pServerType, BlockEntityType<? extends AbstractGeneratorBlockEntity> pClientType) {
        return pLevel.isClientSide ? null : createTickerHelper(pServerType, pClientType, AbstractGeneratorBlockEntity::serverTick);
    }
}
