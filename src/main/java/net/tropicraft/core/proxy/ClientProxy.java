package net.tropicraft.core.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.tropicraft.core.client.entity.render.*;
import net.tropicraft.core.common.entity.hostile.TropiSkellyEntity;
import net.tropicraft.core.common.entity.neutral.EIHEntity;
import net.tropicraft.core.common.entity.neutral.IguanaEntity;
import net.tropicraft.core.common.entity.passive.EntityKoaHunter;
import net.tropicraft.core.common.entity.passive.TropiCreeperEntity;
import net.tropicraft.core.common.entity.placeable.UmbrellaEntity;
import net.tropicraft.core.common.item.IColoredItem;
import net.tropicraft.core.common.item.UmbrellaItem;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends CommonProxy {
    
    public ClientProxy() {
        super();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }
    
    private void clientSetup(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityKoaHunter.class, RenderKoaMan::new);
        RenderingRegistry.registerEntityRenderingHandler(TropiCreeperEntity.class, TropiCreeperRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(IguanaEntity.class, IguanaRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(UmbrellaEntity.class, UmbrellaRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropiSkellyEntity.class, TropiSkellyRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EIHEntity.class, EIHRenderer::new);

        for (final UmbrellaItem item : UmbrellaItem.getAllItems()) {
            registerColoredItem(item);
        }
    }

    @Override
    public <T extends Item & IColoredItem> void registerColoredItem(T item) {
        IItemColor itemColor = item.getColorHandler();
        if (itemColor != null) {
            Minecraft.getInstance().getItemColors().register(itemColor, item);
        } else {
            System.err.println("!!! FAILED TO REGISTER COLOR HANDLER FOR ITEM " + item.getRegistryName() + " !!!");
        }
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