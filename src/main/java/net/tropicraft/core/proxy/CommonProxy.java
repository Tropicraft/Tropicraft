package net.tropicraft.core.proxy;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Random;

public class CommonProxy {
    
    public CommonProxy() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::preInit);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::postInit);
    }
    
    protected void preInit(FMLCommonSetupEvent event) {

    }
    
    protected void init(InterModEnqueueEvent event) {

    }
    
    protected void postInit(InterModProcessEvent event) {

    }
}
