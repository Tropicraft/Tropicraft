package net.tropicraft.core.proxy;

import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.tropicraft.core.common.item.IColoredItem;

public class CommonProxy {
    
    public CommonProxy() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::preInit);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::postInit);
    }

    public <T extends Item & IColoredItem> void registerColoredItem(T item) {}
    
    protected void preInit(FMLCommonSetupEvent event) {

    }
    
    protected void init(InterModEnqueueEvent event) {

    }
    
    protected void postInit(InterModProcessEvent event) {

    }

    public World getClientWorld() {
        return null;
    }
}
