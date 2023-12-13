package com.tompkins_development.bettergens.forge;

import com.mojang.logging.LogUtils;
import com.tompkins_development.bettergens.forge.util.Constants;
import com.tompkins_development.bettergens.forge.register.Registrar;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Constants.MOD_ID)
public class BetterGens {

    public static final Logger LOGGER = LogUtils.getLogger();
    public static Registrar registrar;

    public BetterGens() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = net.minecraftforge.common.MinecraftForge.EVENT_BUS;
        registrar = new Registrar(forgeBus, modBus);

        registrar.register();
    }

}