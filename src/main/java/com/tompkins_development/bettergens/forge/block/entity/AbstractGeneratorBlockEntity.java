package com.tompkins_development.bettergens.forge.block.entity;

import com.tompkins_development.bettergens.forge.block.AbstractGeneratorBlock;
import com.tompkins_development.bettergens.forge.handler.EnergyStorageHandler;
import com.tompkins_development.bettergens.forge.recipe.AbstractGeneratorRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class AbstractGeneratorBlockEntity extends BlockEntity implements MenuProvider {

    public int ticksUntilComplete = 80;
    public int currentTicks = 0;
    public int maxEnergyStorage = 100000;
    public int energyToAdd = 0;

    public ContainerData data;
    public ItemStackHandler itemHandler;
    public EnergyStorageHandler energyHandler;

    public LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    public LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    public final int MAIN_INPUT_SLOT = 0;


    public AbstractGeneratorBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        this.data = createData();
        this.itemHandler = createItemHandler();
        this.ticksUntilComplete = getTicksUntilComplete();
        this.maxEnergyStorage = getMaxEnergyStorage();
        this.energyHandler = createEnergyHandler();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        if(cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("generator.progress", currentTicks);
        pTag.putInt("energy", energyHandler.getEnergyStored());
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        currentTicks = pTag.getInt("generator.progress");
        energyHandler.setEnergy(pTag.getInt("energy"));
        super.load(pTag);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyHandler.invalidate();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyEnergyHandler = LazyOptional.of(() -> energyHandler);
    }

    public void updateTicksUntilComplete(int ticks) {
        this.ticksUntilComplete = ticks;
    }

    public abstract MenuType<?> getMenuType();

    public ContainerData createData() {
        return new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> AbstractGeneratorBlockEntity.this.currentTicks;
                    case 1 -> AbstractGeneratorBlockEntity.this.ticksUntilComplete;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> AbstractGeneratorBlockEntity.this.currentTicks = pValue;
                    case 1 -> AbstractGeneratorBlockEntity.this.ticksUntilComplete = pValue;
                };
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public abstract ItemStackHandler createItemHandler();

    private EnergyStorageHandler createEnergyHandler() {
        return new EnergyStorageHandler(maxEnergyStorage, maxEnergyStorage) {
            @Override
            public void onEnergyChanged() {
                setChanged();
                getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        };
    }

    public abstract int getTicksUntilComplete();

    public abstract int getMaxEnergyStorage();

    public void drops() {
        SimpleContainer inv = new SimpleContainer(itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++)
            inv.setItem(i, itemHandler.getStackInSlot(i));
        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    public IEnergyStorage getEnergyStorage() {
        return energyHandler;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState state, AbstractGeneratorBlockEntity abstractGeneratorBlockEntity) {
        abstractGeneratorBlockEntity.tick(level, blockPos, state);
    }

    /* Tick / Functionality */

    public void tick(Level level, BlockPos blockPos, BlockState state) {
        if(isBurning()) {
            burning(level, blockPos, state);
        } else {
            tryBurn(level, blockPos, state);
        }
    }

    public boolean hasRecipe() {
        Optional<AbstractGeneratorRecipe> recipe = getCurrentRecipe();
        if(recipe.isEmpty()) return false;
        return recipe.isPresent();
    }

    public void addEnergy() {
        this.energyHandler.receiveEnergy(this.energyToAdd, false);
    }

    public void burning(Level level, BlockPos blockPos, BlockState state) {
        if(completedBurning()) {
            this.currentTicks = 0;
            setChanged(level, blockPos, state);
            if(!tryBurn(level, blockPos, state))
                level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(AbstractGeneratorBlock.GENERATING, false));
        } else {
            if(!bufferFull() && !bufferWillBeFull()) {
                this.currentTicks++;
                addEnergy();
                setChanged(level, blockPos, state);
            }
        }
    }

    public boolean tryBurn(Level level, BlockPos blockPos, BlockState state) {
        if(hasRecipe()) {
            if(!bufferFull() && !bufferWillBeFull()) {
                burn();
                setChanged(level, blockPos, state);
                return true;
            }
        }
        return false;
    }

    private boolean bufferWillBeFull() {
        ItemStack item = itemHandler.getStackInSlot(MAIN_INPUT_SLOT);
        return this.energyHandler.getEnergyStored()+energyToAdd >= this.maxEnergyStorage;
    }

    private boolean bufferFull() {
        return this.energyHandler.getEnergyStored() > this.maxEnergyStorage;
    }


    private boolean completedBurning() {
        return this.currentTicks > this.ticksUntilComplete;
    }

    private void burn() {
        Optional<AbstractGeneratorRecipe> recipe = getCurrentRecipe();
        recipe.ifPresent(abstractGeneratorRecipe -> this.energyToAdd = abstractGeneratorRecipe.getFeProductionPerTick());
        this.itemHandler.extractItem(MAIN_INPUT_SLOT, 1, false);
        this.currentTicks++;
        level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(AbstractGeneratorBlock.GENERATING, true));
    }

    private boolean isBurning() {
        return this.currentTicks != 0;
    }

    public abstract Optional<AbstractGeneratorRecipe> getCurrentRecipe();

}
