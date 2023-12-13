package com.tompkins_development.bettergens.forge.block.entity;

import com.tompkins_development.bettergens.forge.recipe.AbstractGeneratorRecipe;
import com.tompkins_development.bettergens.forge.recipe.StirlingGeneratorRecipe;
import com.tompkins_development.bettergens.forge.ui.ModMenuTypes;
import com.tompkins_development.bettergens.forge.ui.menu.StirlingGeneratorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class StirlingGeneratorBlockEntity extends AbstractGeneratorBlockEntity {

    public StirlingGeneratorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.STIRLING_GENERATOR.get(), pPos, pBlockState);
    }

    @Override
    public MenuType<?> getMenuType() {
        return ModMenuTypes.STIRLING_GENERATOR_MENU.get();
    }


    @Override
    public ItemStackHandler createItemHandler() {
        return new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {

                return switch (slot) {
                    case 0 -> true;
                    default -> false;
                };
            }
        };
    }

    @Override
    public int getTicksUntilComplete() {
        return 80;
    }

    @Override
    public int getMaxEnergyStorage() {
        return 25000;
    }

    @Override
    public Optional<AbstractGeneratorRecipe> getCurrentRecipe() {
        SimpleContainer inv = new SimpleContainer(this.itemHandler.getSlots());
        for(int i = 0; i < this.itemHandler.getSlots(); i++)
            inv.setItem(i, this.itemHandler.getStackInSlot(i));

        return this.level.getRecipeManager().getRecipeFor(StirlingGeneratorRecipe.Type.INSTANCE, inv, level);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Stirling Generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new StirlingGeneratorMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState state) {
        Optional<AbstractGeneratorRecipe> recipe = getCurrentRecipe();
        recipe.ifPresent((recipe1) -> this.updateTicksUntilComplete(recipe1.getBurnTimeTicks()));
        super.tick(level, blockPos, state);
    }
}
