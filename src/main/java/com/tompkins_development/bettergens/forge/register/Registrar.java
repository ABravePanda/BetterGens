package com.tompkins_development.bettergens.forge.register;

import com.tompkins_development.bettergens.forge.block.ModBlocks;
import com.tompkins_development.bettergens.forge.block.entity.ModBlockEntities;
import com.tompkins_development.bettergens.forge.datagen.providers.ModCreativeModeTabs;
import com.tompkins_development.bettergens.forge.item.ModItems;
import com.tompkins_development.bettergens.forge.networking.ModMessages;
import com.tompkins_development.bettergens.forge.recipe.ModRecipes;
import com.tompkins_development.bettergens.forge.ui.ModMenuTypes;
import com.tompkins_development.bettergens.forge.ui.screen.StirlingGeneratorScreen;
import com.tompkins_development.bettergens.forge.util.Constants;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class Registrar {

    private IEventBus forgeEventBus;
    private IEventBus modEventBus;

    public Registrar(IEventBus forgeEventBus, IEventBus modEventBus) {
        this.forgeEventBus = forgeEventBus;
        this.modEventBus = modEventBus;
    }

    public IEventBus getForgeEventBus() {
        return forgeEventBus;
    }

    public IEventBus getModEventBus() {
        return modEventBus;
    }

    public void register() {
        registerForgeEventBus();
        registerModEventBus();
    }

    private void registerForgeEventBus() {
        IEventBus forgeEventBus = getForgeEventBus();
    }

    private void registerModEventBus() {
        IEventBus modEventBus = getModEventBus();

        modEventBus.addListener(this::commonSetup);

        ModRecipes.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
    }

    public void commonSetup(final FMLCommonSetupEvent event) {
        ModMessages.register();
    }

    @Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                MenuScreens.register(ModMenuTypes.STIRLING_GENERATOR_MENU.get(), StirlingGeneratorScreen::new);
            });
        }
    }
}
