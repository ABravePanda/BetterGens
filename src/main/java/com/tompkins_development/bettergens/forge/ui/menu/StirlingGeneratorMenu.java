package com.tompkins_development.bettergens.forge.ui.menu;

import com.tompkins_development.bettergens.forge.block.entity.AbstractGeneratorBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class StirlingGeneratorMenu extends AbstractGeneratorMenu {


    public StirlingGeneratorMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        super(pContainerId, inv, extraData);
    }

    public StirlingGeneratorMenu(int pContainerId, Inventory inv, AbstractGeneratorBlockEntity blockEntity, ContainerData data) {
        super(pContainerId, inv, blockEntity, data);
    }

    @Override
    public void addItemSlots(IItemHandler itemHandler) {
        this.addSlot(new SlotItemHandler(itemHandler, 0, 80, 37));
    }
}
