package net.tropicraft.core.proxy;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.tropicraft.core.client.entity.render.RenderKoaMan;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;
import net.tropicraft.core.common.entity.passive.EntityKoaHunter;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends CommonProxy {
    
    public ClientProxy() {
        super();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }
    
    private void clientSetup(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityKoaHunter.class, RenderKoaMan::new);
    }
   
    @Override
    protected void preInit(FMLCommonSetupEvent event) {
        super.preInit(event);
    }
    
    @Override
    protected void postInit(InterModProcessEvent event) {
        super.postInit(event);
    }
}